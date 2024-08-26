package com.yxt.sentinel.starter.locator;

import org.springframework.core.env.MapPropertySource;

import javax.crypto.Mac;
import java.util.Map;

public class YxtMapPropertySource extends MapPropertySource {
    public YxtMapPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
}
