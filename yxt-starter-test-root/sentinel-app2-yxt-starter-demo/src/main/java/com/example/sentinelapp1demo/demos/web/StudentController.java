package com.example.sentinelapp1demo.demos.web;

import com.example.sentinelapp1demo.opensdk.StudentControllerOpenApi;
import com.yxt.starter.sentinel.annotation.YxtSentinel;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liqiang
 * @Date 2024/8/29 15:07
 */
@RestController
@YxtSentinel(configFallbackClass = StudentFallback.class)
public class StudentController implements StudentControllerOpenApi {

    @Override
    public User user(Long id) {
        User user = new User();
        user.setAge(id.intValue());
        user.setName("c测试环境");
        return user;
    }
}
