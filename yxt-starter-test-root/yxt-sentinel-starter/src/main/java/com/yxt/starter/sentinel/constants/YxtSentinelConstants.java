package com.yxt.starter.sentinel.constants;

/**
 * 常量定义
 *
 * @Author liqiang
 * @Date 2024/8/28 21:57
 */
public final class YxtSentinelConstants {

    /**
     * YxtSentinel容器名字
     */
    public static final String CONTEXT_NAME = "YxtSentinelContext";
    /**
     * yxtSentinel配置文件key
     */
    public static final String YXT_SENTINEL_CONFIG = "yxtSentinelConfig";
    /**
     * 加载yxtSentinel环境变量优先级 12 表示在applo配置加载之后
     */
    public static final int YXT_SENTINEL_ENVIRONMENT_PROCESS_ORDER = 12;
    /**
     * 客户端ip
     */
    public static final String HEAD_CLIENT_IP_KEY = "x-real-ip";
    /**
     * serviceName
     */
    public static final String HEAD_SERVICE_NAME_KEY = "service-name";

    /**
     * yxt sentinel前缀
     */
    public static final String PROPERTY_PREFIX = "yxt.sentinel";

    private YxtSentinelConstants() {

    }
}
