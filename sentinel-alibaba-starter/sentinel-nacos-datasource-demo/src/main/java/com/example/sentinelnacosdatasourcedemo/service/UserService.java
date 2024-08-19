package com.example.sentinelnacosdatasourcedemo.service;

import com.example.sentinelnacosdatasourcedemo.web.User;
import java.util.List;

/**
 * @Author liqiang
 * @Date 2024/7/31 17:59
 */
public interface UserService{
     User findByUser(Long userId);

     List<User> listByUserName(String userName);

      void flowRuleRelationQuery();

      void flowRuleChain();
}
