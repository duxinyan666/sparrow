package com.weilin.controller;

import com.weilin.pojo.Result;
import com.weilin.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Redis 控制器
 * 提供 Redis 数据操作的 RESTful API
 */
@Slf4j
@Tag(name = "Redis", description = "Redis 数据操作接口")
@RestController
@RequestMapping("/api/redis")
@Validated
public class RedisController {
    
    @Autowired
    private RedisService redisService;
    
    @Operation(summary = "设置字符串值", description = "设置 Redis 字符串键值对")
    @PutMapping("/set")
    public Result set(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key,
            @Parameter(description = "值", required = true) @RequestParam String value) {
        log.info("Redis SET request: source={}, key={}", sourceName, key);
        String result = redisService.set(sourceName, key, value);
        return Result.success(result, "设置成功");
    }
    
    @Operation(summary = "设置字符串值（带过期时间）", description = "设置 Redis 字符串键值对，指定过期时间")
    @PutMapping("/setex")
    public Result setEx(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key,
            @Parameter(description = "值", required = true) @RequestParam String value,
            @Parameter(description = "过期时间（秒）", required = true) @RequestParam long seconds) {
        log.info("Redis SETEX request: source={}, key={}, ttl={}s", sourceName, key, seconds);
        String result = redisService.setEx(sourceName, key, value, seconds);
        return Result.success(result, "设置成功");
    }
    
    @Operation(summary = "获取字符串值", description = "从 Redis 获取字符串值")
    @GetMapping("/get")
    public Result get(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key) {
        log.info("Redis GET request: source={}, key={}", sourceName, key);
        String value = redisService.get(sourceName, key);
        return Result.success(value);
    }
    
    @Operation(summary = "删除键", description = "删除一个或多个 Redis 键")
    @DeleteMapping("/del")
    public Result del(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键列表", required = true) @RequestParam String[] keys) {
        log.info("Redis DEL request: source={}, keys={}", sourceName, keys.length);
        Long count = redisService.del(sourceName, keys);
        return Result.success(count, "删除了 " + count + " 个键");
    }
    
    @Operation(summary = "检查键是否存在", description = "检查 Redis 键是否存在")
    @GetMapping("/exists")
    public Result exists(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key) {
        log.info("Redis EXISTS request: source={}, key={}", sourceName, key);
        Boolean exists = redisService.exists(sourceName, key);
        return Result.success(exists);
    }
    
    @Operation(summary = "设置过期时间", description = "为 Redis 键设置过期时间")
    @PostMapping("/expire")
    public Result expire(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key,
            @Parameter(description = "过期时间（秒）", required = true) @RequestParam long seconds) {
        log.info("Redis EXPIRE request: source={}, key={}, ttl={}s", sourceName, key, seconds);
        Boolean result = redisService.expire(sourceName, key, seconds);
        return Result.success(result, "设置成功");
    }
    
    @Operation(summary = "获取剩余过期时间", description = "获取 Redis 键的剩余过期时间")
    @GetMapping("/ttl")
    public Result ttl(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key) {
        log.info("Redis TTL request: source={}, key={}", sourceName, key);
        Long ttl = redisService.ttl(sourceName, key);
        return Result.success(ttl);
    }
    
    @Operation(summary = "自增", description = "Redis 键值自增 1")
    @PostMapping("/incr")
    public Result incr(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key) {
        log.info("Redis INCR request: source={}, key={}", sourceName, key);
        Long value = redisService.incr(sourceName, key);
        return Result.success(value);
    }
    
    @Operation(summary = "自减", description = "Redis 键值自减 1")
    @PostMapping("/decr")
    public Result decr(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键", required = true) @RequestParam String key) {
        log.info("Redis DECR request: source={}, key={}", sourceName, key);
        Long value = redisService.decr(sourceName, key);
        return Result.success(value);
    }
    
    @Operation(summary = "批量获取", description = "批量获取多个 Redis 键的值")
    @GetMapping("/mget")
    public Result mget(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "键列表", required = true) @RequestParam String[] keys) {
        log.info("Redis MGET request: source={}, keys={}", sourceName, keys.length);
        var values = redisService.mget(sourceName, keys);
        return Result.success(values);
    }
    
    @Operation(summary = "哈希设置", description = "设置 Redis 哈希字段")
    @PutMapping("/hset")
    public Result hset(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "哈希键", required = true) @RequestParam String key,
            @Parameter(description = "字段", required = true) @RequestParam String field,
            @Parameter(description = "值", required = true) @RequestParam String value) {
        log.info("Redis HSET request: source={}, key={}, field={}", sourceName, key, field);
        Long result = redisService.hset(sourceName, key, field, value);
        return Result.success(result, "设置成功");
    }
    
    @Operation(summary = "哈希获取", description = "获取 Redis 哈希字段值")
    @GetMapping("/hget")
    public Result hget(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "哈希键", required = true) @RequestParam String key,
            @Parameter(description = "字段", required = true) @RequestParam String field) {
        log.info("Redis HGET request: source={}, key={}, field={}", sourceName, key, field);
        String value = redisService.hget(sourceName, key, field);
        return Result.success(value);
    }
    
    @Operation(summary = "获取整个哈希", description = "获取 Redis 哈希表的所有字段")
    @GetMapping("/hgetAll")
    public Result hgetAll(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "哈希键", required = true) @RequestParam String key) {
        log.info("Redis HGETALL request: source={}, key={}", sourceName, key);
        Map<String, String> map = redisService.hgetAll(sourceName, key);
        return Result.success(map);
    }
    
    @Operation(summary = "列表左侧推送", description = "向 Redis 列表左侧添加元素")
    @PostMapping("/lpush")
    public Result lpush(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "列表键", required = true) @RequestParam String key,
            @Parameter(description = "值列表", required = true) @RequestParam String[] values) {
        log.info("Redis LPUSH request: source={}, key={}, values={}", sourceName, key, values.length);
        Long length = redisService.lpush(sourceName, key, values);
        return Result.success(length, "当前列表长度：" + length);
    }
    
    @Operation(summary = "列表右侧弹出", description = "从 Redis 列表右侧弹出元素")
    @PostMapping("/rpop")
    public Result rpop(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "列表键", required = true) @RequestParam String key) {
        log.info("Redis RPOP request: source={}, key={}", sourceName, key);
        String value = redisService.rpop(sourceName, key);
        return Result.success(value);
    }
    
    @Operation(summary = "获取列表范围", description = "获取 Redis 列表指定范围的元素")
    @GetMapping("/lrange")
    public Result lrange(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "列表键", required = true) @RequestParam String key,
            @Parameter(description = "起始索引", required = true) @RequestParam long start,
            @Parameter(description = "结束索引", required = true) @RequestParam long end) {
        log.info("Redis LRANGE request: source={}, key={}, range={}-{}", sourceName, key, start, end);
        var list = redisService.lrange(sourceName, key, start, end);
        return Result.success(list);
    }
    
    @Operation(summary = "健康检查", description = "检查 Redis 连接状态")
    @GetMapping("/status")
    public Result status(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName) {
        log.info("Redis status check: source={}", sourceName);
        var status = redisService.getStatus(sourceName);
        return Result.success(status);
    }
}
