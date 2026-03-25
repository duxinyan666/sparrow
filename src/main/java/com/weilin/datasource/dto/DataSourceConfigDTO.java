package com.weilin.datasource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 动态数据源配置 DTO
 */
@Data
@Schema(description = "动态数据源配置")
public class DataSourceConfigDTO {
    
    @NotBlank(message = "数据库类型不能为空")
    @Schema(description = "数据库类型", example = "mysql", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;
    
    @NotBlank(message = "主机地址不能为空")
    @Schema(description = "主机地址", example = "192.168.1.100", requiredMode = Schema.RequiredMode.REQUIRED)
    private String host;
    
    @NotNull(message = "端口不能为空")
    @Schema(description = "端口", example = "3306", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer port;
    
    @NotBlank(message = "数据库名不能为空")
    @Schema(description = "数据库名", example = "test_db", requiredMode = Schema.RequiredMode.REQUIRED)
    private String database;
    
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "root", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @Schema(description = "密码", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    
    @Schema(description = "连接池最大连接数", example = "10")
    private Integer maxPoolSize = 10;
    
    @Schema(description = "最小空闲连接数", example = "2")
    private Integer minIdle = 2;
    
    @Schema(description = "连接超时时间 (ms)", example = "30000")
    private Integer connectionTimeout = 30000;
}
