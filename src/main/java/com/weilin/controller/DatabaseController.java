package com.weilin.controller;

import com.weilin.pojo.Result;
import com.weilin.service.DatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据库控制器
 * 提供 MySQL/PostgreSQL 数据库操作的 RESTful API
 */
@Slf4j
@Tag(name = "Database", description = "数据库操作接口")
@RestController
@RequestMapping("/api/database")
public class DatabaseController {
    
    @Autowired
    private DatabaseService databaseService;
    
    @Operation(summary = "执行查询", description = "执行 SQL SELECT 查询")
    @PostMapping("/query")
    public Result query(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "SQL 语句", required = true) @RequestParam String sql,
            @RequestBody(required = false) List<Object> params) {
        log.info("Database QUERY request: source={}, sql={}", sourceName, sql);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        List<Map<String, Object>> result = databaseService.query(sourceName, sql, paramArray);
        
        return Result.success(result, "查询成功，返回 " + result.size() + " 行");
    }
    
    @Operation(summary = "执行更新", description = "执行 SQL UPDATE/DELETE 操作")
    @PostMapping("/update")
    public Result update(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "SQL 语句", required = true) @RequestParam String sql,
            @RequestBody(required = false) List<Object> params) {
        log.info("Database UPDATE request: source={}, sql={}", sourceName, sql);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        int rows = databaseService.update(sourceName, sql, paramArray);
        
        return Result.success(rows, "更新成功，影响 " + rows + " 行");
    }
    
    @Operation(summary = "执行插入", description = "执行 SQL INSERT 并返回生成的主键")
    @PostMapping("/insert")
    public Result insert(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "SQL 语句", required = true) @RequestParam String sql,
            @RequestBody(required = false) List<Object> params) {
        log.info("Database INSERT request: source={}, sql={}", sourceName, sql);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        Long key = databaseService.insertAndGetKey(sourceName, sql, paramArray);
        
        return Result.success(key, "插入成功，生成主键：" + key);
    }
    
    @Operation(summary = "批量执行", description = "批量执行 SQL 操作")
    @PostMapping("/batch")
    public Result batch(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "SQL 语句", required = true) @RequestParam String sql,
            @Parameter(description = "批量参数", required = true) @RequestBody List<List<Object>> batchParams) {
        log.info("Database BATCH request: source={}, sql={}, batches={}", sourceName, sql, batchParams.size());
        
        Object[][] params = batchParams.stream()
                .map(list -> list.toArray())
                .toArray(Object[][]::new);
        
        int[] results = databaseService.batchUpdate(sourceName, sql, java.util.Arrays.asList(params));
        
        return Result.success(results, "批量执行成功，共 " + results.length + " 批");
    }
    
    @Operation(summary = "检查表是否存在", description = "检查数据库表是否存在")
    @GetMapping("/table/exists")
    public Result tableExists(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "表名", required = true) @RequestParam String tableName) {
        log.info("Database TABLE_EXISTS request: source={}, table={}", sourceName, tableName);
        
        Boolean exists = databaseService.tableExists(sourceName, tableName);
        
        return Result.success(exists, exists ? "表存在" : "表不存在");
    }
    
    @Operation(summary = "获取表结构", description = "获取数据库表的列信息")
    @GetMapping("/table/columns")
    public Result getTableColumns(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName,
            @Parameter(description = "表名", required = true) @RequestParam String tableName) {
        log.info("Database TABLE_COLUMNS request: source={}, table={}", sourceName, tableName);
        
        List<Map<String, Object>> columns = databaseService.getTableColumns(sourceName, tableName);
        
        return Result.success(columns, "获取表结构成功，共 " + columns.size() + " 列");
    }
    
    @Operation(summary = "连接池状态", description = "获取数据库连接池监控指标")
    @GetMapping("/pool/status")
    public Result getPoolStatus(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName) {
        log.info("Database POOL_STATUS request: source={}", sourceName);
        
        Map<String, Object> status = databaseService.getPoolStatus(sourceName);
        
        return Result.success(status);
    }
    
    @Operation(summary = "测试连接", description = "测试数据库连接是否可用")
    @GetMapping("/test")
    public Result testConnection(
            @Parameter(description = "数据源名称", required = true) @RequestParam String sourceName) {
        log.info("Database TEST request: source={}", sourceName);
        
        Boolean success = databaseService.testConnection(sourceName);
        
        return Result.success(success, success ? "连接正常" : "连接失败");
    }
}
