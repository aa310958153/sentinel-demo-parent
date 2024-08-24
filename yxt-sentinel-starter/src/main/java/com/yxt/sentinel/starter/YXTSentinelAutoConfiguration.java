package com.yxt.sentinel.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liqiang
 * @Date 2024/8/22 16:46
 */
@Configuration
public class YXTSentinelAutoConfiguration {

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

}
