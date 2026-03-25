# 动态多数据源使用指南

## 📖 概述

JackSparrow 提供动态多数据源功能，支持：

- ✅ 运行时动态注册/注销数据源
- ✅ 通过参数控制查询不同 IP 机器的数据库
- ✅ 统一的 CRUD 接口
- ✅ MyBatis Plus 集成
- ✅ 连接池自动管理

---

## 🚀 快速开始

### 1. 注册数据源

```bash
POST http://localhost:8080/api/datasource/register
Content-Type: application/json

{
  "type": "mysql",
  "host": "192.168.1.100",
  "port": 3306,
  "database": "test_db",
  "username": "root",
  "password": "password"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "数据源注册成功：mysql:192.168.1.100:3306:test_db",
  "data": "mysql:192.168.1.100:3306:test_db"
}
```

### 2. 查询数据

```bash
POST http://localhost:8080/api/crud/query?dataSource=mysql:192.168.1.100:3306:test_db&sql=SELECT * FROM users WHERE id = ?
Content-Type: application/json

[1]
```

### 3. 一步完成（快速查询）

```bash
POST http://localhost:8080/api/crud/quick-query?type=mysql&host=192.168.1.100&port=3306&database=test_db&username=root&password=password&sql=SELECT * FROM users
```

---

## 📡 API 接口

### 数据源管理

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/datasource/register` | POST | 注册数据源 |
| `/api/datasource/unregister/{key}` | DELETE | 注销数据源 |
| `/api/datasource/list` | GET | 获取数据源列表 |
| `/api/datasource/{key}` | GET | 获取数据源详情 |
| `/api/datasource/{key}/test` | GET | 测试连接 |

### CRUD 操作

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/crud/query` | POST | 通用查询 |
| `/api/crud/insert` | POST | 通用插入 |
| `/api/crud/update` | POST | 通用更新 |
| `/api/crud/delete` | POST | 通用删除 |
| `/api/crud/page` | POST | 分页查询 |
| `/api/crud/tables` | GET | 获取表列表 |
| `/api/crud/table/columns` | GET | 获取表结构 |
| `/api/crud/quick-query` | POST | 快速查询（注册 + 查询） |

---

## 💡 使用示例

### 示例 1：查询不同 IP 的数据库

```bash
# 查询北京机器的数据库
curl -X POST "http://localhost:8080/api/crud/query?dataSource=mysql:192.168.1.100:3306:users&sql=SELECT * FROM orders WHERE status = ?" \
  -H "Content-Type: application/json" \
  -d '[1]'

# 查询上海机器的数据库
curl -X POST "http://localhost:8080/api/crud/query?dataSource=mysql:192.168.2.100:3306:users&sql=SELECT * FROM orders WHERE status = ?" \
  -H "Content-Type: application/json" \
  -d '[1]'
```

### 示例 2：批量操作

```bash
# 在北京机器插入数据
curl -X POST "http://localhost:8080/api/crud/insert?dataSource=mysql:192.168.1.100:3306:users&sql=INSERT INTO users (username, email) VALUES (?, ?)" \
  -H "Content-Type: application/json" \
  -d '["alice", "alice@example.com"]'

# 在上海机器插入数据
curl -X POST "http://localhost:8080/api/crud/insert?dataSource=mysql:192.168.2.100:3306:users&sql=INSERT INTO users (username, email) VALUES (?, ?)" \
  -H "Content-Type: application/json" \
  -d '["bob", "bob@example.com"]'
```

### 示例 3：分页查询

```bash
curl -X POST "http://localhost:8080/api/crud/page?dataSource=mysql:192.168.1.100:3306:users&sql=SELECT * FROM orders&pageNum=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -d '[]'
```

### 示例 4：更新操作

```bash
curl -X POST "http://localhost:8080/api/crud/update?dataSource=mysql:192.168.1.100:3306:users&sql=UPDATE users SET email = ? WHERE id = ?" \
  -H "Content-Type: application/json" \
  -d '["newemail@example.com", 1]'
```

### 示例 5：删除操作

```bash
curl -X POST "http://localhost:8080/api/crud/delete?dataSource=mysql:192.168.1.100:3306:users&sql=DELETE FROM users WHERE id = ?" \
  -H "Content-Type: application/json" \
  -d '[1]'
```

---

## 🔧 在其他项目中集成

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.github.duxinyan666</groupId>
    <artifactId>sparrow</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置数据源

```yaml
sparrow:
  datasource:
    enabled: true
    default:
      type: mysql
      host: localhost
      port: 3306
      database: test_db
      username: root
      password: password
```

### 3. 使用示例

```java
@Autowired
private DynamicDataSourceService dataSourceService;

// 注册数据源
DataSourceConfigDTO config = new DataSourceConfigDTO();
config.setType("mysql");
config.setHost("192.168.1.100");
config.setPort(3306);
config.setDatabase("test_db");
config.setUsername("root");
config.setPassword("password");

String key = dataSourceService.registerDataSource(config);

// 执行查询
List<Map<String, Object>> result = dataSourceService.executeQuery(
    key, 
    "SELECT * FROM users WHERE id = ?", 
    1
);

// 分页查询
Page<Map<String, Object>> page = dataSourceService.executeQueryByPage(
    key, 
    "SELECT * FROM orders", 
    1, 
    10
);
```

---

## 📝 数据源键名格式

数据源键名格式：`type:host:port:database`

示例：
- `mysql:192.168.1.100:3306:test_db`
- `postgresql:192.168.2.100:5432:users`

---

## ⚠️ 注意事项

1. **连接池管理**
   - 每个数据源独立连接池
   - 默认最大连接数：10
   - 注销数据源会自动关闭连接池

2. **线程安全**
   - 使用 ThreadLocal 管理数据源切换
   - 请求结束后自动清除

3. **性能优化**
   - 频繁访问的数据源建议预先注册
   - 使用连接池复用连接

4. **安全建议**
   - 生产环境使用环境变量管理密码
   - 限制 SQL 注入风险（使用参数化查询）

---

## 🔍 监控

### 查看数据源状态

```bash
curl http://localhost:8080/api/datasource/list
```

### 查看连接池指标

```bash
curl http://localhost:8080/actuator/metrics/hikaricp.connections
```

---

## 🐛 故障排查

### 问题 1：数据源不存在

**错误：** `数据源不存在：mysql:192.168.1.100:3306:test_db`

**解决：** 先注册数据源

### 问题 2：连接失败

**错误：** `无法连接到数据库`

**解决：**
1. 检查网络连通性
2. 检查数据库服务是否运行
3. 检查用户名密码是否正确

### 问题 3：SQL 语法错误

**错误：** `SQL 语法错误`

**解决：**
- MySQL 使用 `?` 作为占位符
- PostgreSQL 使用 `$1`, `$2` 作为占位符

---

更多示例请参考 Swagger 文档：http://localhost:8080/swagger-ui.html
