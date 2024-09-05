package com.yxt.starter.sentinel;

import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker.State;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 增加断路器日期打印
 *
 * @Author liqiang
 * @Date 2024/8/17 10:09
 */
public class SentinelEventObserverRegistry implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(
        SentinelEventObserverRegistry.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //监听熔断变更
        EventObserverRegistry.getInstance().addStateChangeObserver("stateChange",
            (prevState, newState, rule, snapshotValue) -> {
                if (newState == State.OPEN) {
                    // 变换至 OPEN state 时会携带触发时的值
                    logger.error("#36 Sentinel断路器 resource={},{} -> OPEN at {}, snapshotValue={}",
                        rule.getResource(), prevState.name(),
                        TimeUtil.currentTimeMillis(),
                        snapshotValue);
                } else {
                    logger.error("#41 Sentinel断路器  resource={}, {} -> {} at {}", rule.getResource(),
                        prevState.name(), newState.name(),
                        TimeUtil.currentTimeMillis());
                }
            });
    }
}
