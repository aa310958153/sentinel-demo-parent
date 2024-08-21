package com.example.sentinelnacosdatasourcedemosdk;

import org.springframework.context.annotation.Bean;

/**
 * @Author liqiang
 * @Date 2024/8/21 12:25
 */
public class FeignConfiguration {

    @Bean
    public StudentOpenApiFallback studentOpenApiFallback() {
        return new StudentOpenApiFallback();
    }
}
