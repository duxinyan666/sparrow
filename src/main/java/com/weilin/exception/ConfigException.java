package com.weilin.exception;

/**
 * 配置异常
 * 配置文件加载或解析失败时抛出
 */
public class ConfigException extends DataSourceException {
    
    public ConfigException(String message) {
        super(message, 400);
    }
    
    public ConfigException(String message, Throwable cause) {
        super(message, cause, 400);
    }
}
