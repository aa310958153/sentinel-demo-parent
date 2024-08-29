package com.example.sentinelapp1demo.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.sentinelapp1demo.config.FeignConfiguration;
import com.example.sentinelapp1demo.demos.web.User;
import com.yxt.starter.sentinel.annotation.YXTSentinel;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @Author liqiang
 * @Date 2024/7/31 17:59
 */
@Service
@YXTSentinel(fallbackClass = UserServiceImplFallback.class, configuration = FeignConfiguration.class)
public class UserServiceImpl implements UserService {

    @Override
    public User findByUser(Long userId) throws BlockException {

        // 1.5.0 版本开始可以利用 try-with-resources 特性
        // 资源名可使用任意有业务语义的字符串，比如方法名、接口名或其它可唯一标识的字符串。
        try (Entry entry = SphU.entry("com.example.sentinelapp1demo.service.UserServiceImpl.findByUser")) {
            // 被保护的业务逻辑
            User user = new User();
            user.setAge(13);
            user.setName("李强");
            return user;
        } catch (Exception e) {
            throw e;
        }
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
