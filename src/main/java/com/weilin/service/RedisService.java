package com.weilin.service;

import java.util.List;
import java.util.Map;

/**
 * Redis 服务接口
 * 提供统一的 Redis 操作抽象
 */
public interface RedisService {
    
    /**
     * 设置字符串值
     * @param sourceName 数据源名称
     * @param key 键
     * @param value 值
     * @return 操作结果
     */
    String set(String sourceName, String key, String value);
    
    /**
     * 设置字符串值（带过期时间）
     * @param sourceName 数据源名称
     * @param key 键
     * @param value 值
     * @param seconds 过期时间（秒）
     * @return 操作结果
     */
    String setEx(String sourceName, String key, String value, long seconds);
    
    /**
     * 获取字符串值
     * @param sourceName 数据源名称
     * @param key 键
     * @return 值
     */
    String get(String sourceName, String key);
    
    /**
     * 删除键
     * @param sourceName 数据源名称
     * @param keys 键列表
     * @return 删除的数量
     */
    Long del(String sourceName, String... keys);
    
    /**
     * 检查键是否存在
     * @param sourceName 数据源名称
     * @param key 键
     * @return 是否存在
     */
    Boolean exists(String sourceName, String key);
    
    /**
     * 设置过期时间
     * @param sourceName 数据源名称
     * @param key 键
     * @param seconds 过期时间（秒）
     * @return 是否设置成功
     */
    Boolean expire(String sourceName, String key, long seconds);
    
    /**
     * 获取剩余过期时间
     * @param sourceName 数据源名称
     * @param key 键
     * @return 剩余时间（秒），-1 表示永久，-2 表示已过期
     */
    Long ttl(String sourceName, String key);
    
    /**
     * 自增
     * @param sourceName 数据源名称
     * @param key 键
     * @return 自增后的值
     */
    Long incr(String sourceName, String key);
    
    /**
     * 自减
     * @param sourceName 数据源名称
     * @param key 键
     * @return 自减后的值
     */
    Long decr(String sourceName, String key);
    
    /**
     * 批量设置
     * @param sourceName 数据源名称
     * @param map 键值对
     * @return 操作结果
     */
    String mset(String sourceName, Map<String, String> map);
    
    /**
     * 批量获取
     * @param sourceName 数据源名称
     * @param keys 键列表
     * @return 值列表
     */
    List<String> mget(String sourceName, String... keys);
    
    /**
     * 设置哈希字段
     * @param sourceName 数据源名称
     * @param key 哈希键
     * @param field 字段
     * @param value 值
     * @return 操作结果
     */
    Long hset(String sourceName, String key, String field, String value);
    
    /**
     * 获取哈希字段
     * @param sourceName 数据源名称
     * @param key 哈希键
     * @param field 字段
     * @return 字段值
     */
    String hget(String sourceName, String key, String field);
    
    /**
     * 获取整个哈希
     * @param sourceName 数据源名称
     * @param key 哈希键
     * @return 哈希表
     */
    Map<String, String> hgetAll(String sourceName, String key);
    
    /**
     * 添加到列表左侧
     * @param sourceName 数据源名称
     * @param key 列表键
     * @param values 值列表
     * @return 列表长度
     */
    Long lpush(String sourceName, String key, String... values);
    
    /**
     * 从列表右侧弹出
     * @param sourceName 数据源名称
     * @param key 列表键
     * @return 弹出的值
     */
    String rpop(String sourceName, String key);
    
    /**
     * 获取列表范围
     * @param sourceName 数据源名称
     * @param key 列表键
     * @param start 起始索引
     * @param end 结束索引
     * @return 列表片段
     */
    List<String> lrange(String sourceName, String key, long start, long end);
    
    /**
     * 获取 Redis 连接状态
     * @param sourceName 数据源名称
     * @return 状态信息
     */
    Map<String, Object> getStatus(String sourceName);
}
