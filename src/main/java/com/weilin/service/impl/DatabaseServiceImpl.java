package com.weilin.service.impl;

import com.weilin.datasource.DataSourceManager;
import com.weilin.exception.ConnectionException;
import com.weilin.exception.DataSourceException;
import com.weilin.service.DatabaseService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * 数据库服务实现
 * 支持 MySQL 和 PostgreSQL
 */
@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {
    
    @Autowired
    private DataSourceManager dataSourceManager;
    
    /**
     * 获取数据库连接
     */
    private Connection getConnection(String sourceName) {
        try {
            HikariDataSource dataSource = dataSourceManager.get(sourceName);
            if (dataSource == null) {
                throw new DataSourceException("数据源不存在：" + sourceName, 404);
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("获取数据库连接失败：{}", e.getMessage());
            throw new ConnectionException("无法连接到数据库：" + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> query(String sourceName, String sql, Object... params) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = getConnection(sourceName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParams(pstmt, params);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    result.add(row);
                }
            }
            
            log.debug("SQL 查询成功：{} 行返回", result.size());
        } catch (SQLException e) {
            log.error("SQL 查询失败：{}, SQL: {}", e.getMessage(), sql);
            throw new DataSourceException("查询失败：" + e.getMessage(), e);
        }
        return result;
    }
    
    @Override
    public int update(String sourceName, String sql, Object... params) {
        try (Connection conn = getConnection(sourceName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParams(pstmt, params);
            int rows = pstmt.executeUpdate();
            
            log.debug("SQL 更新成功：{} 行影响", rows);
            return rows;
        } catch (SQLException e) {
            log.error("SQL 更新失败：{}, SQL: {}", e.getMessage(), sql);
            throw new DataSourceException("更新失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public Long insertAndGetKey(String sourceName, String sql, Object... params) {
        try (Connection conn = getConnection(sourceName);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setParams(pstmt, params);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long key = rs.getLong(1);
                    log.debug("SQL 插入成功，生成主键：{}", key);
                    return key;
                }
            }
            
            log.debug("SQL 插入成功，无生成主键");
            return null;
        } catch (SQLException e) {
            log.error("SQL 插入失败：{}, SQL: {}", e.getMessage(), sql);
            throw new DataSourceException("插入失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public int[] batchUpdate(String sourceName, String sql, List<Object[]> batchParams) {
        try (Connection conn = getConnection(sourceName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Object[] params : batchParams) {
                setParams(pstmt, params);
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            log.debug("SQL 批量执行成功：{} 批", results.length);
            return results;
        } catch (SQLException e) {
            log.error("SQL 批量执行失败：{}, SQL: {}", e.getMessage(), sql);
            throw new DataSourceException("批量执行失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public Boolean tableExists(String sourceName, String tableName) {
        try (Connection conn = getConnection(sourceName)) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = conn.getCatalog();
            String schema = conn.getSchema();
            
            try (ResultSet rs = metaData.getTables(catalog, schema, tableName, new String[]{"TABLE"})) {
                Boolean exists = rs.next();
                log.debug("表 {} 存在性检查：{}", tableName, exists);
                return exists;
            }
        } catch (SQLException e) {
            log.error("表存在性检查失败：{}", e.getMessage());
            throw new DataSourceException("检查失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getTableColumns(String sourceName, String tableName) {
        List<Map<String, Object>> columns = new ArrayList<>();
        try (Connection conn = getConnection(sourceName)) {
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
                    column.put("isAutoIncrement", "YES".equals(rs.getString("IS_AUTOINCREMENT")));
                    column.put("remarks", rs.getString("REMARKS"));
                    columns.add(column);
                }
            }
            
            log.debug("获取表 {} 结构：{} 列", tableName, columns.size());
        } catch (SQLException e) {
            log.error("获取表结构失败：{}", e.getMessage());
            throw new DataSourceException("获取结构失败：" + e.getMessage(), e);
        }
        return columns;
    }
    
    @Override
    public Map<String, Object> getPoolStatus(String sourceName) {
        Map<String, Object> status = new HashMap<>();
        try {
            HikariDataSource dataSource = dataSourceManager.get(sourceName);
            if (dataSource == null) {
                throw new DataSourceException("数据源不存在：" + sourceName, 404);
            }
            
            status.put("sourceName", sourceName);
            status.put("activeConnections", dataSource.getHikariPoolMXBean().getActiveConnections());
            status.put("idleConnections", dataSource.getHikariPoolMXBean().getIdleConnections());
            status.put("totalConnections", dataSource.getHikariPoolMXBean().getTotalConnections());
            status.put("threadsAwaitingConnection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
            status.put("maxPoolSize", dataSource.getMaximumPoolSize());
            status.put("minIdle", dataSource.getMinimumIdle());
            status.put("connectionTimeout", dataSource.getConnectionTimeout());
            status.put("maxLifetime", dataSource.getMaxLifetime());
            status.put("idleTimeout", dataSource.getIdleTimeout());
            status.put("connected", true);
            
            log.debug("连接池状态：{}", status);
        } catch (Exception e) {
            status.put("connected", false);
            status.put("error", e.getMessage());
            log.error("获取连接池状态失败：{}", e.getMessage());
        }
        return status;
    }
    
    @Override
    public Boolean testConnection(String sourceName) {
        try (Connection conn = getConnection(sourceName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            
            Boolean success = rs.next() && rs.getInt(1) == 1;
            log.debug("连接测试：{} - {}", sourceName, success ? "成功" : "失败");
            return success;
        } catch (SQLException e) {
            log.error("连接测试失败：{}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 设置 PreparedStatement 参数
     */
    private void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
    }
}
