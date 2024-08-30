package com.example.sentinelapp1demo.config;

import com.example.sentinelapp1demo.service.UserServiceImplFallback;
import org.springframework.context.annotation.Bean;

/**
 * @Author liqiang
 * @Date 2024/8/21 12:25
 */
public class FeignConfiguration {

    @Bean
    public UserServiceImplFallback userServiceImplFallback() {
        return new UserServiceImplFallback();
    }

}
