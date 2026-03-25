package com.weilin.exception;

/**
 * 数据源操作异常
 * 统一的数据源相关异常基类
 */
public class DataSourceException extends RuntimeException {
    
    private final int errorCode;
    
    public DataSourceException(String message) {
        super(message);
        this.errorCode = 500;
    }
    
    public DataSourceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 500;
    }
    
    public DataSourceException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
}
