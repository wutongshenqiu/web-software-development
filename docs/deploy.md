# Deploy

目前已经支持了以下五种服务的部署，包括

- [API 和缓存服务](/services/api-service)

- [数据库服务](/services/db-service)

- [消息队列服务](/services/mq-service)

- [消费(订单处理)服务](/services/consumer-service-rs)

- [反向代理服务](/services/proxy-service)

在进行部署之前，务必确认检查各个服务目录下的 `.env` 文件，并确认

- [ ] API 服务和数据库服务中的环境变量一致(数据库名、用户名、密码、端口号)

- [ ] API 服务和消息队列服务中的环境变量一致(用户名、密码、端口号)

- [ ] API 服务和反向代理服务中的环境变量一致(监听端口)

- [ ] 消费服务和消息队列服务中的环境变量一致(用户名、密码、端口号)

- [ ] 反向代理服务环境变量中对外部暴露的端口

## 步骤

> :warning: 由于部分服务之间存在依赖关系(例如 API 服务依赖于数据库服务和消息队列服务)，因此最好按照这里给出的步骤依次执行
> 
> :book: 以下命令中的 `docker compose` 可以用 `docker-compose` 代替

1. 下载项目到本地
   
   ```bash
   git clone https://github.com/wutongshenqiu/web-software-development.git
   cd web-software-development
   
   # 如果由于网络原因，导致上述命令下载过慢或者不可用，可以尝试从 gitee clone
   # https://gitee.com/mymashiro/web-software-development.git
   ```

:warning: 以下所有服务的启动命令均是相对于根目录而言的

2. 部署数据库服务
- 启动服务
  
  ```bash
  cd services/db-service
  docker compose up -d
  ```

- 等待数据库初始化完成之后(可能需要等待一段时间)导入数据。由于数据较多，此步骤可能需要等待一段时间
  
  ```bash
  unzip sql_data.zip
  docker compose exec -T db-service sh -c 'exec mysql -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" --default-character-set=utf8mb4' < sql_data/.tb_all_with_ddl.sql
  ```

- 查看数据是否导入成功
  
  ```bash
  # 查看所有的表
  docker compose exec -T db-service sh -c 'exec mysql -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" -D"${MYSQL_DATABASE}" -e "show tables;"'
  
  # 从 user 表中查询前 20 个用户的 id 和 name
  docker compose exec -T db-service sh -c 'exec mysql -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" -D"${MYSQL_DATABASE}" -e "SELECT id, name FROM tb_user LIMIT 20;" --default-character-set=utf8mb4'
  ```
3. 部署消息队列服务
   
   ```bash
   cd services/mq-service
   docker compose up -d
   ```

4. 部署消费服务
   
   - 部署服务(此步骤可能会耗时较久，可以在等待的同时执行后面的步骤，其与后面的步骤没有依赖关系)
   
   ```bash
   cd services/consumer-service-rs
   docker compose up -d
   ```
   
   - 查看服务是否正常启动
   
   ```bash
   docker compose logs
   ```

5. 部署 API 和缓存服务
   
   ```bash
   cd services/api-service
   docker compose up -d
   ```

6. 部署反向代理服务
   
   ```bash
   cd services/proxy-service
   docker compose up -d
   ```

若所有服务均部署成功，则可以通过主机 ip 和 nginx 对外提供的端口进行访问

**:book: Tips：**

导入的数据中提供了 10 个可以用于测试的用户，其登陆的用户名分别是 test1, test2, ..., test10，密码均为 123456
