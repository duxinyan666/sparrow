package com.weilin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分布式锁服务
 * 基于 Redis 实现简单的分布式锁
 */
@Slf4j
@Service
public class DistributedLockService {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * 本地锁（用于 Redis 操作的互斥）
     */
    private final Lock localLock = new ReentrantLock();
    
    /**
     * 尝试获取锁
     * @param lockKey 锁键
     * @param value 锁值（通常是 UUID 或请求 ID）
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String value, long timeout, TimeUnit unit) {
        localLock.lock();
        try {
            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, value, timeout, unit);
            
            if (success != null && success) {
                log.debug("分布式锁获取成功：{}", lockKey);
                return true;
            }
            
            log.debug("分布式锁获取失败：{} (已被占用)", lockKey);
            return false;
        } finally {
            localLock.unlock();
        }
    }
    
    /**
     * 释放锁
     * @param lockKey 锁键
     * @param value 锁值（必须是获取锁时的值）
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String value) {
        localLock.lock();
        try {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            
            // 防止误删其他线程的锁
            if (value.equals(currentValue)) {
                redisTemplate.delete(lockKey);
                log.debug("分布式锁释放成功：{}", lockKey);
                return true;
            }
            
            log.warn("分布式锁释放失败：{} (锁值不匹配)", lockKey);
            return false;
        } finally {
            localLock.unlock();
        }
    }
    
    /**
     * 带重试的获取锁
     * @param lockKey 锁键
     * @param value 锁值
     * @param timeout 锁超时时间
     * @param unit 时间单位
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @return 是否获取成功
     */
    public boolean tryLockWithRetry(String lockKey, String value, long timeout, TimeUnit unit,
                                     int retryTimes, long retryInterval) {
        for (int i = 0; i < retryTimes; i++) {
            if (tryLock(lockKey, value, timeout, unit)) {
                return true;
            }
            
            try {
                log.debug("等待锁重试：{} ({}/{})", lockKey, i + 1, retryTimes);
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("等待锁被中断：{}", lockKey);
                return false;
            }
        }
        
        log.warn("获取锁超时：{} (重试 {} 次)", lockKey, retryTimes);
        return false;
    }
    
    /**
     * 检查锁是否存在
     * @param lockKey 锁键
     * @return 是否存在
     */
    public boolean isLocked(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }
    
    /**
     * 获取锁的剩余时间
     * @param lockKey 锁键
     * @return 剩余时间（秒），-1 表示不存在或永久
     */
    public long getLockTTL(String lockKey) {
        Long ttl = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        return ttl != null ? ttl : -1;
    }
    
    /**
     * 续期锁
     * @param lockKey 锁键
     * @param value 锁值
     * @param timeout 新的超时时间
     * @param unit 时间单位
     * @return 是否续期成功
     */
    public boolean renewLock(String lockKey, String value, long timeout, TimeUnit unit) {
        localLock.lock();
        try {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            
            if (value.equals(currentValue)) {
                redisTemplate.expire(lockKey, timeout, unit);
                log.debug("分布式锁续期成功：{}", lockKey);
                return true;
            }
            
            log.warn("分布式锁续期失败：{} (锁值不匹配)", lockKey);
            return false;
        } finally {
            localLock.unlock();
        }
    }
}
