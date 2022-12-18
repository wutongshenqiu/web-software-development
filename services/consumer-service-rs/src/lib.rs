use chrono::NaiveDateTime;
use mysql::prelude::*;
use mysql::{PooledConn, Row};
use tracing::{debug, error};

#[derive(Debug, PartialEq, Eq)]
struct Bed {
    id: i64,
    create_time: NaiveDateTime,
    remark: Option<String>,
    update_time: NaiveDateTime,
    bed_status: i32,
    name: String,
    room_id: i64,
    user_id: Option<i64>
}

#[derive(Debug, PartialEq, Eq)]
struct Order {
    id: i64,
    create_time: NaiveDateTime,
    remark: Option<String>,
    update_time: NaiveDateTime,
    finish_time: Option<NaiveDateTime>,
    form_status: i32,
    result_content: Option<String>,
    building_id: i64,
    group_id: i64,
    room_id: Option<i64>,
    submitter_id: i64
}

#[derive(Debug)]
pub struct Member {
    pub id: i64,
    pub name: String
}

#[derive(Debug)]
pub struct OrderInfo {
    pub order_id: i64,
    pub building_id: i64,
    pub group_id: i64,
    pub gender: i32,
    pub members: Vec<Member>
}

pub struct Processor {
    conn: PooledConn
}

impl Processor {
    pub fn new(conn: PooledConn) -> Processor {
        Self { conn }
    }

    pub fn process_order(&mut self, order_info: OrderInfo) -> Result<(), String> {
        // 1. 检查成员是否已有床位
        self.check_members_had_bed(&order_info)?;

        // 2，检查订单状态是否合法，并修改订单状态为处理中
        self.check_and_update_order(&order_info)?;

        // 3. 查询对应宿舍楼中是否有满足条件的 room
        //      - 如果没有符合条件的 room，则修改订单状态和相应的信息，结束处理流程
        let room_opt: Option<i64> = self.get_available_room(&order_info)?;

        // 4. 如果有符合条件的 room，则继续下述操作
        //      1). 获取该房间的所有可用床位
        //      2). 将用户信息添加至床位表
        //      3). 修改队伍信息为已完成
        //      4). 修改订单信息为成功
        match room_opt {
            Some(room_id) => {
                debug!("Find available room `{}`", room_id);
                
                let bed_ids = self.get_available_beds_by_room(room_id);
                debug!("Available beds: {:?}", bed_ids);

                let user_ids: Vec<i64> = order_info.members.iter().map(|x| x.id).collect();
                if bed_ids.len() < user_ids.len() {
                    self.no_available_room_fail(order_info.order_id)?;
                }
                self.allocate_beds(user_ids, bed_ids);

                let sql = format!(
                    "UPDATE tb_group SET group_status=1 WHERE id={}", order_info.group_id
                );
                debug!("{}", sql);
                self.conn.query_drop(sql).unwrap();

                let sql = format!(
                    "UPDATE tb_form SET form_status=3, result_content='成功', room_id={} WHERE id={}",
                    room_id, order_info.order_id
                );
                debug!("{}", sql);
                self.conn.query_drop(sql).unwrap();
                
                Ok(())      
            }
            None => {
                self.no_available_room_fail(order_info.order_id)
            }
        }
    }

    pub fn update_order_fail(&mut self, order_id: i64, result_content: &str) {
        let sql = format!(
            "UPDATE tb_form SET form_status=2, result_content='{}' WHERE id={}",
            result_content, order_id
        );
        debug!("{}", sql);

        self.conn.query_drop(sql).unwrap();
    }

    fn no_available_room_fail(&mut self, order_id: i64) -> Result<(), String> {
        let msg = "没有可用的房间";
        self.update_order_fail(order_id, msg);
        error!("{}", msg);

        Err(msg.into())
    }

    fn allocate_beds(&mut self, user_ids: Vec<i64>, bed_ids: Vec<i64>) {
        for (user_id, bed_id) in user_ids.iter().zip(bed_ids.iter()) {
            let sql = format!(
                "UPDATE tb_bed SET bed_status=2, user_id={} WHERE id={}",
                user_id, bed_id
            );
            debug!("{}", sql);

            self.conn.query_drop(sql).unwrap();
        }
    }

    fn get_available_beds_by_room(&mut self, room_id: i64) -> Vec<i64> {
        let sql = format!(
            "SELECT tb_bed.id FROM tb_bed WHERE room_id={} AND bed_status=0",
            room_id
        );
        debug!("{}", sql);

        self.conn.query_map(sql, |x: i64| { x }).unwrap().into_iter().collect()
    }

    fn get_available_room(&mut self, order_info: &OrderInfo) -> Result<Option<i64>, String> {
        let sql = format!(
            "
            WITH tb_room_id AS
            (
                SELECT tb_room.id as room_id
                FROM tb_room
                INNER JOIN tb_building ON tb_room.building_id = tb_building.id
                WHERE
                    building_id = {} AND
                    gender = {}
            )
        
            SELECT tb_bed.room_id AS room_id, COUNT(*) AS bed_cnt
            FROM tb_bed
            INNER JOIN tb_room_id ON tb_bed.room_id = tb_room_id.room_id
            WHERE bed_status = 0
            GROUP BY room_id
            HAVING bed_cnt >= {}
            LIMIT 1
            ",
            order_info.building_id, order_info.gender, order_info.members.len()
        );
        debug!("{}", sql);

        let row: Option<Row> = self.conn.query_first(sql).unwrap();
        
        match row {
            Some(mut row) => {
                Ok(row.take::<i64, &str>("room_id"))
            },
            None => Ok(None)
        }
        
    }

    fn check_and_update_order(&mut self, order_info: &OrderInfo) -> Result<(), String> {
        let sql = format!("SELECT * FROM tb_form WHERE id={}", order_info.order_id);
        debug!("{}", sql);

        let mut order_row: Row = self.conn.query_first(sql)
        .unwrap()
        .into_iter()
        .next()
        .unwrap();
        
        debug!("{:?}", order_row);
        
        let form_status: i32 = order_row.take("form_status").unwrap();
        if form_status != 0 {
            let msg = format!("错误的订单状态 {}", form_status);
            error!("{}", msg);

            return Err(msg);
        }
        
        // 更新订单状态为 PROCESSING
        let sql = format!("UPDATE tb_form SET form_status=1 WHERE id={}", order_info.order_id);
        debug!("{}", sql);

        self.conn.query_drop(sql).unwrap();

        Ok(())
    }

    fn check_members_had_bed(&mut self, order_info: &OrderInfo) -> Result<(), String> {
        for member in &order_info.members {
            let sql = format!("SELECT EXISTS(SELECT 1 FROM tb_bed where user_id = {})", member.id);
            debug!("{}", sql);

            let flag: bool = self.conn.query_first(sql).unwrap().unwrap();
            if flag {
                let msg = format!("{} 已有床位", {&member.name});
                error!("{}", msg);
                self.update_order_fail(order_info.order_id, &msg);

                return Err(msg);
            }
        }

        Ok(())
    }
}
