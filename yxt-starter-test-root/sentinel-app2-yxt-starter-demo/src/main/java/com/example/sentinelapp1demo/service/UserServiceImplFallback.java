package com.example.sentinelapp1demo.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.sentinelapp1demo.demos.web.User;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

/**
 * @Author liqiang
 * @Date 2024/7/31 17:59
 */

public class UserServiceImplFallback implements UserService {

    @Resource
    private UserService userService;

    @Override
    public User findByUser(Long userId) {
        User user = new User();
        user.setAge(13);
        user.setName("我是降级返回");
        return user;
    }

    @Override
    public List<User> listByUserName(String userName) {
        User user = new User();
        user.setAge(13);
        user.setName("李强");
        return Collections.singletonList(user);
    }

    @SentinelResource("flowRuleRelationQuery")
    @Override
    public void flowRuleRelationQuery() {

    }


}
