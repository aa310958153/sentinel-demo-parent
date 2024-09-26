package com.example.sentinelapp1demo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.sentinelnacosdatasourcedemosdk"})
public class SentinelStarterDemoApplication {

    public static void main(String[] args) {
        // 获取RuntimeMXBean实例
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

        // 获取JVM启动参数
        List<String> inputArguments = runtimeMxBean.getInputArguments();

        // 输出JVM启动参数
        System.out.println("JVM启动参数:");
        for (String arg : inputArguments) {
            System.out.println(arg);
        }
        SpringApplication.run(SentinelStarterDemoApplication.class, args);
    }

}
