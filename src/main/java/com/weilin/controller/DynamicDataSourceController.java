package com.weilin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilin.datasource.dto.DataSourceConfigDTO;
import com.weilin.datasource.dto.DataSourceInfoDTO;
import com.weilin.pojo.Result;
import com.weilin.service.DynamicDataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 动态数据源控制器
 * 支持运行时动态管理多数据源
 */
@Slf4j
@Tag(name = "DynamicDataSource", description = "动态数据源管理接口")
@RestController
@RequestMapping("/api/datasource")
public class DynamicDataSourceController {
    
    @Autowired
    private DynamicDataSourceService dataSourceService;
    
    @Operation(summary = "注册数据源", description = "动态注册一个新的数据源")
    @PostMapping("/register")
    public Result registerDataSource(@Valid @RequestBody DataSourceConfigDTO config) {
        log.info("注册数据源请求：{}:{}:{}/{}", config.getType(), config.getHost(), config.getPort(), config.getDatabase());
        
        String key = dataSourceService.registerDataSource(config);
        
        return Result.success(key, "数据源注册成功：" + key);
    }
    
    @Operation(summary = "注销数据源", description = "移除一个已注册的数据源")
    @DeleteMapping("/unregister/{key}")
    public Result unregisterDataSource(
            @Parameter(description = "数据源键名", required = true) 
            @PathVariable String key) {
        log.info("注销数据源请求：{}", key);
        
        boolean success = dataSourceService.unregisterDataSource(key);
        
        return Result.success(success, success ? "注销成功" : "注销失败");
    }
    
    @Operation(summary = "数据源列表", description = "获取所有已注册的数据源")
    @GetMapping("/list")
    public Result listDataSources() {
        log.info("获取数据源列表");
        
        List<DataSourceInfoDTO> list = dataSourceService.listDataSources();
        
        return Result.success(list, "共 " + list.size() + " 个数据源");
    }
    
    @Operation(summary = "数据源详情", description = "获取指定数据源的详细信息")
    @GetMapping("/{key}")
    public Result getDataSource(
            @Parameter(description = "数据源键名", required = true) 
            @PathVariable String key) {
        log.info("获取数据源详情：{}", key);
        
        DataSourceInfoDTO info = dataSourceService.getDataSource(key);
        
        return Result.success(info);
    }
    
    @Operation(summary = "测试连接", description = "测试数据源连接是否可用")
    @GetMapping("/{key}/test")
    public Result testConnection(
            @Parameter(description = "数据源键名", required = true) 
            @PathVariable String key) {
        log.info("测试数据源连接：{}", key);
        
        boolean success = dataSourceService.testConnection(key);
        
        return Result.success(success, success ? "连接正常" : "连接失败");
    }
    
    @Operation(summary = "执行 SQL 查询", description = "在指定数据源上执行 SQL 查询")
    @PostMapping("/query")
    public Result executeQuery(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String key,
            @Parameter(description = "SQL 语句", required = true) 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        log.info("执行 SQL 查询：{}, 数据源：{}", sql, key);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        List<Map<String, Object>> result = dataSourceService.executeQuery(key, sql, paramArray);
        
        return Result.success(result, "查询成功，返回 " + result.size() + " 行");
    }
    
    @Operation(summary = "执行 SQL 更新", description = "在指定数据源上执行 SQL 更新/插入/删除")
    @PostMapping("/update")
    public Result executeUpdate(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String key,
            @Parameter(description = "SQL 语句", required = true) 
            @RequestParam String sql,
            @RequestBody(required = false) 
            List<Object> params) {
        log.info("执行 SQL 更新：{}, 数据源：{}", sql, key);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        int rows = dataSourceService.executeUpdate(key, sql, paramArray);
        
        return Result.success(rows, "更新成功，影响 " + rows + " 行");
    }
    
    @Operation(summary = "分页查询", description = "在指定数据源上执行分页查询")
    @PostMapping("/query/page")
    public Result executeQueryByPage(
            @Parameter(description = "数据源键名", required = true) 
            @RequestParam String key,
            @Parameter(description = "SQL 语句", required = true) 
            @RequestParam String sql,
            @Parameter(description = "页码", required = true) 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", required = true) 
            @RequestParam(defaultValue = "10") int size,
            @RequestBody(required = false) 
            List<Object> params) {
        log.info("执行分页查询：{}, 数据源：{}, 页码：{}, 大小：{}", sql, key, page, size);
        
        Object[] paramArray = params != null ? params.toArray() : new Object[0];
        Page<Map<String, Object>> result = dataSourceService.executeQueryByPage(key, sql, page, size, paramArray);
        
        return Result.success(result, "查询成功，共 " + result.getTotal() + " 条");
    }
    
    @Operation(summary = "获取表列表", description = "获取指定数据源的所有表名")
    @GetMapping("/{key}/tables")
    public Result listTables(
            @Parameter(description = "数据源键名", required = true) 
            @PathVariable String key) {
        log.info("获取表列表：{}", key);
        
        List<String> tables = dataSourceService.listTables(key);
        
        return Result.success(tables, "共 " + tables.size() + " 张表");
    }
    
    @Operation(summary = "获取表结构", description = "获取指定表的列信息")
    @GetMapping("/{key}/table/{tableName}/columns")
    public Result getTableColumns(
            @Parameter(description = "数据源键名", required = true) 
            @PathVariable String key,
            @Parameter(description = "表名", required = true) 
            @PathVariable String tableName) {
        log.info("获取表结构：{}.{}", key, tableName);
        
        List<Map<String, Object>> columns = dataSourceService.getTableColumns(key, tableName);
        
        return Result.success(columns, "共 " + columns.size() + " 列");
    }
}
