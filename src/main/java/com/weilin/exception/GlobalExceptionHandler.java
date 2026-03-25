package com.weilin.exception;

import com.weilin.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理所有控制器层抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理数据源异常
     */
    @ExceptionHandler(DataSourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleDataSourceException(DataSourceException e) {
        log.error("数据源操作异常：{}", e.getMessage(), e);
        return Result.error(e.getErrorCode(), e.getMessage());
    }
    
    /**
     * 处理配置异常
     */
    @ExceptionHandler(ConfigException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleConfigException(ConfigException e) {
        log.error("配置异常：{}", e.getMessage(), e);
        return Result.error(400, e.getMessage());
    }
    
    /**
     * 处理连接异常
     */
    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Result handleConnectionException(ConnectionException e) {
        log.error("连接异常：{}", e.getMessage(), e);
        return Result.error(503, "服务暂时不可用：" + e.getMessage());
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleValidationException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数验证失败：{}", errors);
        return Result.error(400, "参数验证失败：" + errors);
    }
    
    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(BindException e) {
        String errors = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数绑定失败：{}", errors);
        return Result.error(400, "参数绑定失败：" + errors);
    }
    
    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        log.error("未知异常：{}", e.getMessage(), e);
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
