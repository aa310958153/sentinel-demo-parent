package com.yxt.starter.sentinel.annotation;

/**
 * @Author liqiang
 * @Date 2024/8/27 15:43
 */

import com.alibaba.csp.sentinel.EntryType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author liqiang
 * @Date 2024/8/27 15:39
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YXTSentinel {

    /**
     * 是否上报资源
     *
     * @return
     */
    boolean shouldReportResource() default false;

    Class<?>[] configuration() default {};


    String value() default "";

    EntryType entryType() default EntryType.OUT;

    int resourceType() default 0;

    String blockHandler() default "";

    Class<?>[] blockHandlerClass() default {};

    String fallback() default "";

    String defaultFallback() default "";

    Class<?>[] fallbackClass() default {};

    Class<? extends Throwable>[] exceptionsToTrace() default {Throwable.class};

    Class<? extends Throwable>[] exceptionsToIgnore() default {};

    /**
     * 降级类定义
     *
     * @return
     */
    Class<?> configFallbackClass() default void.class;


}