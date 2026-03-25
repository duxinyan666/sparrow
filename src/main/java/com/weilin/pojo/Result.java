package com.weilin.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一响应结果封装
 * 所有 API 接口返回的标准格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private int code;
    private Object data;
    private String message;
    private String timestamp;
    private String path;

    public Result(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Result() {
        this.code = 200;
        this.message = "success";
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * 成功响应（无数据）
     */
    public static Result success() {
        return new Result(200, null, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static Result success(Object data) {
        return new Result(200, data, "操作成功");
    }

    /**
     * 成功响应（带数据和消息）
     */
    public static Result success(Object data, String message) {
        return new Result(200, data, message);
    }

    /**
     * 错误响应
     */
    public static Result error(int code, String message) {
        return new Result(code, null, message);
    }

    /**
     * 错误响应（默认 500）
     */
    public static Result error(String message) {
        return new Result(500, null, message);
    }

    /**
     * 设置响应路径（由拦截器填充）
     */
    public Result withPath(String path) {
        this.path = path;
        return this;
    }
}
