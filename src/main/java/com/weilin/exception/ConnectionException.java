package com.weilin.exception;

/**
 * 连接异常
 * 数据源连接失败时抛出
 */
public class ConnectionException extends DataSourceException {
    
    public ConnectionException(String message) {
        super(message, 503);
    }
    
    public ConnectionException(String message, Throwable cause) {
        super(message, cause, 503);
    }
}
