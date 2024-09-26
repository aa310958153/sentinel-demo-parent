package com.yxt.starter.sentinel.io;

import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @Author liqiang
 * @Date 2024/8/29 16:15
 */
public class YxtSentinelConfigLoader {

    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/yxtsentinel.config";
    private static final Logger logger = LoggerFactory.getLogger(YxtSentinelConfigLoader.class);

    public static List<Class<?>> loadYxtSentinelConfig(@Nullable ClassLoader classLoader) {
        List<Class<?>> configClassList = new ArrayList<>();
        Map<String, List<String>> configNames = loadYxtSentinelConfigNames(classLoader);
        List<String> classNameList = configNames.get(YxtSentinelConstants.YXT_SENTINEL_CONFIG);
        if (CollectionUtils.isEmpty(classNameList)) {
            return configClassList;
        }
        for (String className : classNameList) {
            try {
                configClassList.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(
                    "Cannot found class" + className, e);
            }
        }

        return configClassList;
    }

    private static Map<String, List<String>> loadYxtSentinelConfigNames(@Nullable ClassLoader classLoader) {

        try {
            Enumeration<URL> urls = (classLoader != null ?
                classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryClassName = ((String) entry.getKey()).trim();
                    for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                        result.add(factoryClassName, factoryName.trim());
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                FACTORIES_RESOURCE_LOCATION + "]", ex);
        }
    }
}
