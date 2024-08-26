package com.example.sentinelapp1demo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SentinelApp2DemoApplication {

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
        SpringApplication.run(SentinelApp2DemoApplication.class, args);
    }

}
