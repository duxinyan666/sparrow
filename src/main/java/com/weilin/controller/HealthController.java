package com.weilin.controller;

import com.weilin.datasource.DataSourceManager;
import com.weilin.pojo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 提供系统健康状态和数据源状态检查
 */
@Slf4j
@Tag(name = "Health", description = "健康检查接口")
@RestController
@RequestMapping("/api/health")
public class HealthController {
    
    @Autowired(required = false)
    private BuildProperties buildProperties;
    
    @Autowired
    private DataSourceManager dataSourceManager;
    
    @Operation(summary = "系统健康检查", description = "检查系统整体健康状态")
    @GetMapping
    public Result health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        
        if (buildProperties != null) {
            health.put("version", buildProperties.getVersion());
            health.put("name", buildProperties.getName());
        }
        
        return Result.success(health);
    }
    
    @Operation(summary = "数据源健康检查", description = "检查所有配置的数据源连接状态")
    @GetMapping("/datasources")
    public Result checkDataSources() {
        Map<String, Object> status = new HashMap<>();
        
        // 检查 Redis
        try {
            var redisStatus = dataSourceManager.get("redisName");
            if (redisStatus != null) {
                status.put("redis", "UP");
            } else {
                status.put("redis", "DOWN");
            }
        } catch (Exception e) {
            status.put("redis", "DOWN");
            status.put("redisError", e.getMessage());
        }
        
        // 检查 Elasticsearch
        try {
            var esStatus = dataSourceManager.get("elasticName");
            if (esStatus != null) {
                status.put("elasticsearch", "UP");
            } else {
                status.put("elasticsearch", "DOWN");
            }
        } catch (Exception e) {
            status.put("elasticsearch", "DOWN");
            status.put("esError", e.getMessage());
        }
        
        return Result.success(status);
    }
    
    @Operation(summary = "详细信息", description = "获取系统详细信息")
    @GetMapping("/info")
    public Result info() {
        Map<String, Object> info = new HashMap<>();
        
        // JVM 信息
        Map<String, String> jvm = new HashMap<>();
        jvm.put("version", System.getProperty("java.version"));
        jvm.put("vendor", System.getProperty("java.vendor"));
        jvm.put("home", System.getProperty("java.home"));
        info.put("jvm", jvm);
        
        // 系统信息
        Map<String, String> system = new HashMap<>();
        system.put("os", System.getProperty("os.name"));
        system.put("arch", System.getProperty("os.arch"));
        system.put("version", System.getProperty("os.version"));
        info.put("system", system);
        
        // 内存信息
        Runtime runtime = Runtime.getRuntime();
        Map<String, Long> memory = new HashMap<>();
        memory.put("total", runtime.totalMemory());
        memory.put("free", runtime.freeMemory());
        memory.put("max", runtime.maxMemory());
        memory.put("used", runtime.totalMemory() - runtime.freeMemory());
        info.put("memory", memory);
        
        // 应用信息
        if (buildProperties != null) {
            Map<String, String> app = new HashMap<>();
            app.put("name", buildProperties.getName());
            app.put("version", buildProperties.getVersion());
            info.put("application", app);
        }
        
        return Result.success(info);
    }
}
