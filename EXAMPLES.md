# JackSparrow 使用示例

本文件提供常用场景的代码示例，帮助你快速上手。

---

## 📋 目录

1. [Redis 操作示例](#redis-操作示例)
2. [MySQL 操作示例](#mysql-操作示例)
3. [PostgreSQL 操作示例](#postgresql-操作示例)
4. [分布式锁示例](#分布式锁示例)
5. [连接池监控](#连接池监控)

---

## Redis 操作示例

### 设置缓存

```bash
# 设置字符串值
curl -X PUT "http://localhost:8080/api/redis/set?sourceName=redisName&key=user:1001&value=张三"

# 设置带过期时间的值（1 小时）
curl -X PUT "http://localhost:8080/api/redis/setex?sourceName=redisName&key=token:abc123&value=xyz&seconds=3600"
```

### 获取缓存

```bash
curl "http://localhost:8080/api/redis/get?sourceName=redisName&key=user:1001"
```

### 哈希操作

```bash
# 设置哈希字段
curl -X PUT "http://localhost:8080/api/redis/hset?sourceName=redisName&key=user:1001&field=name&value=张三"
curl -X PUT "http://localhost:8080/api/redis/hset?sourceName=redisName&key=user:1001&field=age&value=25"

# 获取单个字段
curl "http://localhost:8080/api/redis/hget?sourceName=redisName&key=user:1001&field=name"

# 获取整个哈希
curl "http://localhost:8080/api/redis/hgetAll?sourceName=redisName&key=user:1001"
```

### 计数器

```bash
# 自增（访问计数）
curl -X POST "http://localhost:8080/api/redis/incr?sourceName=redisName&key=page:home:views"

# 自减
curl -X POST "http://localhost:8080/api/redis/decr?sourceName=redisName&key=inventory:product:1001"
```

---

## MySQL 操作示例

### 查询数据

```bash
# 简单查询
curl -X POST "http://localhost:8080/api/database/query?sourceName=mysqlName&sql=SELECT * FROM users WHERE id = ?" \
  -H "Content-Type: application/json" \
  -d '[1]'

# 多条件查询
curl -X POST "http://localhost:8080/api/database/query?sourceName=mysqlName&sql=SELECT * FROM orders WHERE status = ? AND user_id = ?" \
  -H "Content-Type: application/json" \
  -d '[1, 1]'
```

### 插入数据

```bash
curl -X POST "http://localhost:8080/api/database/insert?sourceName=mysqlName&sql=INSERT INTO users (username, email) VALUES (?, ?)" \
  -H "Content-Type: application/json" \
  -d '["david", "david@example.com"]'
```

### 更新数据

```bash
curl -X POST "http://localhost:8080/api/database/update?sourceName=mysqlName&sql=UPDATE users SET email = ? WHERE id = ?" \
  -H "Content-Type: application/json" \
  -d '["newemail@example.com", 1]'
```

### 删除数据

```bash
curl -X POST "http://localhost:8080/api/database/update?sourceName=mysqlName&sql=DELETE FROM orders WHERE id = ?" \
  -H "Content-Type: application/json" \
  -d '[1]'
```

### 批量操作

```bash
curl -X POST "http://localhost:8080/api/database/batch?sourceName=mysqlName&sql=INSERT INTO users (username, email) VALUES (?, ?)" \
  -H "Content-Type: application/json" \
  -d '[
    ["user1", "user1@example.com"],
    ["user2", "user2@example.com"],
    ["user3", "user3@example.com"]
  ]'
```

### 查看表结构

```bash
curl "http://localhost:8080/api/database/table/columns?sourceName=mysqlName&tableName=users"
```

### 检查表是否存在

```bash
curl "http://localhost:8080/api/database/table/exists?sourceName=mysqlName&tableName=users"
```

---

## PostgreSQL 操作示例

PostgreSQL 操作与 MySQL 类似，只需更改 `sourceName`：

```bash
# 查询
curl -X POST "http://localhost:8080/api/database/query?sourceName=pgName&sql=SELECT * FROM users WHERE id = $1" \
  -H "Content-Type: application/json" \
  -d '[1]'

# 插入
curl -X POST "http://localhost:8080/api/database/insert?sourceName=pgName&sql=INSERT INTO users (username, email) VALUES ($1, $2) RETURNING id" \
  -H "Content-Type: application/json" \
  -d '["pg_user", "pg@example.com"]'
```

---

## 分布式锁示例

### 获取锁

```bash
# 尝试获取锁（立即返回）
curl -X POST "http://localhost:8080/api/lock/try?lockKey=order:1001&timeout=30&unit=SECONDS" \
  -H "Content-Type: application/json"

# 带重试获取锁
curl -X POST "http://localhost:8080/api/lock/try-retry?lockKey=order:1001&timeout=30&unit=SECONDS&retryTimes=5&retryInterval=1000" \
  -H "Content-Type: application/json"
```

### 检查锁状态

```bash
curl "http://localhost:8080/api/lock/status?lockKey=order:1001"
```

### 释放锁

```bash
curl -X POST "http://localhost:8080/api/lock/unlock?lockKey=order:1001&value=<锁返回的 value>" \
  -H "Content-Type: application/json"
```

### 续期锁

```bash
curl -X POST "http://localhost:8080/api/lock/renew?lockKey=order:1001&value=<锁返回的 value>&timeout=60&unit=SECONDS" \
  -H "Content-Type: application/json"
```

### 获取锁剩余时间

```bash
curl "http://localhost:8080/api/lock/ttl?lockKey=order:1001"
```

---

## 连接池监控

### 查看 Redis 连接池状态

```bash
curl "http://localhost:8080/api/redis/status?sourceName=redisName"
```

### 查看数据库连接池状态

```bash
curl "http://localhost:8080/api/database/pool/status?sourceName=mysqlName"
```

### Prometheus 指标

访问：http://localhost:9090

```promql
# 活跃连接数
hikaricp_active_connections

# 空闲连接数
hikaricp_idle_connections

# 总连接数
hikaricp_connections

# 等待连接的线程数
hikaricp_threads_awaiting_connection
```

### Grafana 仪表盘

访问：http://localhost:3000 (admin/admin123)

1. 添加 Prometheus 数据源 (http://prometheus:9090)
2. 导入 Dashboard 或自定义面板

---

## 健康检查

```bash
# 系统健康
curl "http://localhost:8080/api/health"

# 数据源状态
curl "http://localhost:8080/api/health/datasources"

# 详细信息
curl "http://localhost:8080/api/health/info"
```

---

## Java 代码示例

### Redis 操作

```java
@Autowired
private RedisService redisService;

// 设置缓存
redisService.set("redisName", "user:1001", "张三");

// 获取缓存
String user = redisService.get("redisName", "user:1001");

// 设置过期时间
redisService.setEx("redisName", "token:abc", "xyz", 3600);

// 哈希操作
redisService.hset("redisName", "user:1001", "name", "张三");
Map<String, String> userMap = redisService.hgetAll("redisName", "user:1001");
```

### 数据库操作

```java
@Autowired
private DatabaseService databaseService;

// 查询
List<Map<String, Object>> users = databaseService.query(
    "mysqlName", 
    "SELECT * FROM users WHERE id = ?", 
    1
);

// 插入
Long userId = databaseService.insertAndGetKey(
    "mysqlName", 
    "INSERT INTO users (username, email) VALUES (?, ?)", 
    "newuser", "newuser@example.com"
);

// 更新
int rows = databaseService.update(
    "mysqlName", 
    "UPDATE users SET email = ? WHERE id = ?", 
    "newemail@example.com", 1
);
```

### 分布式锁

```java
@Autowired
private DistributedLockService lockService;

String lockKey = "order:1001";
String value = UUID.randomUUID().toString();

// 尝试获取锁
if (lockService.tryLock(lockKey, value, 30, TimeUnit.SECONDS)) {
    try {
        // 执行业务逻辑
        processOrder();
    } finally {
        // 释放锁
        lockService.unlock(lockKey, value);
    }
}

// 带重试的获取锁
if (lockService.tryLockWithRetry(lockKey, value, 30, TimeUnit.SECONDS, 5, 1000)) {
    try {
        processOrder();
    } finally {
        lockService.unlock(lockKey, value);
    }
}
```

---

## 常见问题

### 1. 连接失败

检查数据源配置：
```bash
curl "http://localhost:8080/api/health/datasources"
```

### 2. 锁无法释放

确保使用获取锁时返回的 `value` 来释放锁。

### 3. SQL 语法错误

MySQL 和 PostgreSQL 的占位符不同：
- MySQL: `?`
- PostgreSQL: `$1`, `$2`, `$3`...

---

更多示例请参考 Swagger 文档：http://localhost:8080/swagger-ui.html
