package com.example.sentinelhelloworddemo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.example.sentinelhelloworddemo.service.AssistCommodityAdapter;
import com.example.sentinelhelloworddemo.service.AssistCommodityQueryApi;
import com.example.sentinelhelloworddemo.service.MockAssistCommodityQueryApiImpl;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import javax.annotation.Resource;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootApplication
public class SentinelHellowordDemoApplicationTests {
    public static void main(String[] args) {
        SpringApplication.run(SentinelHellowordDemoApplicationTests.class, args);
    }

}
