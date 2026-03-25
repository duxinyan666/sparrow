# 🏴‍☠️ JackSparrow 项目总结

## 📊 项目概览

**JackSparrow** 是一个企业级多数据源统一管理框架，基于 Spring Boot 3.5.11 构建，提供开箱即用的数据源管理、分布式锁、监控告警等功能。

---

## ✨ 核心功能

### 1. 多数据源支持

| 数据源 | 状态 | 连接池 | 说明 |
|--------|------|--------|------|
| Redis | ✅ | Jedis Pool | 缓存/分布式锁 |
| Elasticsearch | ✅ | REST Client | 搜索引擎 |
| MySQL | ✅ | HikariCP | 关系型数据库 |
| PostgreSQL | ✅ | HikariCP | 关系型数据库 |

### 2. 分布式锁

- 基于 Redis 实现
- 支持自动过期
- 支持重试机制
- 支持锁续期
- 防止误删（UUID 验证）

### 3. 监控体系

| 组件 | 端口 | 说明 |
|------|------|------|
| Actuator | 8080/actuator | Spring Boot 原生指标 |
| Prometheus | 9090 | 指标采集 |
| Grafana | 3000 | 可视化仪表盘 |

### 4. API 文档

- Swagger/OpenAPI 3.0
- 自动生成的交互式文档
- 在线测试接口

---

## 📁 项目结构

```
JackSparrow/
├── src/main/java/com/weilin/
│   ├── JackSparrowApplication.java    # 启动类
│   ├── common/                        # 通用模块
│   │   ├── annotation/                # 自定义注解
│   │   └── utils/                     # 工具类
│   ├── config/                        # 配置类
│   │   ├── MetricsConfig.java         # 监控配置
│   │   └── RedisConfig.java           # Redis 配置
│   ├── controller/                    # REST 控制器
│   │   ├── DatabaseController.java    # 数据库 API
│   │   ├── DistributedLockController.java  # 分布式锁 API
│   │   ├── ElasticSearchController.java    # ES API
│   │   ├── HealthController.java      # 健康检查
│   │   └── RedisController.java       # Redis API
│   ├── datasource/                    # 数据源层
│   │   ├── DataSourceManager.java     # 数据源管理器
│   │   ├── DataSourceProvider.java    # 提供者接口
│   │   ├── ProviderRegistry.java      # 注册表
│   │   └── provider/                  # 具体实现
│   │       ├── ElasticSearchProvider.java
│   │       ├── MySqlProvider.java
│   │       ├── PostgreSQLProvider.java
│   │       └── RedisProvider.java
│   ├── enums/                         # 枚举
│   ├── exception/                     # 异常处理
│   │   ├── DataSourceException.java
│   │   ├── ConfigException.java
│   │   ├── ConnectionException.java
│   │   └── GlobalExceptionHandler.java
│   ├── pojo/                          # 数据对象
│   ├── service/                       # 服务层
│   │   ├── DatabaseService.java
│   │   ├── DistributedLockService.java
│   │   ├── ElasticSearchService.java
│   │   ├── RedisService.java
│   │   └── impl/                      # 实现类
│   └── ...
├── src/main/resources/
│   ├── application.properties         # 应用配置
│   ├── dataSource.json                # 数据源配置
│   ├── logback-spring.xml             # 日志配置
│   └── init/                          # 初始化脚本
│       ├── mysql/
│       └── postgres/
├── src/test/                          # 单元测试
├── docker-compose.yml                 # Docker 编排
├── Dockerfile                         # 容器镜像
├── monitoring/prometheus.yml          # Prometheus 配置
├── README.md                          # 项目说明
├── QUICKSTART.md                      # 快速开始
├── EXAMPLES.md                        # 使用示例
├── CONTRIBUTING.md                    # 贡献指南
└── CHANGELOG.md                       # 变更日志
```

---

## 🚀 快速启动

### 方式 1：Docker Compose（推荐）

```bash
# 一键启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

访问：
- API 文档：http://localhost:8080/swagger-ui.html
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin123)

### 方式 2：本地运行

```bash
# 构建
mvn clean package -DskipTests

# 运行
java -jar target/jacksparrow-0.0.1-SNAPSHOT.jar
```

---

## 📡 API 接口

### Redis (15+ 接口)
- 字符串操作：set, get, setex, del, incr, decr
- 哈希操作：hset, hget, hgetAll
- 列表操作：lpush, rpop, lrange
- 其他：exists, expire, ttl, status

### 数据库 (8 接口)
- SQL 操作：query, update, insert, batch
- 元数据：table/exists, table/columns
- 监控：pool/status, test

### 分布式锁 (6 接口)
- 锁操作：try, try-retry, unlock, renew
- 状态：status, ttl

### Elasticsearch (3 接口)
- getMapping, addDoc, select

### 健康检查 (4 接口)
- health, health/datasources, health/info, actuator/prometheus

---

## 🛠️ 技术栈

### 核心框架
- Spring Boot 3.5.11
- JDK 17+
- Maven 3.6+

### 数据源
- Jedis 5.2.0 (Redis)
- HikariCP 5.1.0 (MySQL/PG)
- Elasticsearch REST Client 7.9.3

### 监控
- Micrometer
- Prometheus
- Grafana
- Spring Boot Actuator

### 文档
- SpringDoc OpenAPI 2.3.0

### 其他
- FastJSON 2.0.43
- Lombok
- Commons IO 2.20.0
- Logback

---

## 📊 代码统计

| 类别 | 数量 |
|------|------|
| Java 类 | 30+ |
| REST API | 35+ |
| 单元测试 | 15+ |
| 配置文件 | 10+ |
| 文档文件 | 8 |

---

## 🔐 安全特性

- 统一异常处理
- SQL 参数化查询（防注入）
- 连接池隔离
- 分布式锁防误删
- 敏感配置分离

---

## 📈 性能优化

- HikariCP 高性能连接池
- 连接复用
- 异步日志
- 指标监控
- 连接池调优参数

---

## 🎯 使用场景

1. **微服务数据源管理** - 统一管理多个数据源
2. **分布式缓存** - Redis 操作封装
3. **分布式锁** - 防止并发冲突
4. **多租户系统** - 动态数据源切换
5. **数据同步** - 跨数据源操作
6. **监控告警** - 实时指标采集

---

## 📝 待办事项

### 短期
- [ ] 添加 MongoDB 支持
- [ ] 实现数据源动态添加/删除
- [ ] 添加连接池告警
- [ ] 完善 Grafana 仪表盘模板

### 中期
- [ ] 支持读写分离
- [ ] 实现分库分表
- [ ] 添加 SQL 审计日志
- [ ] 支持分布式事务

### 长期
- [ ] Service Mesh 集成
- [ ] 多集群支持
- [ ] 自动故障转移
- [ ] AI 辅助 SQL 优化

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

详见：[CONTRIBUTING.md](CONTRIBUTING.md)

---

## 📜 许可证

MIT License

---

## 👥 致谢

感谢所有贡献者和使用者！

---

**项目地址**: https://github.com/duxinyan666/sparrow/tree/dxy

**最后更新**: 2026-03-25
