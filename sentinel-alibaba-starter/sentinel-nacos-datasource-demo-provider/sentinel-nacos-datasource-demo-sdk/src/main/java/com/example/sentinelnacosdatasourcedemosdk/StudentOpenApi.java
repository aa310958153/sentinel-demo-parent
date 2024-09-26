package com.example.sentinelnacosdatasourcedemosdk;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author liqiang
 * @Date 2024/8/20 21:38
 */
@RequestMapping({"open-sdk/studnet/r"})
@FeignClient(name = "demo-provider", fallback = StudentOpenApiFallback.class, configuration = FeignConfiguration.class)
public interface StudentOpenApi {

    @RequestMapping("getStudent")
    Student getStudent(@RequestParam(name = "name") String name);
}
