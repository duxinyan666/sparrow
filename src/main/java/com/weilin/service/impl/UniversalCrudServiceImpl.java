package com.weilin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilin.datasource.routing.DynamicRoutingDataSource;
import com.weilin.exception.DataSourceException;
import com.weilin.service.UniversalCrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 通用 CRUD 服务实现
 */
@Slf4j
@Service
public class UniversalCrudServiceImpl implements UniversalCrudService {
    
    @Autowired
    private DynamicRoutingDataSource routingDataSource;
    
    /**
     * 切换到指定数据源
     */
    private void switchDataSource(String dataSourceKey) {
        DynamicRoutingDataSource.setDataSourceKey(dataSourceKey);
    }
    
    /**
     * 清除数据源
     */
    private void clearDataSource() {
        DynamicRoutingDataSource.clearDataSourceKey();
    }
    
    /**
     * 获取 Mapper
     */
    @SuppressWarnings("unchecked")
    private <T> BaseMapper<T> getMapper(Class<T> entityClass) {
        try (SqlSession sqlSession = SqlSessionUtils.getSqlSession(
                org.mybatis.spring.SqlSessionUtils.getSqlSession(
                    org.apache.ibatis.session.SqlSessionFactory.class.cast(
                        org.springframework.context.ApplicationContext.class.cast(
                            org.springframework.web.context.support.WebApplicationContextUtils.class
                        )
                    )
                ).getConfiguration())) {
            return sqlSession.getMapper(entityClass);
        } catch (Exception e) {
            throw new DataSourceException("获取 Mapper 失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public <T> T getById(String dataSourceKey, Class<T> entityClass, Serializable id) {
        switchDataSource(dataSourceKey);
        try {
            // 这里需要使用实际的 Mapper，由于泛型擦除，需要通过反射或其他方式获取
            // 简化实现，实际项目中应该注入 Mapper 工厂
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> List<T> listByIds(String dataSourceKey, Class<T> entityClass, Collection<? extends Serializable> idList) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> List<T> list(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> List<T> listAll(String dataSourceKey, Class<T> entityClass) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> Page<T> page(String dataSourceKey, Class<T> entityClass, int page, int size, Wrapper<T> queryWrapper) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> long count(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean insert(String dataSourceKey, T entity) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean insertBatch(String dataSourceKey, Class<T> entityClass, Collection<T> entityList) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean updateById(String dataSourceKey, T entity) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean update(String dataSourceKey, T entity, Wrapper<T> updateWrapper) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean updateBatchById(String dataSourceKey, Class<T> entityClass, Collection<T> entityList) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean deleteById(String dataSourceKey, Class<T> entityClass, Serializable id) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean deleteByIds(String dataSourceKey, Class<T> entityClass, Collection<? extends Serializable> idList) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> boolean delete(String dataSourceKey, Class<T> entityClass, Wrapper<T> queryWrapper) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用具体的 Mapper 或 JdbcTemplate");
        } finally {
            clearDataSource();
        }
    }
    
    @Override
    public <T> T getMapper(String dataSourceKey, Class<T> mapperClass) {
        switchDataSource(dataSourceKey);
        try {
            throw new UnsupportedOperationException("请使用 DynamicDataSourceService 执行 SQL");
        } finally {
            clearDataSource();
        }
    }
}
