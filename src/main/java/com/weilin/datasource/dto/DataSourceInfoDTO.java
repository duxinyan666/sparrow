package com.weilin.datasource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据源信息 DTO
 */
@Data
@Schema(description = "数据源信息")
public class DataSourceInfoDTO {
    
    @Schema(description = "数据源键名")
    private String key;
    
    @Schema(description = "数据库类型")
    private String type;
    
    @Schema(description = "主机地址")
    private String host;
    
    @Schema(description = "端口")
    private Integer port;
    
    @Schema(description = "数据库名")
    private String database;
    
    @Schema(description = "活跃连接数")
    private Integer activeConnections;
    
    @Schema(description = "空闲连接数")
    private Integer idleConnections;
    
    @Schema(description = "总连接数")
    private Integer totalConnections;
    
    @Schema(description = "状态")
    private String status;
}
