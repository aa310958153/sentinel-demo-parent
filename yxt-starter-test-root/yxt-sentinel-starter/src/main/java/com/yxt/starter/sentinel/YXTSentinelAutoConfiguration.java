package com.yxt.starter.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.yxt.starter.sentinel.aspectj.YXTSentinelAspect;
import com.yxt.starter.sentinel.constants.YXTSentinelConstants;
import com.yxt.starter.sentinel.context.YXTSentinelContext;
import com.yxt.starter.sentinel.context.YXTSentinelSpecification;
import com.yxt.starter.sentinel.context.YXTSentinelSpecificationRegister;
import com.yxt.starter.sentinel.io.YxtSentinelConfigLoader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

/**
 * @Author liqiang
 * @Date 2024/8/22 16:46
 */
@Configuration
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class YXTSentinelAutoConfiguration implements BeanPostProcessor {


    @Bean
    public YXTSentinelSpecificationRegister yxtSentinelSpecificationRegister() {
        return new YXTSentinelSpecificationRegister();
    }

    @Bean
    @Order(0)
    public YXTSentinelAspect yxtSentinelAspect() {
        return new YXTSentinelAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler yxtCustomBlockExceptionHandler() {
        return new YXTCustomBlockExceptionHandler();
    }

    @Bean
    @Lazy
    public YXTSentinelContext yxtSentinelContext(YXTSentinelSpecificationRegister yxtSentinelSpecificationRegister) {
        YXTSentinelContext yxtSentinelContext = new YXTSentinelContext();
        List<Class<?>> classes = YxtSentinelConfigLoader.loadYxtSentinelConfig(this.getClass().getClassLoader());
        List<YXTSentinelSpecification> yxtSentinelSpecificationList = yxtSentinelSpecificationRegister.getYxtSentinelSpecificationList();
        if (CollectionUtils.isEmpty(yxtSentinelSpecificationList)) {
            yxtSentinelSpecificationList = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(classes)) {
            YXTSentinelSpecification yxtSentinelSpecification = new YXTSentinelSpecification();
            yxtSentinelSpecification.setName(YXTSentinelConstants.CONTEXT_NAME);
            yxtSentinelSpecification.setConfiguration(classes.toArray(new Class[0]));
            yxtSentinelSpecificationList.add(yxtSentinelSpecification);
        }
        yxtSentinelContext.setConfigurations(yxtSentinelSpecificationList);
        return yxtSentinelContext;
    }
}
