package com.weilin.datasource.routing;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源管理器
 * 支持运行时动态添加、删除数据源
 */
@Slf4j
@Data
@Component
public class DynamicDataSourceManager {
    
    /**
     * 存储所有动态数据源
     * key: 数据源键名（格式：type:host:database）
     * value: HikariDataSource
     */
    private final Map<String, HikariDataSource> dynamicDataSources = new ConcurrentHashMap<>();
    
    /**
     * 数据源路由
     */
    private DynamicRoutingDataSource routingDataSource;
    
    /**
     * 设置路由数据源
     */
    public void setRoutingDataSource(DynamicRoutingDataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }
    
    /**
     * 添加数据源
     * @param key 数据源键名
     * @param dataSource 数据源实例
     */
    public void addDataSource(String key, HikariDataSource dataSource) {
        dynamicDataSources.put(key, dataSource);
        updateTargetDataSources();
        log.info("添加数据源：{}", key);
    }
    
    /**
     * 移除数据源
     * @param key 数据源键名
     */
    public void removeDataSource(String key) {
        HikariDataSource dataSource = dynamicDataSources.remove(key);
        if (dataSource != null) {
            dataSource.close();
            updateTargetDataSources();
            log.info("移除数据源：{}", key);
        }
    }
    
    /**
     * 获取数据源
     * @param key 数据源键名
     * @return 数据源实例
     */
    public HikariDataSource getDataSource(String key) {
        return dynamicDataSources.get(key);
    }
    
    /**
     * 检查数据源是否存在
     * @param key 数据源键名
     * @return 是否存在
     */
    public boolean containsDataSource(String key) {
        return dynamicDataSources.containsKey(key);
    }
    
    /**
     * 更新路由数据源的目标数据源
     */
    private void updateTargetDataSources() {
        if (routingDataSource != null) {
            routingDataSource.setTargetDataSources(dynamicDataSources);
            routingDataSource.afterPropertiesSet();
            log.debug("更新路由数据源，当前数据源数量：{}", dynamicDataSources.size());
        }
    }
    
    /**
     * 创建数据源键名
     * @param type 数据库类型（mysql/postgresql）
     * @param host 主机地址
     * @param port 端口
     * @param database 数据库名
     * @return 数据源键名
     */
    public static String createDataSourceKey(String type, String host, int port, String database) {
        return String.format("%s:%s:%d:%s", type, host, port, database);
    }
    
    /**
     * 解析数据源键名
     * @param key 数据源键名
     * @return 数据源信息
     */
    public static DataSourceInfo parseDataSourceKey(String key) {
        String[] parts = key.split(":");
        if (parts.length >= 4) {
            DataSourceInfo info = new DataSourceInfo();
            info.setType(parts[0]);
            info.setHost(parts[1]);
            info.setPort(Integer.parseInt(parts[2]));
            info.setDatabase(parts[3]);
            return info;
        }
        return null;
    }
    
    /**
     * 创建 Hikari 数据源配置
     * @param type 数据库类型
     * @param host 主机地址
     * @param port 端口
     * @param database 数据库名
     * @param username 用户名
     * @param password 密码
     * @return HikariConfig
     */
    public static HikariConfig createHikariConfig(String type, String host, int port, 
                                                   String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        
        // JDBC URL
        String jdbcUrl;
        String driverClassName;
        
        if ("mysql".equalsIgnoreCase(type)) {
            jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true",
                    host, port, database);
            driverClassName = "com.mysql.cj.jdbc.Driver";
        } else if ("postgresql".equalsIgnoreCase(type)) {
            jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?ssl=false&stringtype=unspecified",
                    host, port, database);
            driverClassName = "org.postgresql.Driver";
        } else {
            throw new IllegalArgumentException("不支持的数据库类型：" + type);
        }
        
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(driverClassName);
        config.setUsername(username);
        config.setPassword(password);
        
        // 连接池配置
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setIdleTimeout(600000);
        
        return config;
    }
    
    /**
     * 数据源信息
     */
    @Data
    public static class DataSourceInfo {
        private String type;
        private String host;
        private int port;
        private String database;
    }
}
