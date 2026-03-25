package com.weilin.controller;

import com.weilin.datasource.dto.DataSourceConfigDTO;
import com.weilin.pojo.Result;
import com.weilin.service.DynamicDataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通用 CRUD 控制器
 * 通过参数控制查询不同 IP 机器的数据库
 */
@Slf4j
@Tag(name = "UniversalCRUD", description = "通用 CRUD 操作接口")
@RestController
@RequestMapping("/api/crud")
public class UniversalCrudController {
    
    @Autowired
    private DynamicDataSourceService dataSourceService;
    
    @Operation(summary = "通用查询", description = "在指定数据源上执行查询，支持任意 SQL")
    @PostMapping("/query")
    public Result query(
            @Parameter(description = "数据源键名（格式：type:host:port:database）", required = true, example = "mysql:192.168.1.100:3306:test_db") 
            @RequestParam String dataSource,
            @Parameter(description = "SQL 查询语句", required = true, example = "SELECT * FROM users WHERE id = ?") 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("通用查询 - 数据源：{}, SQL: {}, 参数：{}", dataSource, sql, params);
        
        try {
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            List<Map<String, Object>> result = dataSourceService.executeQuery(dataSource, sql, paramArray);
            
            return Result.success(result, "查询成功，返回 " + result.size() + " 行");
        } catch (Exception e) {
            log.error("查询失败：{}", e.getMessage(), e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "通用插入", description = "在指定数据源上执行插入")
    @PostMapping("/insert")
    public Result insert(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource,
            @Parameter(description = "SQL 插入语句", required = true, example = "INSERT INTO users (username, email) VALUES (?, ?)") 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("通用插入 - 数据源：{}, SQL: {}, 参数：{}", dataSource, sql, params);
        
        try {
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            int rows = dataSourceService.executeUpdate(dataSource, sql, paramArray);
            
            return Result.success(rows, "插入成功，影响 " + rows + " 行");
        } catch (Exception e) {
            log.error("插入失败：{}", e.getMessage(), e);
            return Result.error("插入失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "通用更新", description = "在指定数据源上执行更新")
    @PostMapping("/update")
    public Result update(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource,
            @Parameter(description = "SQL 更新语句", required = true, example = "UPDATE users SET email = ? WHERE id = ?") 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("通用更新 - 数据源：{}, SQL: {}, 参数：{}", dataSource, sql, params);
        
        try {
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            int rows = dataSourceService.executeUpdate(dataSource, sql, paramArray);
            
            return Result.success(rows, "更新成功，影响 " + rows + " 行");
        } catch (Exception e) {
            log.error("更新失败：{}", e.getMessage(), e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "通用删除", description = "在指定数据源上执行删除")
    @PostMapping("/delete")
    public Result delete(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource,
            @Parameter(description = "SQL 删除语句", required = true, example = "DELETE FROM users WHERE id = ?") 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("通用删除 - 数据源：{}, SQL: {}, 参数：{}", dataSource, sql, params);
        
        try {
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            int rows = dataSourceService.executeUpdate(dataSource, sql, paramArray);
            
            return Result.success(rows, "删除成功，影响 " + rows + " 行");
        } catch (Exception e) {
            log.error("删除失败：{}", e.getMessage(), e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "分页查询", description = "在指定数据源上执行分页查询")
    @PostMapping("/page")
    public Result page(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource,
            @Parameter(description = "SQL 查询语句", required = true) 
            @RequestParam String sql,
            @Parameter(description = "页码", required = true, example = "1") 
            @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小", required = true, example = "10") 
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("分页查询 - 数据源：{}, SQL: {}, 页码：{}, 大小：{}", dataSource, sql, pageNum, pageSize);
        
        try {
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            var result = dataSourceService.executeQueryByPage(dataSource, sql, pageNum, pageSize, paramArray);
            
            return Result.success(result, "查询成功，共 " + result.getTotal() + " 条");
        } catch (Exception e) {
            log.error("分页查询失败：{}", e.getMessage(), e);
            return Result.error("分页查询失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取表列表", description = "获取指定数据源的所有表")
    @GetMapping("/tables")
    public Result listTables(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource) {
        
        log.info("获取表列表 - 数据源：{}", dataSource);
        
        try {
            List<String> tables = dataSourceService.listTables(dataSource);
            return Result.success(tables, "共 " + tables.size() + " 张表");
        } catch (Exception e) {
            log.error("获取表列表失败：{}", e.getMessage(), e);
            return Result.error("获取表列表失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取表结构", description = "获取指定表的列信息")
    @GetMapping("/table/columns")
    public Result getTableColumns(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String dataSource,
            @Parameter(description = "表名", required = true) 
            @RequestParam String tableName) {
        
        log.info("获取表结构 - 数据源：{}, 表：{}", dataSource, tableName);
        
        try {
            List<Map<String, Object>> columns = dataSourceService.getTableColumns(dataSource, tableName);
            return Result.success(columns, "共 " + columns.size() + " 列");
        } catch (Exception e) {
            log.error("获取表结构失败：{}", e.getMessage(), e);
            return Result.error("获取表结构失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "快速注册并查询", description = "临时注册数据源并执行查询（一步完成）")
    @PostMapping("/quick-query")
    public Result quickQuery(
            @Parameter(description = "数据库类型", required = true, example = "mysql") 
            @RequestParam String type,
            @Parameter(description = "主机地址", required = true, example = "192.168.1.100") 
            @RequestParam String host,
            @Parameter(description = "端口", required = true, example = "3306") 
            @RequestParam int port,
            @Parameter(description = "数据库名", required = true, example = "test_db") 
            @RequestParam String database,
            @Parameter(description = "用户名", required = true) 
            @RequestParam String username,
            @Parameter(description = "密码", required = true) 
            @RequestParam String password,
            @Parameter(description = "SQL 查询语句", required = true) 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        
        log.info("快速查询 - {}:{}:{}/{}", type, host, port, database);
        
        try {
            // 构建数据源配置
            DataSourceConfigDTO config = new DataSourceConfigDTO();
            config.setType(type);
            config.setHost(host);
            config.setPort(port);
            config.setDatabase(database);
            config.setUsername(username);
            config.setPassword(password);
            
            // 注册数据源
            String dataSourceKey = dataSourceService.registerDataSource(config);
            
            // 执行查询
            Object[] paramArray = params != null ? params.toArray() : new Object[0];
            List<Map<String, Object>> result = dataSourceService.executeQuery(dataSourceKey, sql, paramArray);
            
            return Result.success(result, "查询成功，返回 " + result.size() + " 行");
        } catch (Exception e) {
            log.error("快速查询失败：{}", e.getMessage(), e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
