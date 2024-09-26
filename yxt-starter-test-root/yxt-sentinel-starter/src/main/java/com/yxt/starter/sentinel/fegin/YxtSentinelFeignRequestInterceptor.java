package com.yxt.starter.sentinel.fegin;

import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * feign拦截器 透传serviceName 用于来源限流
 *
 * @Author liqiang
 */
public class YxtSentinelFeignRequestInterceptor implements RequestInterceptor, EnvironmentAware {


    private Environment environment;

    @Override
    public void apply(RequestTemplate template) {
        template.header(YxtSentinelConstants.HEAD_SERVICE_NAME_KEY, environment.getProperty("spring.application.name"));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
