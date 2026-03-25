package com.weilin.datasource.routing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源路由
 * 根据 ThreadLocal 中存储的数据源键名切换数据源
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    
    /**
     * ThreadLocal 存储当前线程的数据源键名
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY = ThreadLocal.withInitial(() -> "default");
    
    /**
     * 设置当前线程使用的数据源
     * @param key 数据源键名（格式：type:host:database）
     */
    public static void setDataSourceKey(String key) {
        if (key == null || key.isEmpty()) {
            log.warn("数据源键名为空，使用默认数据源");
            key = "default";
        }
        DATA_SOURCE_KEY.set(key);
        log.debug("切换数据源：{}", key);
    }
    
    /**
     * 获取当前线程的数据源键名
     * @return 数据源键名
     */
    public static String getDataSourceKey() {
        return DATA_SOURCE_KEY.get();
    }
    
    /**
     * 清除当前线程的数据源键名
     */
    public static void clearDataSourceKey() {
        DATA_SOURCE_KEY.remove();
        log.debug("清除数据源键名");
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        String key = getDataSourceKey();
        log.trace("当前数据源键名：{}", key);
        return key;
    }
}
