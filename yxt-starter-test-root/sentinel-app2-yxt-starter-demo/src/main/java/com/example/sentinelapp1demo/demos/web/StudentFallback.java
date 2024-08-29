package com.example.sentinelapp1demo.demos.web;

import com.example.sentinelapp1demo.opensdk.StudentControllerOpenApi;

/**
 * @Author liqiang
 * @Date 2024/8/29 15:08
 */
public class StudentFallback implements StudentControllerOpenApi {

    @Override
    public User user(Long id) {
        User user = new User();
        user.setName("我是降级返回");
        user.setAge(id.intValue());
        return user;
    }
}
