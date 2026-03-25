# JackSparrow

一个基于 Spring Boot 3.5.11 的多数据源管理平台，支持 Elasticsearch 和 Redis 集成。

## 🚀 项目简介

JackSparrow 是一个灵活的数据源管理框架，提供统一的数据访问接口，支持动态配置和管理多种数据源。

## 📦 技术栈

- **框架**: Spring Boot 3.5.11
- **JDK**: Java 17
- **构建工具**: Maven
- **数据源**:
  - Elasticsearch 7.9.3
  - Redis (Jedis 5.2.0)
- **其他依赖**:
  - FastJSON 1.2.78
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

### Elasticsearch

- `ElasticSearchController` - 提供 ES 数据操作接口

### Redis

- `RedisController` - 提供 Redis 缓存操作接口

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
