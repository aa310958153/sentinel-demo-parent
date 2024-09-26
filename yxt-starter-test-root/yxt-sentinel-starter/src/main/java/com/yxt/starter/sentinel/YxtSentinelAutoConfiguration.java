package com.yxt.starter.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.yxt.starter.sentinel.aspectj.YxtSentinelAspect;
import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import com.yxt.starter.sentinel.context.YxtSentinelContext;
import com.yxt.starter.sentinel.context.YxtSentinelSpecification;
import com.yxt.starter.sentinel.context.YxtSentinelSpecificationRegister;
import com.yxt.starter.sentinel.fegin.YxtSentinelFeign;
import com.yxt.starter.sentinel.fegin.YxtSentinelFeignRequestInterceptor;
import com.yxt.starter.sentinel.io.YxtSentinelConfigLoader;
import feign.Feign;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

/**
 * yxt-sentinel-spring-boot-starter 自动配置类
 *
 * @Author liqiang
 * @Date 2024/8/22 16:46
 */
@Configuration
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class YxtSentinelAutoConfiguration {


    /**
     * sentinel异常处理器
     *
     * @return
     */
    @Bean
    public YxtSentinelHandlerExceptionResolver yxtSentinelHandlerExceptionResolver() {
        return new YxtSentinelHandlerExceptionResolver();
    }

    @Bean
    public YxtSentinelSpecificationRegister yxtSentinelSpecificationRegister() {
        return new YxtSentinelSpecificationRegister();
    }

    @Bean
    @Order(0)
    public YxtSentinelAspect yxtSentinelAspect() {
        return new YxtSentinelAspect();
    }

    /**
     * 自定义流控处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler yxtCustomBlockExceptionHandler() {
        return new YxtCustomBlockExceptionHandler();
    }

    /**
     * 自定义来源解析器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RequestOriginParser.class)
    public YxtCustomRequestOriginParser yxtCustomRequestOriginParser() {
        return new YxtCustomRequestOriginParser();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder yxtFeignSentinelBuilder() {
        return YxtSentinelFeign.builder();
    }

    /**
     * 初始化yxt-sentinelContext容器
     *
     * @param yxtSentinelSpecificationRegister
     * @return
     */
    @Bean
    @Lazy
    public YxtSentinelContext yxtSentinelContext(YxtSentinelSpecificationRegister yxtSentinelSpecificationRegister) {
        YxtSentinelContext yxtSentinelContext = new YxtSentinelContext();
        List<Class<?>> classes = YxtSentinelConfigLoader.loadYxtSentinelConfig(this.getClass().getClassLoader());
        List<YxtSentinelSpecification> yxtSentinelSpecificationList = yxtSentinelSpecificationRegister.getYxtSentinelSpecificationList();
        if (CollectionUtils.isEmpty(yxtSentinelSpecificationList)) {
            yxtSentinelSpecificationList = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(classes)) {
            YxtSentinelSpecification yxtSentinelSpecification = new YxtSentinelSpecification();
            yxtSentinelSpecification.setName(YxtSentinelConstants.CONTEXT_NAME);
            yxtSentinelSpecification.setConfiguration(classes.toArray(new Class[0]));
            yxtSentinelSpecificationList.add(yxtSentinelSpecification);
        }
        yxtSentinelContext.setConfigurations(yxtSentinelSpecificationList);
        return yxtSentinelContext;
    }

    @Bean
    public SentinelEventObserverRegistry sentinelEventObserverRegistry() {
        return new SentinelEventObserverRegistry();
    }

    @Bean
    public YxtSentinelFeignRequestInterceptor yxtSentinelFeignRequestInterceptor() {
        return new YxtSentinelFeignRequestInterceptor();
    }

}
