package com.weilin.enums;

public enum ConfigKeyEnum {
    SOURCE_TYPE("sourceType"),
    SOURCE_NAME("sourceName"),

    HOST("host"),
    PORT("port"),

    USERNAME("username"),
    PASSWORD("password"),

    DATABASE("database"),
    URL("url"),
    DRIVER_CLASS("driverClassName"),

    SCHEME("scheme"),

    PARAMS("params"),

    MAX_POOL_SIZE("maxPoolSize"),
    MIN_IDLE("minIdle"),

    MAX_IDLE("maxIdle"),
    MAX_TOTAL("maxTotal"),

    CONNECTION_TIMEOUT("connectionTimeout"),

    TIMEOUT("timeOut");

    private final String key;

    ConfigKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
