package com.weilin.datasource.provider;

import com.alibaba.fastjson.JSONObject;
import com.weilin.common.annotation.MiddlewareType;
import com.weilin.datasource.DataSourceProvider;
import com.weilin.enums.ConfigKeyEnum;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * MySQL 数据源提供者
 * 使用 HikariCP 连接池管理数据库连接
 */
@MiddlewareType("mysql")
@Component
public class MySqlProvider implements DataSourceProvider {

    @Override
    public Object create(JSONObject config) {
        HikariConfig hikariConfig = new HikariConfig();
        
        // 基础配置
        String host = config.getString(ConfigKeyEnum.HOST.getKey());
        Integer port = config.getInteger(ConfigKeyEnum.PORT.getKey());
        String database = config.getString(ConfigKeyEnum.DATABASE.getKey());
        String username = config.getString(ConfigKeyEnum.USERNAME.getKey());
        String password = config.getString(ConfigKeyEnum.PASSWORD.getKey());
        
        // JDBC URL
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true",
                host, port, database);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        
        // 连接池配置
        JSONObject params = config.getJSONObject(ConfigKeyEnum.PARAMS.getKey());
        if (params != null) {
            hikariConfig.setMaximumPoolSize(params.getInteger(ConfigKeyEnum.MAX_POOL_SIZE.getKey()) != null ? 
                    params.getInteger(ConfigKeyEnum.MAX_POOL_SIZE.getKey()) : 10);
            hikariConfig.setMinimumIdle(params.getInteger(ConfigKeyEnum.MIN_IDLE.getKey()) != null ? 
                    params.getInteger(ConfigKeyEnum.MIN_IDLE.getKey()) : 2);
            hikariConfig.setConnectionTimeout(params.getInteger(ConfigKeyEnum.CONNECTION_TIMEOUT.getKey()) != null ? 
                    params.getInteger(ConfigKeyEnum.CONNECTION_TIMEOUT.getKey()) : 30000);
            hikariConfig.setMaxLifetime(1800000); // 30 分钟
            hikariConfig.setIdleTimeout(600000); // 10 分钟
        }
        
        // 驱动类
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // 连接测试
        hikariConfig.setConnectionTestQuery("SELECT 1");
        
        // 指标注册（用于 Micrometer 监控）
        hikariConfig.setMetricsTrackerFactory((poolName, registry) -> {
            // 可以在这里注册自定义指标
        });
        
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        
        return dataSource;
    }
}
