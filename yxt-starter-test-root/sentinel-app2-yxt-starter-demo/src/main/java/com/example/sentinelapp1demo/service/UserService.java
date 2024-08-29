package com.example.sentinelapp1demo.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.sentinelapp1demo.demos.web.User;
import java.util.List;

/**
 * @Author liqiang
 * @Date 2024/7/31 17:59
 */
public interface UserService {

    User findByUser(Long userId) throws BlockException;

    List<User> listByUserName(String userName);

    public void flowRuleRelationQuery();
}
