package com.weilin.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.weilin.datasource.routing.DynamicRoutingDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * MyBatis Plus 配置
 */
@Configuration
@MapperScan(basePackages = "com.weilin.mapper", annotationClass = Mapper.class)
public class MybatisPlusConfig {
    
    /**
     * MyBatis Plus 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    
    /**
     * MyBatis SqlSessionFactory
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DynamicRoutingDataSource routingDataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(true);
        configuration.setAggressiveLazyLoading(false);
        
        factoryBean.setConfiguration(configuration);
        
        return factoryBean;
    }
    
    /**
     * 事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicRoutingDataSource routingDataSource) {
        return new DataSourceTransactionManager(routingDataSource);
    }
}
