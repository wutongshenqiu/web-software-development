# 数据表设计

## 总览

- 身份认证表 `tb_auth`

- 用户表 `tb_user`

- 角色表 `tb_role`

- 用户-角色表 `tb_user_role`

- 学生表 `tb_student_info`

- 班级表 `tb_class`

- 班级-宿舍分配表 `tb_class_room`

- 队伍表 `tb_group`

- 队伍成员表 `tb_group_member`

- 宿舍楼表 `tb_building`

- 宿舍房间表 `tb_room` 

- 宿舍床位表 `tb_bed` 

- 订单表 `tb_form` 

- 系统配置表 `tb_config` 

- 操作日志表 `tb_logs` 

## 详细设计

所有表单均包含字段

- `id`：自增主键

- `create_time`：创建时间，默认值为该 row 创建的时间

- `update_time`：更新时间，默认值为该 row 创建的时间

- `remark`：备注，仅管理员可见，默认值为 NULL

以上 4 个字段在后续详细说明中不再单独列出

### 身份认证表 `tb_auth`

| 字段              | 类型  | 是否可为空 | 默认值       | 是否唯一 | 注释                                                           |
| --------------- | --- | ----- | --------- | ---- | ------------------------------------------------------------ |
| username        |     | 否     |           | 否    | 登录用户名                                                        |
| password        |     | 否     |           | 否    | 登录凭据                                                         |
| user_id         |     | 否     |           | 否    | 外键，对应 `tb_user` 中的主键                                         |
| status          |     | 否     | ACTIVE(0) | 否    | 枚举类型，表示登录凭据状态。目前暂定为：AVAILABLE(0)、DENIED(1)、DELETED(2)        |
| auth_type       |     | 否     |           | 否    | 枚举类型，表示登录凭据的类型。目前暂定为：STUDENT_ID(0)、TELEPHONE(1)、WECHAT_ID(2) |
| last_login_time |     | 是     | NULL      | 否    | 最后一次登陆时间                                                     |

- 身份认证表的作用是提高登陆的可扩展性，表中的每一行表示一个用户的一种认证方式。通过这种方式可以支持一个用户多种方式登录系统

- 身份认证表和用户表是**多对一**的关系，通过外键 `user_id` 对应

- `auth_type` 字段用于表示登录的类型，例如手机登录、学号登录等

- 原设计中 `is_del` 字段被包含在 `status` 中了

### 用户表 `tb_user`

| 字段        | 类型  | 是否可为空 | 默认值        | 是否唯一    | 注释                                                |
| --------- | --- | ----- | ---------- | ------- | ------------------------------------------------- |
| name      |     | 否     |            | 否       | 用户姓名                                              |
| gender    |     | 否     | UNKNOWN(2) | 否       | 枚举类型，用户性别，目前支持 FEMALE(0)、MALE(1)、UNKNOWN(2)       |
| email     |     | 否     |            | 是       | 电子邮箱                                              |
| telephone |     | 否     |            | 是，可建立索引 | 电话号码                                              |
| status    |     | 否     | ACTIVE(0)  | 否       | 枚举类型，表示用户的状态。目前暂定为：ACTIVE(0)、DENIED(1)、DELETED(2) |

- 原设计中的 `is_del` 和 `is_deny` 字段均被包含在 `status` 中了

- 原设计中的 `last_login_time` 字段被移动到 `tb_auth` 中，这样能够显示出用户最后一次登录的时间和方式

- 原设计中的 `type` 字段被移除了，新增角色表以及 用户-角色表 将用户与角色连接起来

### 角色表 `tb_role`

| 字段          | 类型  | 是否可为空 | 默认值       | 是否唯一    | 注释                                     |
| ----------- | --- | ----- | --------- | ------- | -------------------------------------- |
| name        |     | 否     |           | 是，可建立索引 | 角色名称                                   |
| description |     | 是     | NULL      | 否       | 角色介绍                                   |
| status      |     | 否     | ACTIVE(0) | 否       | 枚举类型，表示角色的状态，目前暂定为ACTIVE(0)、DELETED(1) |

- name 字段表示角色的名称

### 用户-角色表 `tb_user_role`

| 字段      | 类型  | 是否可为空 | 默认值       | 是否唯一 | 注释                                    |
| ------- | --- | ----- | --------- | ---- | ------------------------------------- |
| user_id |     | 否     |           | 否    | 外键，对应 `tb_user` 表的主键                  |
| role_id |     | 否     |           | 否    | 外键，对应 `tb_role` 表的主键                  |
| status  |     | 否     | ACTIVE(0) | 否    | 枚举类型，表示该项的状态，目前暂定 ACTIVE(0)、DELETE(1) |

- 用户与角色是**多对多**关系，即一个用户可以对应多个角色，一个角色也可以对应多个用户

### 学生信息表 `tb_student_info`

| 字段         | 类型  | 是否可为空 | 默认值  | 是否唯一    | 注释                    |
| ---------- | --- | ----- | ---- | ------- | --------------------- |
| user_id    |     | 否     |      | 是       | 外键，对应 `tb_user` 中的主键  |
| student_id |     | 否     |      | 是，可建立索引 | 学号                    |
| class_id   |     | 是     | NULL | 否       | 外键，对应 `tb_class` 中的主键 |

- 原设计中的 `status` 被移除，因为和用户表一一对应

- 原设计中的 `verification_code` 被移除，因为组队已经进行验证，因此不需要再校验验证码

- 学生表和用户表是**一对一**的关系，通过外键 `user_id` 对应

- 学生表和班级表是**多对一**的关系，通过外键 `class_id` 对应

### 班级表 `tb_class`

| 字段     | 类型  | 是否可为空 | 默认值          | 是否唯一    | 注释                                        |
| ------ | --- | ----- | ------------ | ------- | ----------------------------------------- |
| name   |     | 否     |              | 是，可建立索引 | 班级名称                                      |
| status |     | 否     | AVAILABLE(0) | 否       | 枚举类型，表示班级的状态。目前暂定为AVAILABLE(0)、DELETED(1) |

### 班级宿舍分配表 `tb_class_room`

| 字段       | 类型  | 是否可为空 | 默认值          | 是否唯一 | 注释                                         |
| -------- | --- | ----- | ------------ | ---- | ------------------------------------------ |
| class_id |     | 否     |              | 否    | 外键，对应 `tb_class` 中的主键                      |
| room_id  |     | 否     |              | 否    | 外键，对应 `tb_room` 中的主键                       |
| status   |     | 否     | AVAILABLE(0) | 否    | 枚举类型，表示该表项的状态。目前暂定为AVAILABLE(0)、DELETED(1) |

### 组队表 `tb_group`

| 字段          | 类型  | 是否可为空 | 默认值          | 是否唯一 | 注释                                                      |
| ----------- | --- | ----- | ------------ | ---- | ------------------------------------------------------- |
| creator_id  |     | 否     |              | 否    | 外键，对应 `tb_user` 中的主键                                    |
| name        |     | 否     |              | 否    | 队伍名称                                                    |
| invite_code |     | 否     | 随机字符串        | 否    | 队伍邀请码，不可主动修改，如需更换需要调用相应接口重新生成                           |
| description |     | 否     | NULL         | 否    | 队伍描述                                                    |
| status      |     | 否     | AVAILABLE(0) | 否    | 枚举类型，用来表示队伍的状态。目前暂定AVAILABLE(0)、COMPLETED(1)、DELETED(2) |

- 原设计中 `is_del` 被移除，与 `status` 合并

- 添加 `creator_id`，表示队伍的创建者

- 队伍性别通过 `creator_id` 对应的用户性别表示

- 队伍表与用户表是**多对一**的关系，通过外键 `creator_id` 对应

### 队伍成员表 `tb_group_member`

| 字段         | 类型  | 是否可为空 | 默认值       | 是否唯一 | 注释                                 |
| ---------- | --- | ----- | --------- | ---- | ---------------------------------- |
| member_id  |     | 否     |           | 否    | 外键，对应 `tb_user` 中的主键               |
| group_id   |     | 否     |           | 否    | 外键，对应 `tb_group` 中的主键              |
| status     |     | 否     | JOINED(0) | 否    | 枚举类型，表示成员的状态，目前支持JOINED(0)、LEFT(1) |
| leave_time |     | 是     | NULL      | 否    | 离开队伍时间                             |

- 队伍成员表与用户表是**多对一**的关系，通过外键 `member_id` 对应

- 队伍成员表与队伍表是**多对一**的关系，通过外键 `group_id` 对应

- 原设计中 `creator_id` 字段被移动到队伍表以减少重复

- **队伍创建者也属于队伍成员**，即也需要创建者创建队伍时也会在队伍成员表中新建一行数据

- 成员的性别需要与创建者性别一致

### 宿舍楼表 `tb_building`

| 字段          | 类型  | 是否可为空 | 默认值          | 是否唯一    | 注释                                                   |
| ----------- | --- | ----- | ------------ | ------- | ---------------------------------------------------- |
| name        |     | 否     |              | 是，可建立索引 | 宿舍楼名称                                                |
| description |     | 是     | NULL         | 否       | 楼层描述信息                                               |
| image_url   |     | 是     | NULL         | 否       | 预览图地址，仅支持一张                                          |
| status      |     | 否     | AVAILABLE(0) | 否       | 枚举类型，用来表示宿舍楼状态AVAILABLE(0)、UNAVAILABLE(1)、DELETED(2) |

- 原设计中的 `is_del` 和 `is_valid` 与 `status` 字段合并

### 宿舍房间表 `tb_room`

| 字段          | 类型  | 是否可为空 | 默认值          | 是否唯一    | 注释                                                          |
| ----------- | --- | ----- | ------------ | ------- | ----------------------------------------------------------- |
| building_id |     | 否     |              | 否       | 外键，对应 `building` 中的主键                                       |
| name        |     | 否     |              | 是，可建立索引 | 宿舍名称                                                        |
| gender      |     | 否     |              | 否       | 枚举类型，表示性别。目前暂定FEMALE(0)、MALE(1)、UNKNOWN(2)                  |
| description |     | 是     | NULL         | 否       | 房间描述信息                                                      |
| image_url   |     | 是     | NULL         | 否       | 预览图地址，仅支持一张                                                 |
| status      |     | 否     | AVAILABLE(0) | 否       | 枚举类型，用来表示宿舍房间的状态。目前暂定AVAILABLE(0)、UNAVAILABLE(1)、DELETED(2) |

- 原设计中的 `is_del` 和 `is_valid` 与 `status` 字段合并

- 宿舍房间表与宿舍楼表是**多对一**关系，通过外键 `building_id` 对应

### 宿舍床位表 `tb_bed`

| 字段      | 类型  | 是否可为空 | 默认值          | 是否唯一    | 注释                                                               |
| ------- | --- | ----- | ------------ | ------- | ---------------------------------------------------------------- |
| user_id |     | 是     | NULL         | 是       | 外键，对应 `tb_user` 中的主键                                             |
| room_id |     | 否     |              | 否       | 外键，对应 `tb_room` 中的主键                                             |
| name    |     | 否     |              | 是，可建立索引 | 床位名称(编号)                                                         |
| status  |     | 否     | AVAILABLE(0) | 否       | 枚举类型，用来表示床位的状态AVAILABLE(0)、UNAVAILABLE(1)、OCCUPIED(2)、DELETED(3) |

- 原设计中的 `is_del` 和 `is_valid` 与 `status` 合并

- 宿舍床位表与宿舍房间表是**多对一**关系，通过外键 `room_id` 对应

- 为什么不新增一个用户-床位表
  
  - 如果不考虑记录退宿、换宿舍信息，则用户表-床位表是**一对一**关系，不需要添加一个新表
  
  - 如果考虑保存退宿、换宿舍信息
    
    - 可以单独新建一张表存储相关信息
    
    - 如果用用户-床位表来保存相关信息，则还需要添加 status 字段，即 (`user_id`, `bed_id`) 构成的 tuple 不能被添加唯一性约束，可能导致选宿舍时出现多选的情况

### 订单表 `tb_form`

| 字段           | 类型  | 是否可为空 | 默认值        | 是否唯一 | 注释                                                                         |
| ------------ | --- | ----- | ---------- | ---- | -------------------------------------------------------------------------- |
| submitter_id |     | 否     |            | 否    | 外键，对应 `tb_user` 中的主键                                                       |
| group_id     |     | 是     | NULL       | 否    | 外键，对应 `tb_group` 中的主键                                                      |
| buiding_id   |     | 否     |            | 否    | 外键，对应 `tb_building` 中的主键                                                   |
| room_id      |     | 是     | NULL       | 否    | 外键，对应 `tb_room` 中的主键                                                       |
| finish_time  |     | 是     | NULL       | 否    | 订单完成时间                                                                     |
| message      |     | 是     | NULL       | 否    | 订单处理结果信息，例如成功分配那个宿舍，失败原因等                                                  |
| status       |     | 否     | CREATED(0) | 否    | 枚举类型，表示订单状态，目前支持暂定CREATED(0)、PROCESSING(1)、FAILED(2)、SUCCESS(3)、DELETED(4) |

- 原设计中的 `is_del` 与 `status` 合并

- 原设计中的 `submit_time` 被删除
  
  - 如果记录的是前端传来的时间，则无法保证正确性
  
  - 如果记录的是后端创建的时间，则与 `create_time` 作用重复

### 系统配置表 `tb_config`

| 字段        | 类型  | 是否可为空 | 默认值       | 是否唯一    | 注释                                                |
| --------- | --- | ----- | --------- | ------- | ------------------------------------------------- |
| key_name  |     | 否     |           | 是，可建立索引 | 配置名称                                              |
| key_value |     | 否     |           | 否       | 配置值                                               |
| status    |     | 否     | ENABLE(0) | 否       | 枚举类型，用来表示配置状态，目前暂定ENABLE(0)、DISABLE(1)、DELETED(2) |

- 可以配置开始、结束选宿舍时间

- 可以配置是否按照班级选择宿舍

- 可以配置组队人数

### 操作日志表 `tb_logs`

| 字段               | 类型  | 是否可为空 | 默认值        | 是否唯一 | 注释                                                |
| ---------------- | --- | ----- | ---------- | ---- | ------------------------------------------------- |
| user_id          |     | 否     |            | 否    | 外键，对应 `tb_user` 表的主键                              |
| operation        |     | 否     |            | 否    | 操作名称                                              |
| ip               |     | 否     |            | 否    | 操作 ip 地址                                          |
| operation_detail |     | 是     |            | 否    | 操作详情                                              |
| status           |     | 否     | SUCCESS(0) | 否    | 枚举类型，表示该操作的状态。目前暂定SUCCESS(0)、FAILED(1)、DELETED(2) |
