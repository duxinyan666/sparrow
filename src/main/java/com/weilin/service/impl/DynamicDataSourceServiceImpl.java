package com.weilin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilin.datasource.dto.DataSourceConfigDTO;
import com.weilin.datasource.dto.DataSourceInfoDTO;
import com.weilin.datasource.routing.DynamicDataSourceManager;
import com.weilin.datasource.routing.DynamicRoutingDataSource;
import com.weilin.exception.DataSourceException;
import com.weilin.service.DynamicDataSourceService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

/**
 * 动态数据源服务实现
 */
@Slf4j
@Service
public class DynamicDataSourceServiceImpl implements DynamicDataSourceService {
    
    @Autowired
    private DynamicDataSourceManager dataSourceManager;
    
    @Autowired
    private DynamicRoutingDataSource routingDataSource;
    
    @Override
    public String registerDataSource(DataSourceConfigDTO config) {
        try {
            // 生成数据源键名
            String key = DynamicDataSourceManager.createDataSourceKey(
                    config.getType(), 
                    config.getHost(), 
                    config.getPort(), 
                    config.getDatabase()
            );
            
            // 检查是否已存在
            if (dataSourceManager.containsDataSource(key)) {
                log.warn("数据源已存在：{}", key);
                return key;
            }
            
            // 创建 Hikari 配置
            HikariConfig hikariConfig = DynamicDataSourceManager.createHikariConfig(
                    config.getType(),
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()
            );
            
            // 应用自定义配置
            if (config.getMaxPoolSize() != null) {
                hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
            }
            if (config.getMinIdle() != null) {
                hikariConfig.setMinimumIdle(config.getMinIdle());
            }
            if (config.getConnectionTimeout() != null) {
                hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
            }
            
            // 创建数据源
            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
            
            // 测试连接
            try (Connection conn = dataSource.getConnection()) {
                if (!conn.isValid(5)) {
                    throw new DataSourceException("数据源连接测试失败");
                }
            }
            
            // 注册数据源
            dataSourceManager.addDataSource(key, dataSource);
            
            log.info("注册数据源成功：{}", key);
            return key;
            
        } catch (Exception e) {
            log.error("注册数据源失败：{}", e.getMessage(), e);
            throw new DataSourceException("注册数据源失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean unregisterDataSource(String key) {
        try {
            dataSourceManager.removeDataSource(key);
            log.info("注销数据源成功：{}", key);
            return true;
        } catch (Exception e) {
            log.error("注销数据源失败：{}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<DataSourceInfoDTO> listDataSources() {
        List<DataSourceInfoDTO> list = new ArrayList<>();
        
        for (Map.Entry<String, HikariDataSource> entry : dataSourceManager.getDynamicDataSources().entrySet()) {
            String key = entry.getKey();
            HikariDataSource ds = entry.getValue();
            
            DataSourceInfoDTO info = new DataSourceInfoDTO();
            info.setKey(key);
            
            // 解析键名
            DynamicDataSourceManager.DataSourceInfo dsInfo = 
                    DynamicDataSourceManager.parseDataSourceKey(key);
            if (dsInfo != null) {
                info.setType(dsInfo.getType());
                info.setHost(dsInfo.getHost());
                info.setPort(dsInfo.getPort());
                info.setDatabase(dsInfo.getDatabase());
            }
            
            // 连接池状态
            info.setActiveConnections(ds.getHikariPoolMXBean().getActiveConnections());
            info.setIdleConnections(ds.getHikariPoolMXBean().getIdleConnections());
            info.setTotalConnections(ds.getHikariPoolMXBean().getTotalConnections());
            info.setStatus("UP");
            
            list.add(info);
        }
        
        return list;
    }
    
    @Override
    public DataSourceInfoDTO getDataSource(String key) {
        HikariDataSource ds = dataSourceManager.getDataSource(key);
        if (ds == null) {
            throw new DataSourceException("数据源不存在：" + key, 404);
        }
        
        DataSourceInfoDTO info = new DataSourceInfoDTO();
        info.setKey(key);
        
        DynamicDataSourceManager.DataSourceInfo dsInfo = 
                DynamicDataSourceManager.parseDataSourceKey(key);
        if (dsInfo != null) {
            info.setType(dsInfo.getType());
            info.setHost(dsInfo.getHost());
            info.setPort(dsInfo.getPort());
            info.setDatabase(dsInfo.getDatabase());
        }
        
        info.setActiveConnections(ds.getHikariPoolMXBean().getActiveConnections());
        info.setIdleConnections(ds.getHikariPoolMXBean().getIdleConnections());
        info.setTotalConnections(ds.getHikariPoolMXBean().getTotalConnections());
        info.setStatus("UP");
        
        return info;
    }
    
    @Override
    public boolean testConnection(String key) {
        HikariDataSource ds = dataSourceManager.getDataSource(key);
        if (ds == null) {
            return false;
        }
        
        try (Connection conn = ds.getConnection()) {
            return conn.isValid(5);
        } catch (Exception e) {
            log.error("连接测试失败：{}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void switchDataSource(String key) {
        if (!dataSourceManager.containsDataSource(key)) {
            throw new DataSourceException("数据源不存在：" + key, 404);
        }
        DynamicRoutingDataSource.setDataSourceKey(key);
        log.info("切换数据源：{}", key);
    }
    
    @Override
    public void clearDataSource() {
        DynamicRoutingDataSource.clearDataSourceKey();
    }
    
    @Override
    public List<Map<String, Object>> executeQuery(String key, String sql, Object... params) {
        switchDataSource(key);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(routingDataSource);
            return jdbcTemplate.queryForList(sql, params);
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public int executeUpdate(String key, String sql, Object... params) {
        switchDataSource(key);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(routingDataSource);
            return jdbcTemplate.update(sql, params);
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public Page<Map<String, Object>> executeQueryByPage(String key, String sql, int page, int size, Object... params) {
        switchDataSource(key);
        try {
            // 简单分页实现（实际应该用 MyBatis Plus 的分页插件）
            String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS total";
            JdbcTemplate jdbcTemplate = new JdbcTemplate(routingDataSource);
            
            Long total = jdbcTemplate.queryForObject(countSql, params, Long.class);
            if (total == null) {
                total = 0L;
            }
            
            // 添加分页限制
            String pageSql = sql + " LIMIT " + ((page - 1) * size) + ", " + size;
            List<Map<String, Object>> records = jdbcTemplate.queryForList(pageSql, params);
            
            Page<Map<String, Object>> pageResult = new Page<>(page, size, total);
            pageResult.setRecords(records);
            
            return pageResult;
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public List<String> listTables(String key) {
        switchDataSource(key);
        try {
            List<String> tables = new ArrayList<>();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(routingDataSource);
            
            jdbcTemplate.execute((Connection conn) -> {
                DatabaseMetaData metaData = conn.getMetaData();
                String catalog = conn.getCatalog();
                String schema = conn.getSchema();
                
                try (ResultSet rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"})) {
                    while (rs.next()) {
                        tables.add(rs.getString("TABLE_NAME"));
                    }
                }
                return null;
            });
            
            return tables;
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public List<Map<String, Object>> getTableColumns(String key, String tableName) {
        switchDataSource(key);
        try {
            List<Map<String, Object>> columns = new ArrayList<>();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(routingDataSource);
            
            jdbcTemplate.execute((Connection conn) -> {
                DatabaseMetaData metaData = conn.getMetaData();
                String catalog = conn.getCatalog();
                String schema = conn.getSchema();
                
                try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, "%")) {
                    while (rs.next()) {
                        Map<String, Object> column = new LinkedHashMap<>();
                        column.put("columnName", rs.getString("COLUMN_NAME"));
                        column.put("dataType", rs.getString("DATA_TYPE"));
                        column.put("typeName", rs.getString("TYPE_NAME"));
                        column.put("columnSize", rs.getInt("COLUMN_SIZE"));
                        column.put("nullable", rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                        column.put("defaultValue", rs.getString("COLUMN_DEF"));
                        columns.add(column);
                    }
                }
                return null;
            });
            
            return columns;
        } finally {
            clearDataSource();
        }
    }
}
