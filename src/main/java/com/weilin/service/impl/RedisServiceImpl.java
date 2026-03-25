package com.weilin.service.impl;

import com.weilin.datasource.DataSourceManager;
import com.weilin.exception.ConnectionException;
import com.weilin.exception.DataSourceException;
import com.weilin.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Redis 服务实现
 * 提供完整的 Redis 操作功能
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {
    
    @Autowired
    private DataSourceManager dataSourceManager;
    
    /**
     * 获取 Jedis 连接（带资源管理）
     */
    private Jedis getJedis(String sourceName) {
        try {
            JedisPool jedisPool = dataSourceManager.get(sourceName);
            if (jedisPool == null) {
                throw new DataSourceException("数据源不存在：" + sourceName, 404);
            }
            return jedisPool.getResource();
        } catch (Exception e) {
            log.error("获取 Redis 连接失败：{}", e.getMessage());
            throw new ConnectionException("无法连接到 Redis: " + e.getMessage());
        }
    }
    
    @Override
    public String set(String sourceName, String key, String value) {
        try (Jedis jedis = getJedis(sourceName)) {
            String result = jedis.set(key, value);
            log.debug("Redis SET: {} = {}", key, value);
            return result;
        }
    }
    
    @Override
    public String setEx(String sourceName, String key, String value, long seconds) {
        try (Jedis jedis = getJedis(sourceName)) {
            String result = jedis.setex(key, seconds, value);
            log.debug("Redis SETEX: {} = {} ({}s)", key, value, seconds);
            return result;
        }
    }
    
    @Override
    public String get(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            String value = jedis.get(key);
            log.debug("Redis GET: {} = {}", key, value);
            return value;
        }
    }
    
    @Override
    public Long del(String sourceName, String... keys) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long count = jedis.del(keys);
            log.debug("Redis DEL: {} keys deleted", count);
            return count;
        }
    }
    
    @Override
    public Boolean exists(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            Boolean exists = jedis.exists(key);
            log.debug("Redis EXISTS: {} = {}", key, exists);
            return exists;
        }
    }
    
    @Override
    public Boolean expire(String sourceName, String key, long seconds) {
        try (Jedis jedis = getJedis(sourceName)) {
            Boolean result = jedis.expire(key, seconds);
            log.debug("Redis EXPIRE: {} = {}s", key, seconds);
            return result;
        }
    }
    
    @Override
    public Long ttl(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long ttl = jedis.ttl(key);
            log.debug("Redis TTL: {} = {}s", key, ttl);
            return ttl;
        }
    }
    
    @Override
    public Long incr(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long value = jedis.incr(key);
            log.debug("Redis INCR: {} = {}", key, value);
            return value;
        }
    }
    
    @Override
    public Long decr(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long value = jedis.decr(key);
            log.debug("Redis DECR: {} = {}", key, value);
            return value;
        }
    }
    
    @Override
    public String mset(String sourceName, Map<String, String> map) {
        try (Jedis jedis = getJedis(sourceName)) {
            String result = jedis.mset(map);
            log.debug("Redis MSET: {} keys", map.size());
            return result;
        }
    }
    
    @Override
    public List<String> mget(String sourceName, String... keys) {
        try (Jedis jedis = getJedis(sourceName)) {
            List<String> values = jedis.mget(keys);
            log.debug("Redis MGET: {} keys", keys.length);
            return values;
        }
    }
    
    @Override
    public Long hset(String sourceName, String key, String field, String value) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long result = jedis.hset(key, field, value);
            log.debug("Redis HSET: {}:{} = {}", key, field, value);
            return result;
        }
    }
    
    @Override
    public String hget(String sourceName, String key, String field) {
        try (Jedis jedis = getJedis(sourceName)) {
            String value = jedis.hget(key, field);
            log.debug("Redis HGET: {}:{} = {}", key, field, value);
            return value;
        }
    }
    
    @Override
    public Map<String, String> hgetAll(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            Map<String, String> map = jedis.hgetAll(key);
            log.debug("Redis HGETALL: {} ({} fields)", key, map.size());
            return map;
        }
    }
    
    @Override
    public Long lpush(String sourceName, String key, String... values) {
        try (Jedis jedis = getJedis(sourceName)) {
            Long length = jedis.lpush(key, values);
            log.debug("Redis LPUSH: {} ({} items)", key, values.length);
            return length;
        }
    }
    
    @Override
    public String rpop(String sourceName, String key) {
        try (Jedis jedis = getJedis(sourceName)) {
            String value = jedis.rpop(key);
            log.debug("Redis RPOP: {} = {}", key, value);
            return value;
        }
    }
    
    @Override
    public List<String> lrange(String sourceName, String key, long start, long end) {
        try (Jedis jedis = getJedis(sourceName)) {
            List<String> list = jedis.lrange(key, start, end);
            log.debug("Redis LRANGE: {} ({}-{})", key, start, end);
            return list;
        }
    }
    
    @Override
    public Map<String, Object> getStatus(String sourceName) {
        Map<String, Object> status = new HashMap<>();
        try (Jedis jedis = getJedis(sourceName)) {
            String ping = jedis.ping();
            String info = jedis.info("server");
            
            status.put("connected", "PONG".equals(ping));
            status.put("sourceName", sourceName);
            status.put("host", jedis.getClient().getHost());
            status.put("port", jedis.getClient().getPort());
            status.put("timestamp", new Date().getTime());
            
            log.debug("Redis status check: {}", status);
        } catch (Exception e) {
            status.put("connected", false);
            status.put("error", e.getMessage());
        }
        return status;
    }
}
