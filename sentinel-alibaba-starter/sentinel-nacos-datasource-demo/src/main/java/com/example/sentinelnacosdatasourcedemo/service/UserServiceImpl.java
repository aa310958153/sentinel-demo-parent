package com.example.sentinelnacosdatasourcedemo.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.example.sentinelnacosdatasourcedemo.web.User;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @Author liqiang
 * @Date 2024/7/31 17:59
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User findByUser(Long userId){

        // 1.5.0 版本开始可以利用 try-with-resources 特性
        // 资源名可使用任意有业务语义的字符串，比如方法名、接口名或其它可唯一标识的字符串。
        try (Entry entry = SphU.entry("com.example.sentinelapp1demo.service.UserServiceImpl.findByUser")) {
            // 被保护的业务逻辑
            User user= new User();
            user.setAge(13);
            user.setName("李强");
            return user;
        }catch (FlowException ex){
            User user= new User();
            user.setAge(13);
            user.setName("我是限流返回");
            return user;
        }
        catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级
            // 在此处进行相应的处理操作
            User user= new User();
            user.setAge(13);
            user.setName("我是降级返回");
            return user;
        }
    }

    @Override
    public List<User> listByUserName(String userName) {
        User user= new User();
        user.setAge(13);
        user.setName("李强");
        return Collections.singletonList(user);
    }

    @SentinelResource("flowRuleRelationQuery")
    @Override
    public void flowRuleRelationQuery() {

    }

    @Override
    @SentinelResource("flowRuleChain")
    public void flowRuleChain() {

    }


}
