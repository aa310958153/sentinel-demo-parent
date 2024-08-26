package com.yxt.sentinel.starter.locator;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YxtSentinelPropertySourceLocator  implements PropertySourceLocator {

    @Override
    public PropertySource<?> locate(Environment environment) {
//        Map<String, Object> stringObjectMap=new HashMap<>();
//        stringObjectMap.put("server.port","9001");
//        YxtMapPropertySource yxtMapPropertySource=new YxtMapPropertySource("yxt",stringObjectMap);
//        return yxtMapPropertySource;

        PropertySourceLoader propertySourceLoader=new YamlPropertySourceLoader();
        ResourceLoader resourceLoader=new DefaultResourceLoader();
        final Resource resource = resourceLoader.getResource("classpath:/yxtsentinel-dev.yml");
        try {
            final List<PropertySource<?>> load = propertySourceLoader.load("yxtsentinel-dev.yml", resource);
            return load.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
