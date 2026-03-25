package com.weilin.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用 CRUD 服务接口
 * 支持指定数据源执行操作
 */
public interface UniversalCrudService {
    
    // ==================== 查询操作 ====================
    
    /**
     * 根据 ID 查询
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param id 主键 ID
     * @return 实体对象
     */
    <T> T getById(String dataSourceKey, Class<T> entityClass, Serializable id);
    
    /**
     * 批量查询
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param idList 主键 ID 列表
     * @return 实体列表
     */
    <T> List<T> listByIds(String dataSourceKey, Class<T> entityClass, Collection<? extends Serializable> idList);
    
    /**
     * 条件查询列表
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param queryWrapper 查询条件
     * @return 实体列表
     */
    <T> List<T> list(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper);
    
    /**
     * 查询所有
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @return 实体列表
     */
    <T> List<T> listAll(String dataSourceKey, Class<T> entityClass);
    
    /**
     * 分页查询
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param page 页码
     * @param size 每页大小
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    <T> Page<T> page(String dataSourceKey, Class<T> entityClass, int page, int size, Wrapper<T> queryWrapper);
    
    /**
     * 查询总数
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param queryWrapper 查询条件
     * @return 总数
     */
    <T> long count(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper);
    
    // ==================== 插入操作 ====================
    
    /**
     * 插入单条
     * @param dataSourceKey 数据源键名
     * @param entity 实体对象
     * @return 是否成功
     */
    <T> boolean insert(String dataSourceKey, T entity);
    
    /**
     * 批量插入
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param entityList 实体列表
     * @return 是否成功
     */
    <T> boolean insertBatch(String dataSourceKey, Class<T> entityClass, Collection<T> entityList);
    
    // ==================== 更新操作 ====================
    
    /**
     * 根据 ID 更新
     * @param dataSourceKey 数据源键名
     * @param entity 实体对象（必须包含 ID）
     * @return 是否成功
     */
    <T> boolean updateById(String dataSourceKey, T entity);
    
    /**
     * 根据条件更新
     * @param dataSourceKey 数据源键名
     * @param entity 实体对象（更新的字段）
     * @param updateWrapper 更新条件
     * @return 是否成功
     */
    <T> boolean update(String dataSourceKey, T entity, Wrapper<T> updateWrapper);
    
    /**
     * 批量根据 ID 更新
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param entityList 实体列表
     * @return 是否成功
     */
    <T> boolean updateBatchById(String dataSourceKey, Class<T> entityClass, Collection<T> entityList);
    
    // ==================== 删除操作 ====================
    
    /**
     * 根据 ID 删除
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param id 主键 ID
     * @return 是否成功
     */
    <T> boolean deleteById(String dataSourceKey, Class<T> entityClass, Serializable id);
    
    /**
     * 批量删除
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param idList 主键 ID 列表
     * @return 是否成功
     */
    <T> boolean deleteByIds(String dataSourceKey, Class<T> entityClass, Collection<? extends Serializable> idList);
    
    /**
     * 根据条件删除
     * @param dataSourceKey 数据源键名
     * @param entityClass 实体类
     * @param queryWrapper 查询条件
     * @return 是否成功
     */
    <T> boolean delete(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper);
    
    // ==================== 工具方法 ====================
    
    /**
     * 获取 Mapper
     * @param dataSourceKey 数据源键名
     * @param mapperClass Mapper 类
     * @return Mapper 实例
     */
    <T> T getMapper(String dataSourceKey, Class<T> mapperClass);
}
