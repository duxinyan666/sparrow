package com.weilin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilin.datasource.dto.DataSourceConfigDTO;
import com.weilin.datasource.dto.DataSourceInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * 动态数据源服务接口
 */
public interface DynamicDataSourceService {
    
    /**
     * 注册数据源
     * @param config 数据源配置
     * @return 数据源键名
     */
    String registerDataSource(DataSourceConfigDTO config);
    
    /**
     * 注销数据源
     * @param key 数据源键名
     * @return 是否成功
     */
    boolean unregisterDataSource(String key);
    
    /**
     * 获取所有数据源列表
     * @return 数据源信息列表
     */
    List<DataSourceInfoDTO> listDataSources();
    
    /**
     * 获取数据源详情
     * @param key 数据源键名
     * @return 数据源信息
     */
    DataSourceInfoDTO getDataSource(String key);
    
    /**
     * 测试数据源连接
     * @param key 数据源键名
     * @return 是否成功
     */
    boolean testConnection(String key);
    
    /**
     * 切换当前线程的数据源
     * @param key 数据源键名
     */
    void switchDataSource(String key);
    
    /**
     * 清除当前线程的数据源
     */
    void clearDataSource();
    
    /**
     * 执行 SQL 查询
     * @param key 数据源键名
     * @param sql SQL 语句
     * @param params 参数
     * @return 查询结果
     */
    List<Map<String, Object>> executeQuery(String key, String sql, Object... params);
    
    /**
     * 执行 SQL 更新
     * @param key 数据源键名
     * @param sql SQL 语句
     * @param params 参数
     * @return 影响行数
     */
    int executeUpdate(String key, String sql, Object... params);
    
    /**
     * 分页查询
     * @param key 数据源键名
     * @param sql SQL 语句
     * @param page 页码
     * @param size 每页大小
     * @param params 参数
     * @return 分页结果
     */
    Page<Map<String, Object>> executeQueryByPage(String key, String sql, int page, int size, Object... params);
    
    /**
     * 获取所有表名
     * @param key 数据源键名
     * @return 表名列表
     */
    List<String> listTables(String key);
    
    /**
     * 获取表结构
     * @param key 数据源键名
     * @param tableName 表名
     * @return 列信息
     */
    List<Map<String, Object>> getTableColumns(String key, String tableName);
}
