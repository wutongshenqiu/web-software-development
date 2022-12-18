use std::collections::HashMap;
use mysql::*;
use tracing::{debug, error, info};
use futures_lite::StreamExt;
use lapin::{options::*, types::FieldTable, Connection, ConnectionProperties};
use serde::Deserialize;
use consumer::{Processor, OrderInfo, Member};


#[derive(Deserialize, Debug)]
struct UserInRabbit {
    id: i64,
    name: String,
    gender: i32
}

#[derive(Deserialize, Debug)]
struct OrderInRabbit {
    order_id: i64,
    building_id: i64,
    group_id: i64,
    group_members: Vec<UserInRabbit>
}

fn main() {
    if std::env::var("RUST_LOG").is_err() {
        std::env::set_var("RUST_LOG", "debug");
    }

    tracing_subscriber::fmt::init();

    let opts_list = [
        ("user", std::env::var("MYSQL_USER").unwrap_or_else(|_| "test".into())),
        ("password", std::env::var("MYSQL_PASSWORD").unwrap_or_else(|_| "123test321".into())),
        ("host", std::env::var("MYSQL_HOST").unwrap_or_else(|_| "localhost".into())),
        ("port", std::env::var("MYSQL_PORT").unwrap_or_else(|_| "39001".into())),
        ("db_name", std::env::var("MYSQL_DATABASE").unwrap_or_else(|_| "test".into()))
    ];
    let opts_map: HashMap<String, String> = opts_list
        .into_iter()
        .map(|(x, y)| { (x.into(), y) } )
        .collect();
    let opts = OptsBuilder::new().from_hash_map(&opts_map).unwrap();

    let pool = Pool::new(opts).unwrap();
    let conn = pool.get_conn().unwrap();
    let mut processor = Processor::new(conn);

    let rabbit_addr = std::env::var("AMQP_ADDR")
        .unwrap_or(
            format!(
                "amqp://{}:{}@{}:{}/%2f",
                std::env::var("RABBITMQ_DEFAULT_USER").unwrap_or_else(|_| "qiufeng".into()),
                std::env::var("RABBITMQ_DEFAULT_PASS").unwrap_or_else(|_| "123456".into()),
                std::env::var("RABBITMQ_HOST").unwrap_or_else(|_| "localhost".into()),
                std::env::var("RABBITMQ_PORT").unwrap_or_else(|_| "39004".into())
            )
        );

    async_global_executor::block_on(async {
        let conn = Connection::connect(&rabbit_addr, ConnectionProperties::default())
            .await
            .expect("Connection error");

        //receive channel
        let channel = conn.create_channel().await.expect("Create channel error");

        let queue_options = QueueDeclareOptions { durable: true, ..Default::default() };
        let _ = channel
            .queue_declare(
                "order",
                queue_options,
                FieldTable::default(),
            )
            .await
            .expect("queue_declare");

        let mut consumer = channel
            .basic_consume(
                "order",
                "order_consumer",
                BasicConsumeOptions::default(),
                FieldTable::default(),
            )
            .await
            .expect("basic_consume");

        info!("等待订单");
        while let Some(delivery) = consumer.next().await {
            let json_data: OrderInRabbit = serde_json::from_slice(&delivery.as_ref().unwrap().data).unwrap();
            debug!("{:?}", json_data);

            if let Ok(delivery) = delivery {
                delivery
                    .ack(BasicAckOptions::default())
                    .await
                    .expect("Ack error");
            }

            let genders: Vec<i32> = json_data.group_members
                .iter()
                .map(|x| { x.gender } )
                .collect();
            if !genders.iter().all(|&x| x == genders[0]) {
                let msg = "性别不匹配";

                processor.update_order_fail(json_data.order_id, msg);
                error!("{}", msg);
                continue
            }

            let members: Vec<Member> = json_data.group_members
                .iter()
                .map(|x| { Member { id: x.id, name: x.name.clone() } })
                .collect();

            let order_info = OrderInfo {
                order_id: json_data.order_id,
                building_id: json_data.building_id,
                group_id: json_data.group_id,
                gender: genders[0],
                members
            };
            
            match processor.process_order(order_info) {
                Ok(_) => {
                    info!("订单 {} 成功", json_data.order_id);
                },
                Err(msg) => {
                    error!("{}", msg);
                }
            }
        }
    });

}