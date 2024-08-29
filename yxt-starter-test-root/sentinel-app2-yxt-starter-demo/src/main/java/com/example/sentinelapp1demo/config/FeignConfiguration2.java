package com.example.sentinelapp1demo.config;

import com.example.sentinelapp1demo.demos.web.SentinelBlockTestController;
import com.example.sentinelapp1demo.demos.web.UserControllerFallBack;
import org.springframework.context.annotation.Bean;

/**
 * @Author liqiang
 * @Date 2024/8/21 12:25
 */
public class FeignConfiguration2 {

    @Bean
    public SentinelBlockTestController userServiceImplFallback() {
        return new SentinelBlockTestController();
    }

    @Bean
    public UserControllerFallBack userControllerFallBack() {
        return new UserControllerFallBack();
    }
}
