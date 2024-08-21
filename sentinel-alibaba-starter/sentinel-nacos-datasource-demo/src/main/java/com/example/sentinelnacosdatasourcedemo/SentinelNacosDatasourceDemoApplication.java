package com.example.sentinelnacosdatasourcedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.sentinelnacosdatasourcedemosdk"})
public class SentinelNacosDatasourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelNacosDatasourceDemoApplication.class, args);
    }

}
