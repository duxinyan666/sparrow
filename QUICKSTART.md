# 快速开始指南

## 环境要求

- JDK 17+
- Maven 3.6+
- Redis 7+ (可选，用于 Redis 功能)
- Elasticsearch 7.9.3 (可选，用于 ES 功能)

## 快速启动

### 方式 1：使用 Docker Compose（推荐）

一键启动所有服务（应用 + Redis + Elasticsearch）：

```bash
docker-compose up -d
```

访问：
- 应用：http://localhost:8080
- Swagger 文档：http://localhost:8080/swagger-ui.html
- Redis: localhost:6379
- Elasticsearch: http://localhost:9200

查看日志：
```bash
docker-compose logs -f jacksparrow
```

停止服务：
```bash
docker-compose down
```

### 方式 2：本地运行

1. 确保 Redis 和 Elasticsearch 已启动

2. 构建项目：
```bash
mvn clean package -DskipTests
```

3. 运行应用：
```bash
java -jar target/jacksparrow-0.0.1-SNAPSHOT.jar
```

或使用 Maven：
```bash
mvn spring-boot:run
```

## API 文档

启动后访问：http://localhost:8080/swagger-ui.html

## 主要接口

### Redis 操作

```bash
# 设置值
curl -X PUT "http://localhost:8080/api/redis/set?sourceName=redisName&key=mykey&value=myvalue"

# 获取值
curl "http://localhost:8080/api/redis/get?sourceName=redisName&key=mykey"

# 删除键
curl -X DELETE "http://localhost:8080/api/redis/del?sourceName=redisName&keys=mykey"

# 健康检查
curl "http://localhost:8080/api/redis/status?sourceName=redisName"
```

### Elasticsearch 操作

```bash
# 获取映射
curl "http://localhost:8080/api/elasticSearch/getMapping?indexName=myindex"

# 添加文档
curl -X POST "http://localhost:8080/api/elasticSearch/addDoc" \
  -H "Content-Type: application/json" \
  -d '{"indexName":"myindex","JSONArray":[{"id":1,"name":"test"}]}'

# 查询
curl -X POST "http://localhost:8080/api/elasticSearch/select" \
  -H "Content-Type: application/json" \
  -d '{"indexName":"myindex","params":{"field":"value"}}'
```

### 健康检查

```bash
# 系统健康
curl "http://localhost:8080/api/health"

# 数据源状态
curl "http://localhost:8080/api/health/datasources"

# 详细信息
curl "http://localhost:8080/api/health/info"
```

## 配置说明

### 数据源配置 (src/main/resources/dataSource.json)

```json
[
  {
    "sourceType": "redis",
    "sourceName": "redisName",
    "host": "localhost",
    "port": 6379,
    "password": "",
    "params": {
      "timeOut": 2000,
      "maxTotal": 10,
      "maxIdle": 5,
      "minIdle": 2
    }
  },
  {
    "sourceType": "elasticSearch",
    "sourceName": "elasticName",
    "host": "localhost",
    "port": 9200,
    "scheme": "http"
  }
]
```

### 应用配置 (src/main/resources/application.properties)

主要配置项：
- `server.port`: 服务端口（默认 8080）
- `logging.level.*`: 日志级别
- `springdoc.*`: Swagger 文档配置

## 开发指南

### 添加新数据源

1. 创建 Provider 实现类：

```java
@MiddlewareType("mydb")
@Component
public class MyDbProvider implements DataSourceProvider {
    @Override
    public Object create(JSONObject config) {
        // 创建连接
        return new MyDbClient(config);
    }
}
```

2. 在 `dataSource.json` 中添加配置

3. 创建 Service 和 Controller（可选）

### 运行测试

```bash
mvn test
```

### 构建 Docker 镜像

```bash
docker build -t jacksparrow:latest .
```

## 监控

### Actuator 端点

- 健康检查：http://localhost:8080/actuator/health
- 应用信息：http://localhost:8080/actuator/info
- 指标：http://localhost:8080/actuator/metrics

### 日志

日志文件位于 `logs/` 目录：
- `jacksparrow.info.log`: INFO 级别日志
- `jacksparrow.error.log`: ERROR 级别日志

## 故障排查

### 常见问题

1. **连接失败**
   - 检查 Redis/ES 服务是否启动
   - 检查 `dataSource.json` 配置
   - 查看 `logs/jacksparrow.error.log`

2. **端口冲突**
   - 修改 `application.properties` 中的 `server.port`

3. **内存不足**
   - 调整 Docker Compose 中的 JVM 参数
   - 或修改 `JAVA_OPTS` 环境变量

## 技术栈

- Spring Boot 3.5.11
- Jedis 5.2.0 (Redis 客户端)
- Elasticsearch REST High Level Client 7.9.3
- FastJSON 2.0.43
- SpringDoc OpenAPI 2.3.0
- Lombok
- JUnit 5 + Mockito

## 许可证

MIT License
