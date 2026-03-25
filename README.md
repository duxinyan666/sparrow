# JackSparrow 🏴‍☠️

**多数据源统一管理框架** - 让数据访问更简单

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen.svg)](https://spring.io/projects/spring-boot)

一个基于 Spring Boot 3.5.11 的企业级多数据源管理平台，支持 Redis、Elasticsearch、MySQL、PostgreSQL 等多种数据源，提供分布式锁、连接池监控、RESTful API 等开箱即用的功能。

## 🚀 项目简介

JackSparrow 是一个灵活的数据源管理框架，提供统一的数据访问接口，支持动态配置和管理多种数据源。通过插件化架构，可以轻松扩展新的数据源类型。

## ✨ 核心特性

- 🔌 **多数据源支持**: Redis、Elasticsearch、MySQL、PostgreSQL
- 🔄 **动态数据源路由**: 运行时动态注册/注销数据源
- 🌐 **跨机器数据库访问**: 通过参数控制查询不同 IP 的数据库
- 🔒 **分布式锁**: 基于 Redis 的分布式锁实现
- 📊 **连接池监控**: HikariCP 连接池实时指标
- 📈 **可观测性**: Prometheus + Grafana 监控
- 📖 **API 文档**: Swagger/OpenAPI 3.0
- 🐳 **容器化**: Docker Compose 一键部署
- 🧪 **单元测试**: JUnit 5 + Mockito
- 🏗️ **MyBatis Plus**: 集成 MyBatis Plus  ORM 框架

## 📦 技术栈

- **框架**: Spring Boot 3.5.11
- **JDK**: Java 17+
- **构建工具**: Maven 3.6+
- **数据源**:
  - Redis (Jedis 5.2.0)
  - Elasticsearch 7.9.3
  - MySQL 8.0 (HikariCP 5.1.0)
  - PostgreSQL 15 (HikariCP 5.1.0)
- **监控**:
  - Micrometer + Prometheus
  - Grafana
  - Spring Boot Actuator
- **文档**: SpringDoc OpenAPI 2.3.0
- **其他**:
  - FastJSON 2.0.43
  - Lombok
  - Commons IO 2.20.0

## 🏗️ 项目结构

```
JackSparrow/
├── src/main/java/com/weilin/
│   ├── JackSparrowApplication.java    # 主启动类
│   ├── common/
│   │   ├── annotation/
│   │   │   └── MiddlewareType.java    # 中间件类型注解
│   │   └── utils/
│   │       └── JSONUtils.java         # JSON 工具类
│   ├── config/
│   │   └── DataSourceConfig.java      # 数据源配置
│   ├── controller/
│   │   ├── ElasticSearchController.java  # ES 控制器
│   │   └── RedisController.java          # Redis 控制器
│   ├── datasource/
│   │   ├── DataSourceManager.java     # 数据源管理器
│   │   ├── DataSourceProvider.java    # 数据源提供者接口
│   │   ├── ProviderRegistry.java      # 提供者注册表
│   │   └── provider/
│   │       ├── ElasticSearchProvider.java  # ES 提供者
│   │       └── RedisProvider.java          # Redis 提供者
│   ├── enums/
│   │   └── ConfigKeyEnum.java         # 配置键枚举
│   ├── pojo/
│   │   └── Result.java                # 统一返回结果
│   └── service/
│       ├── ElasticSearchService.java  # ES 服务接口
│       └── impl/
│           └── ElasticServiceServiceImpl.java  # ES 服务实现
├── src/main/resources/
│   ├── application.properties         # 应用配置
│   └── dataSource.json                # 数据源配置文件
└── pom.xml                            # Maven 配置
```

## ⚙️ 配置说明

### 数据源配置 (dataSource.json)

项目使用 JSON 文件配置数据源，支持动态加载：

```json
[
  {
    "sourceType": "elasticSearch",
    "sourceName": "elasticName",
    "host": "localhost",
    "port": 9200,
    "scheme": "http"
  },
  {
    "sourceType": "redis",
    "sourceName": "redisName",
    "host": "localhost",
    "port": 6379,
    "password": "",
    "params": {
      "timeOut": 2000,
      "maxTotal": 5,
      "maxIdle": 3,
      "minIdle": 2
    }
  }
]
```

## 🔧 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- Elasticsearch 7.9.3 (可选)
- Redis (可选)

### 构建项目

```bash
mvn clean install
```

### 运行项目

```bash
mvn spring-boot:run
```

或直接运行主类 `JackSparrowApplication.java`

## 📡 API 接口

启动后访问 Swagger 文档：http://localhost:8080/swagger-ui.html

### Redis 操作

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/redis/set` | PUT | 设置字符串值 |
| `/api/redis/setex` | PUT | 设置带过期时间的值 |
| `/api/redis/get` | GET | 获取字符串值 |
| `/api/redis/del` | DELETE | 删除键 |
| `/api/redis/exists` | GET | 检查键是否存在 |
| `/api/redis/expire` | POST | 设置过期时间 |
| `/api/redis/ttl` | GET | 获取剩余过期时间 |
| `/api/redis/incr` | POST | 自增 |
| `/api/redis/decr` | POST | 自减 |
| `/api/redis/hset` | PUT | 哈希设置 |
| `/api/redis/hget` | GET | 哈希获取 |
| `/api/redis/lpush` | POST | 列表左侧推送 |
| `/api/redis/rpop` | POST | 列表右侧弹出 |

### 数据库操作 (MySQL/PostgreSQL)

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/database/query` | POST | 执行 SQL 查询 |
| `/api/database/update` | POST | 执行 SQL 更新 |
| `/api/database/insert` | POST | 执行 SQL 插入 |
| `/api/database/batch` | POST | 批量执行 |
| `/api/database/table/exists` | GET | 检查表是否存在 |
| `/api/database/table/columns` | GET | 获取表结构 |
| `/api/database/pool/status` | GET | 连接池状态 |
| `/api/database/test` | GET | 测试连接 |

### 分布式锁

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/lock/try` | POST | 尝试获取锁 |
| `/api/lock/try-retry` | POST | 带重试获取锁 |
| `/api/lock/unlock` | POST | 释放锁 |
| `/api/lock/status` | GET | 检查锁状态 |
| `/api/lock/renew` | POST | 续期锁 |
| `/api/lock/ttl` | GET | 获取锁剩余时间 |

### Elasticsearch

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/elasticSearch/getMapping` | GET | 获取索引映射 |
| `/api/elasticSearch/addDoc` | POST | 批量插入文档 |
| `/api/elasticSearch/select` | POST | 多条件查询 |

### 健康检查

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/health` | GET | 系统健康检查 |
| `/api/health/datasources` | GET | 数据源状态 |
| `/api/health/info` | GET | 系统详细信息 |
| `/actuator/prometheus` | GET | Prometheus 指标 |

### 动态数据源

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/datasource/register` | POST | 注册数据源 |
| `/api/datasource/unregister/{key}` | DELETE | 注销数据源 |
| `/api/datasource/list` | GET | 获取数据源列表 |
| `/api/datasource/{key}` | GET | 获取数据源详情 |
| `/api/datasource/{key}/test` | GET | 测试连接 |

### 通用 CRUD

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/crud/query` | POST | 通用查询（支持任意 SQL） |
| `/api/crud/insert` | POST | 通用插入 |
| `/api/crud/update` | POST | 通用更新 |
| `/api/crud/delete` | POST | 通用删除 |
| `/api/crud/page` | POST | 分页查询 |
| `/api/crud/tables` | GET | 获取表列表 |
| `/api/crud/table/columns` | GET | 获取表结构 |
| `/api/crud/quick-query` | POST | 快速查询（注册 + 查询） |

## 🎯 核心特性

1. **多数据源支持**: 统一管理 Elasticsearch 和 Redis
2. **动态配置**: 通过 JSON 文件动态加载数据源配置
3. **插件化架构**: 基于 Provider 模式，易于扩展新数据源
4. **统一响应**: 使用 `Result` 类封装统一返回格式

## 🛠️ 开发指南

### 添加新数据源

1. 实现 `DataSourceProvider` 接口
2. 在 `ProviderRegistry` 中注册
3. 更新 `dataSource.json` 配置

### 自定义配置

修改 `src/main/resources/application.properties`

## 📝 注意事项

- 首次运行前请确保 Elasticsearch 和 Redis 服务已启动
- 生产环境请修改默认配置中的密码和连接信息
- FastJSON 版本较旧，建议升级到最新版本以提高安全性

## 📄 许可证

MIT License

## 👤 作者

weilin-0821

---

*Last updated: 2026-03-25*
