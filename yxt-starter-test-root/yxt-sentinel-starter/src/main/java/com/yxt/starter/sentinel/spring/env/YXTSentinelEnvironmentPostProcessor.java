package com.yxt.starter.sentinel.spring.env;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 自动装配sentinel环境配置
 *
 * @Author liqiang
 * @Date 2024/8/27 10:19
 */
public class YXTSentinelEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(YXTSentinelEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //针对web容器才加载
        if (WebApplicationType.NONE.equals(application.getWebApplicationType())) {
            return;
        }
        //加载yxt-sentinel环境变量
        loadYxtSentinelEnv(environment);
        //加载sentinel环境变量
        loadSentinelEnv(environment);

    }

    private void loadYxtSentinelEnv(ConfigurableEnvironment environment) {
        String fileName = "yxtsentinel-%s.yml";
        loadSentinelEnv("yxtSentinelPropertySources", fileName, environment);
    }

    private void loadSentinelEnv(ConfigurableEnvironment environment) {
        String fileName = "sentinel-%s.yml";
        loadSentinelEnv("sentinelPropertySources", fileName, environment);
    }

    private void loadSentinelEnv(String sourceName, String fileName, ConfigurableEnvironment environment) {
        PropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length <= 0) {
            logger.warn("load yxtsentinel env skip spring  activeProfiles  is null ");
            return;
        }

        CompositePropertySource compositePropertySource = new CompositePropertySource(sourceName);
        for (String activeProfile : activeProfiles) {
            String file = String.format(fileName, activeProfile);
            final Resource resource = resourceLoader.getResource(String.format("classpath:/%s", file));
            try {
                final List<PropertySource<?>> envList = propertySourceLoader.load(file, resource);
                envList.forEach(compositePropertySource::addPropertySource);
                environment.getPropertySources().addLast(compositePropertySource);
            } catch (Exception e) {
                logger.error("load yxtsentinel env [{}] error", file, e);
            }
        }
    }

    @Override
    public int getOrder() {
        return 12;
    }
}
