package com.weilin.service;

import com.weilin.datasource.DataSourceManager;
import com.weilin.exception.DataSourceException;
import com.weilin.service.impl.RedisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Redis 服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class RedisServiceTest {
    
    @Mock
    private DataSourceManager dataSourceManager;
    
    @Mock
    private JedisPool jedisPool;
    
    @Mock
    private Jedis jedis;
    
    @InjectMocks
    private RedisServiceImpl redisService;
    
    private static final String SOURCE_NAME = "redisName";
    private static final String TEST_KEY = "test:key";
    private static final String TEST_VALUE = "test_value";
    
    @BeforeEach
    void setUp() {
        when(dataSourceManager.get(SOURCE_NAME)).thenReturn(jedisPool);
        when(jedisPool.getResource()).thenReturn(jedis);
    }
    
    @Test
    void testSet() {
        // Arrange
        when(jedis.set(TEST_KEY, TEST_VALUE)).thenReturn("OK");
        
        // Act
        String result = redisService.set(SOURCE_NAME, TEST_KEY, TEST_VALUE);
        
        // Assert
        assertEquals("OK", result);
        verify(jedis).set(TEST_KEY, TEST_VALUE);
        verify(jedis).close();
    }
    
    @Test
    void testGet() {
        // Arrange
        when(jedis.get(TEST_KEY)).thenReturn(TEST_VALUE);
        
        // Act
        String result = redisService.get(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(TEST_VALUE, result);
        verify(jedis).get(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testDel() {
        // Arrange
        when(jedis.del(TEST_KEY)).thenReturn(1L);
        
        // Act
        Long result = redisService.del(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(1L, result);
        verify(jedis).del(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testExists() {
        // Arrange
        when(jedis.exists(TEST_KEY)).thenReturn(true);
        
        // Act
        Boolean result = redisService.exists(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertTrue(result);
        verify(jedis).exists(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testSetEx() {
        // Arrange
        long ttl = 3600;
        when(jedis.setex(TEST_KEY, ttl, TEST_VALUE)).thenReturn("OK");
        
        // Act
        String result = redisService.setEx(SOURCE_NAME, TEST_KEY, TEST_VALUE, ttl);
        
        // Assert
        assertEquals("OK", result);
        verify(jedis).setex(TEST_KEY, ttl, TEST_VALUE);
        verify(jedis).close();
    }
    
    @Test
    void testIncr() {
        // Arrange
        when(jedis.incr(TEST_KEY)).thenReturn(1L);
        
        // Act
        Long result = redisService.incr(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(1L, result);
        verify(jedis).incr(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testDecr() {
        // Arrange
        when(jedis.decr(TEST_KEY)).thenReturn(0L);
        
        // Act
        Long result = redisService.decr(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(0L, result);
        verify(jedis).decr(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testExpire() {
        // Arrange
        long ttl = 3600;
        when(jedis.expire(TEST_KEY, ttl)).thenReturn(true);
        
        // Act
        Boolean result = redisService.expire(SOURCE_NAME, TEST_KEY, ttl);
        
        // Assert
        assertTrue(result);
        verify(jedis).expire(TEST_KEY, ttl);
        verify(jedis).close();
    }
    
    @Test
    void testTtl() {
        // Arrange
        when(jedis.ttl(TEST_KEY)).thenReturn(3600L);
        
        // Act
        Long result = redisService.ttl(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(3600L, result);
        verify(jedis).ttl(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testMget() {
        // Arrange
        String[] keys = {TEST_KEY, "test:key2"};
        List<String> values = List.of("value1", "value2");
        when(jedis.mget(keys)).thenReturn(values);
        
        // Act
        List<String> result = redisService.mget(SOURCE_NAME, keys);
        
        // Assert
        assertEquals(values, result);
        verify(jedis).mget(keys);
        verify(jedis).close();
    }
    
    @Test
    void testHset() {
        // Arrange
        String field = "field1";
        when(jedis.hset(TEST_KEY, field, TEST_VALUE)).thenReturn(1L);
        
        // Act
        Long result = redisService.hset(SOURCE_NAME, TEST_KEY, field, TEST_VALUE);
        
        // Assert
        assertEquals(1L, result);
        verify(jedis).hset(TEST_KEY, field, TEST_VALUE);
        verify(jedis).close();
    }
    
    @Test
    void testHget() {
        // Arrange
        String field = "field1";
        when(jedis.hget(TEST_KEY, field)).thenReturn(TEST_VALUE);
        
        // Act
        String result = redisService.hget(SOURCE_NAME, TEST_KEY, field);
        
        // Assert
        assertEquals(TEST_VALUE, result);
        verify(jedis).hget(TEST_KEY, field);
        verify(jedis).close();
    }
    
    @Test
    void testHgetAll() {
        // Arrange
        Map<String, String> map = Map.of("field1", "value1", "field2", "value2");
        when(jedis.hgetAll(TEST_KEY)).thenReturn(map);
        
        // Act
        Map<String, String> result = redisService.hgetAll(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(map, result);
        verify(jedis).hgetAll(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testLpush() {
        // Arrange
        String[] values = {"value1", "value2"};
        when(jedis.lpush(TEST_KEY, values)).thenReturn(2L);
        
        // Act
        Long result = redisService.lpush(SOURCE_NAME, TEST_KEY, values);
        
        // Assert
        assertEquals(2L, result);
        verify(jedis).lpush(TEST_KEY, values);
        verify(jedis).close();
    }
    
    @Test
    void testRpop() {
        // Arrange
        when(jedis.rpop(TEST_KEY)).thenReturn(TEST_VALUE);
        
        // Act
        String result = redisService.rpop(SOURCE_NAME, TEST_KEY);
        
        // Assert
        assertEquals(TEST_VALUE, result);
        verify(jedis).rpop(TEST_KEY);
        verify(jedis).close();
    }
    
    @Test
    void testLrange() {
        // Arrange
        List<String> list = List.of("value1", "value2", "value3");
        when(jedis.lrange(TEST_KEY, 0, -1)).thenReturn(list);
        
        // Act
        List<String> result = redisService.lrange(SOURCE_NAME, TEST_KEY, 0, -1);
        
        // Assert
        assertEquals(list, result);
        verify(jedis).lrange(TEST_KEY, 0, -1);
        verify(jedis).close();
    }
    
    @Test
    void testGetStatus() {
        // Arrange
        when(jedis.ping()).thenReturn("PONG");
        when(jedis.info("server")).thenReturn("# Server\nredis_version:7.0.0");
        when(jedis.getClient()).thenReturn(new redis.clients.jedis.Connection("localhost", 6379));
        
        // Act
        Map<String, Object> result = redisService.getStatus(SOURCE_NAME);
        
        // Assert
        assertTrue((Boolean) result.get("connected"));
        assertEquals(SOURCE_NAME, result.get("sourceName"));
        verify(jedis).ping();
        verify(jedis).close();
    }
    
    @Test
    void testDataSourceNotFound() {
        // Arrange
        when(dataSourceManager.get("invalid")).thenReturn(null);
        
        // Act & Assert
        assertThrows(DataSourceException.class, () -> {
            redisService.get("invalid", TEST_KEY);
        });
    }
}
