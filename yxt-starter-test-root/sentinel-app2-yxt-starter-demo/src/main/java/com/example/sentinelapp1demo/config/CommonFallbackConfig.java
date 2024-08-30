package com.example.sentinelapp1demo.config;

import com.example.sentinelapp1demo.demos.web.StudentFallback;
import org.springframework.context.annotation.Bean;

/**
 * @Author liqiang
 * @Date 2024/8/29 16:33
 */
public class CommonFallbackConfig {
    @Bean
    public StudentFallback studentFallback() {
        return new StudentFallback();
    }
}
