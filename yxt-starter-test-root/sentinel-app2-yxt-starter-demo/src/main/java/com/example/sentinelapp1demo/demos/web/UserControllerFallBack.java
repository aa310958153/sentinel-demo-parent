package com.example.sentinelapp1demo.demos.web;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author liqiang
 * @Date 2024/8/28 18:16
 */
public class UserControllerFallBack extends UserController {

    @Override
    public String helloSentinelResource(String name) {
        return "我是降级返回";
    }

    @Override
    public User user(@PathVariable("id") Long id) throws BlockException {
        User user = new User();
        user.setName("我是降级返回");
        user.setAge(id.intValue());
        return user;
    }
}
