package com.weilin.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Micrometer 监控配置
 * 提供数据源连接池指标
 */
@Configuration
public class MetricsConfig {
    
    /**
     * 注册数据源指标
     */
    @Bean
    public MeterBinder dataSourceMetrics() {
        return (registry) -> {
            // 可以在这里注册自定义数据源指标
            // 具体实现需要在 DataSourceManager 中维护数据源引用
        };
    }
    
    /**
     * 应用自定义指标
     */
    @Bean
    public MeterBinder applicationMetrics() {
        return (registry) -> {
            Gauge.builder("app.uptime", () -> 
                    java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime())
                    .description("Application uptime in milliseconds")
                    .baseUnit("milliseconds")
                    .register(registry);
            
            Gauge.builder("app.available.processors", () -> 
                    (double) Runtime.getRuntime().availableProcessors())
                    .description("Number of available processors")
                    .register(registry);
            
            Gauge.builder("app.memory.heap.used", () -> 
                    (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))
                    .description("Heap memory used in bytes")
                    .baseUnit("bytes")
                    .register(registry);
            
            Gauge.builder("app.memory.heap.max", () -> 
                    (double) Runtime.getRuntime().maxMemory())
                    .description("Maximum heap memory in bytes")
                    .baseUnit("bytes")
                    .register(registry);
        };
    }
}
