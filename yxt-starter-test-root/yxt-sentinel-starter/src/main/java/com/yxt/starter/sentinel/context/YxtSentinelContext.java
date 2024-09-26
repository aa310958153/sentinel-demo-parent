package com.yxt.starter.sentinel.context;

import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * @Author liqiang
 * @Date 2024/8/28 11:42
 */
public class YxtSentinelContext extends NamedContextFactory<YxtSentinelSpecification> {

    public YxtSentinelContext() {
        super(YxtSentinelSpecification.class, "yxt", "yxt.sentinel.context.name");
    }
}
