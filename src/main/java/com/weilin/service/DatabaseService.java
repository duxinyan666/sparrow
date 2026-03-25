package com.weilin.service;

import java.util.List;
import java.util.Map;

/**
 * 数据库服务接口
 * 提供统一的 SQL 数据库操作抽象（支持 MySQL/PostgreSQL）
 */
public interface DatabaseService {
    
    /**
     * 执行查询
     * @param sourceName 数据源名称
     * @param sql SQL 语句
     * @param params 参数列表
     * @return 结果列表
     */
    List<Map<String, Object>> query(String sourceName, String sql, Object... params);
    
    /**
     * 执行更新
     * @param sourceName 数据源名称
     * @param sql SQL 语句
     * @param params 参数列表
     * @return 影响行数
     */
    int update(String sourceName, String sql, Object... params);
    
    /**
     * 执行插入并返回生成的主键
     * @param sourceName 数据源名称
     * @param sql SQL 语句
     * @param params 参数列表
     * @return 生成的主键
     */
    Long insertAndGetKey(String sourceName, String sql, Object... params);
    
    /**
     * 批量执行
     * @param sourceName 数据源名称
     * @param sql SQL 语句
     * @param batchParams 批量参数
     * @return 每批影响行数
     */
    int[] batchUpdate(String sourceName, String sql, List<Object[]> batchParams);
    
    /**
     * 检查表是否存在
     * @param sourceName 数据源名称
     * @param tableName 表名
     * @return 是否存在
     */
    Boolean tableExists(String sourceName, String tableName);
    
    /**
     * 获取表结构
     * @param sourceName 数据源名称
     * @param tableName 表名
     * @return 列信息
     */
    List<Map<String, Object>> getTableColumns(String sourceName, String tableName);
    
    /**
     * 获取数据库连接池状态
     * @param sourceName 数据源名称
     * @return 连接池状态
     */
    Map<String, Object> getPoolStatus(String sourceName);
    
    /**
     * 测试连接
     * @param sourceName 数据源名称
     * @return 是否成功
     */
    Boolean testConnection(String sourceName);
}
