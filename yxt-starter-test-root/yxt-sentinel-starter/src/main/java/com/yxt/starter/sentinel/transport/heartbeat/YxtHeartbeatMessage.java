package com.yxt.starter.sentinel.transport.heartbeat;

import com.alibaba.csp.sentinel.transport.heartbeat.HeartbeatMessage;
import com.alibaba.fastjson.JSON;
import com.yxt.starter.sentinel.YxtSentinelProperties;
import com.yxt.starter.sentinel.spring.SpringContextUtil;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @Author: qiang.li
 * @Date: 2024/9/29 11:17
 * @Description: 与dashboard通信的message
 */
public class YxtHeartbeatMessage extends HeartbeatMessage {

    private static final Logger logger = LoggerFactory.getLogger(
        YxtHeartbeatMessage.class);

    @Override
    public Map<String, String> generateCurrentMessage() {
        Map<String, String> heartbeatMessage = super.generateCurrentMessage();
        //增加auth配置
        setYxtAuthConfig(heartbeatMessage);
        return heartbeatMessage;
    }

    /**
     * 设置授权相关配置
     */
    public void setYxtAuthConfig(Map<String, String> heartbeatMessage) {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        if (applicationContext == null) {
            logger.warn("#31 YxtHeartbeatMessage setYxtAuthConfig fail applicationContext is null");
            return;
        }
        YxtSentinelProperties yxtSentinelProperties = applicationContext.getBean(YxtSentinelProperties.class);

        if (yxtSentinelProperties.getAuth() == null) {
            return;
        }
        heartbeatMessage.put("auth", JSON.toJSONString(yxtSentinelProperties.getAuth()));


    }


}
