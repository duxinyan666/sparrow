package com.weilin.controller;

import com.weilin.pojo.Result;
import com.weilin.service.DistributedLockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁控制器
 * 提供基于 Redis 的分布式锁操作 API
 */
@Slf4j
@Tag(name = "DistributedLock", description = "分布式锁操作接口")
@RestController
@RequestMapping("/api/lock")
public class DistributedLockController {
    
    @Autowired
    private DistributedLockService lockService;
    
    @Operation(summary = "尝试获取锁", description = "尝试获取分布式锁，立即返回结果")
    @PostMapping("/try")
    public Result tryLock(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey,
            @Parameter(description = "超时时间", required = true) @RequestParam long timeout,
            @Parameter(description = "时间单位", required = true) @RequestParam TimeUnit unit) {
        
        String value = UUID.randomUUID().toString();
        log.info("Lock TRY request: key={}, timeout={} {}, value={}", lockKey, timeout, unit, value);
        
        boolean success = lockService.tryLock(lockKey, value, timeout, unit);
        
        Map<String, Object> data = new HashMap<>();
        data.put("locked", success);
        data.put("lockKey", lockKey);
        data.put("value", success ? value : null);
        
        return Result.success(data, success ? "获取锁成功" : "获取锁失败（锁已被占用）");
    }
    
    @Operation(summary = "带重试获取锁", description = "带重试机制地获取分布式锁")
    @PostMapping("/try-retry")
    public Result tryLockWithRetry(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey,
            @Parameter(description = "超时时间", required = true) @RequestParam long timeout,
            @Parameter(description = "时间单位", required = true) @RequestParam TimeUnit unit,
            @Parameter(description = "重试次数", required = true) @RequestParam int retryTimes,
            @Parameter(description = "重试间隔 (ms)", required = true) @RequestParam long retryInterval) {
        
        String value = UUID.randomUUID().toString();
        log.info("Lock TRY_RETRY request: key={}, timeout={} {}, retries={}, interval={}ms",
                lockKey, timeout, unit, retryTimes, retryInterval);
        
        boolean success = lockService.tryLockWithRetry(lockKey, value, timeout, unit, retryTimes, retryInterval);
        
        Map<String, Object> data = new HashMap<>();
        data.put("locked", success);
        data.put("lockKey", lockKey);
        data.put("value", success ? value : null);
        
        return Result.success(data, success ? "获取锁成功" : "获取锁超时");
    }
    
    @Operation(summary = "释放锁", description = "释放分布式锁")
    @PostMapping("/unlock")
    public Result unlock(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey,
            @Parameter(description = "锁值", required = true) @RequestParam String value) {
        
        log.info("Lock UNLOCK request: key={}, value={}", lockKey, value);
        
        boolean success = lockService.unlock(lockKey, value);
        
        return Result.success(success, success ? "释放锁成功" : "释放锁失败（锁值不匹配或锁已过期）");
    }
    
    @Operation(summary = "检查锁状态", description = "检查锁是否被占用")
    @GetMapping("/status")
    public Result getLockStatus(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey) {
        
        log.info("Lock STATUS request: key={}", lockKey);
        
        boolean isLocked = lockService.isLocked(lockKey);
        long ttl = lockService.getLockTTL(lockKey);
        
        Map<String, Object> data = new HashMap<>();
        data.put("locked", isLocked);
        data.put("lockKey", lockKey);
        data.put("ttlSeconds", ttl);
        
        return Result.success(data, isLocked ? "锁被占用" : "锁空闲");
    }
    
    @Operation(summary = "续期锁", description = "延长分布式锁的过期时间")
    @PostMapping("/renew")
    public Result renewLock(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey,
            @Parameter(description = "锁值", required = true) @RequestParam String value,
            @Parameter(description = "新的超时时间", required = true) @RequestParam long timeout,
            @Parameter(description = "时间单位", required = true) @RequestParam TimeUnit unit) {
        
        log.info("Lock RENEW request: key={}, timeout={} {}, value={}", lockKey, timeout, unit, value);
        
        boolean success = lockService.renewLock(lockKey, value, timeout, unit);
        
        return Result.success(success, success ? "锁续期成功" : "锁续期失败（锁值不匹配）");
    }
    
    @Operation(summary = "获取锁剩余时间", description = "获取分布式锁的剩余过期时间")
    @GetMapping("/ttl")
    public Result getLockTTL(
            @Parameter(description = "锁键", required = true) @RequestParam String lockKey) {
        
        log.info("Lock TTL request: key={}", lockKey);
        
        long ttl = lockService.getLockTTL(lockKey);
        
        Map<String, Object> data = new HashMap<>();
        data.put("lockKey", lockKey);
        data.put("ttlSeconds", ttl);
        
        return Result.success(data, ttl > 0 ? "剩余 " + ttl + " 秒" : "锁不存在");
    }
}
