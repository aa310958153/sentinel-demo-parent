package com.example.sentinelhelloworddemo.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker.State;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.slots.block.flow.ClusterFlowConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.example.sentinelhelloworddemo.SentinelHellowordDemoApplicationTests;
import com.example.sentinelhelloworddemo.constans.SentinelConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SentinelHellowordDemoApplicationTests.class)
public class AssistCommodityAdapterTest {

    @Resource
    private AssistCommodityAdapter adapter;

    /**
     * 初始化流控规则 [ { "app": "assist-core-toolkit-local", "clusterConfig": { "fallbackToLocalWhenFail": true,
     * "sampleCount": 10, "strategy": 0, "thresholdType": 0, "windowIntervalMs": 1000 }, "clusterMode": false,
     * "controlBehavior": 0, "count": 1.0, "gmtCreate": 1725876637357, "gmtModified": 1725876637357, "grade": 1, "id":
     * 27, "ip": "10.4.1.125", "limitApp": "default", "port": 8719, "resource":
     * "com.yxt.assist.toolkit.dao.coldchain.monitoring.dao.impl.ColdChainAlarmOrderDaoImpl.pageAlarmOrder", "strategy":
     * 0 } ]
     */
    private static void initFlowRules() {

        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource("HelloWorld");

        ClusterFlowConfig config = new ClusterFlowConfig();
        config.setFallbackToLocalWhenFail(true);
        config.setSampleCount(10);
        config.setThresholdType(0);
        config.setWindowIntervalMs(1000);
        rule1.setClusterConfig(config);
        // set limit qps to 20
        rule1.setCount(1);
        rule1.setStrategy(0);
        rule1.setClusterMode(false);
        rule1.setControlBehavior(0);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * 初始化熔断规则
     */
    private static void initDegradeRule() {
        /**
         * 请求超过7秒的判定为慢请求
         * 在10秒内请求数量大于5同时慢请求超过60%触发熔断
         */
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule r = new DegradeRule(SentinelConstants.RESOURCE_QUERY_COMMODITY_SALE)
            .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
            .setTimeWindow(10)
            .setCount(7000)
            .setSlowRatioThreshold(0.6)
            .setMinRequestAmount(5)
            .setStatIntervalMs(10000);
        rules.add(r);
        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
        //断路器事件监听器
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
            (prevState, newState, rule, snapshotValue) -> {

                if (newState == State.OPEN) {
                    //触发断路器开启
                    // 变换至 OPEN state 时会携带触发时的值
                    System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(),
                        TimeUtil.currentTimeMillis(), snapshotValue));
                } else {
                    //断路器关闭
                    System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(),
                        TimeUtil.currentTimeMillis()));
                }
            });
    }

    /**
     * 配合控制台使用: 文档参考：https://sentinelguard.io/zh-cn/docs/dashboard.html
     */
    @Test
    public void flowRule_TEST() throws InterruptedException {

        // 配置规则.
        initFlowRules();
        Thread.sleep(2000);

        for (int j = 0; j < 2; j++) {
            new Thread(new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    for (int k = 0; k < 30; k++) {
                        // 1.5.0 版本开始可以直接利用 try-with-resources 特性 自动回收调用  entry.exit() 配合注解使用 @SentinelResource("HelloWorld")
                        try (Entry entry = SphU.entry(
                            "HelloWorld",
                            EntryType.IN)) {
                            // 被保护的逻辑
                            System.out.println(Thread.currentThread().getId() + "hello world" + (++i));

                        } catch (BlockException ex) {
                            i++;
                            // 处理被流控的逻辑
                            System.out.println("blocked!");

                        }
                    }

                }
            }).start();

        }
        Thread.sleep(500000000);
    }


    public void testParamFlowRules() throws IOException {
        Entry entry = null;
        try {
            entry = SphU.entry("paramFlowRules", EntryType.IN, 1, "paramA", "paramB");
            // Your logic here.
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
        } finally {
            if (entry != null) {
                entry.exit(1, "paramA", "paramB");
            }
        }
    }

    /**
     * 模拟熔断降级 经过测试熔断降级只会对请求做统计，而不会主动结束线程，比如设置5秒钟为慢请求，执行了10秒，需要等待10秒执行完，进行统计 如果瞬时流量瞬间增加，则会阻塞等待,调用第三方需要设置合理的超时时间
     *
     * @throws InterruptedException
     */
    @Test
    public void degradeRule_TEST() throws InterruptedException {
        initDegradeRule();
        AssistCommodityQueryApi mockAssistCommodityQueryApi = new MockAssistCommodityQueryApiImpl();
        Whitebox.setInternalState(adapter, "assistCommodityQueryApi",
            mockAssistCommodityQueryApi);
        //模拟5个线程并发调用
        int concurrency = 5;
        List<Thread> tasks = new ArrayList<>();
        for (int i = 0; i < concurrency; i++) {
            Thread entryThread = new Thread(() -> {
                while (true) {
                    try {
                        adapter.queryCommoditySale(null);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            entryThread.setName("sentinel-simulate-traffic-task-" + i);
            entryThread.start();
            tasks.add(entryThread);
        }
        for (Thread task : tasks) {
            task.join();
        }
    }

    /**
     * com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowSlot#checkFlow 默认是string.valueof
     *
     * @throws IOException
     */
    public void initParamFlowRules() throws IOException {

        ParamFlowRule rule = new ParamFlowRule("paramFlowRules")
            .setParamIdx(0)
            .setCount(5);
        // 针对 int 类型的参数 PARAM_B，单独设置限流 QPS 阈值为 10，而不是全局的阈值 5.
        ParamFlowItem item = new ParamFlowItem().setObject(String.valueOf("210005"))
            .setClassType(int.class.getName())
            .setCount(10);
        rule.setParamFlowItemList(Collections.singletonList(item));

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }

    /**
     * 资源应用仅允许appA,appB访问 调用方信息通过 ContextUtil.enter(resourceName, origin) 方法中的 origin 参数传入。
     * ContextUtil.enter(resourceName, origin)
     */
    public void intAuthorityRule() {
        AuthorityRule rule = new AuthorityRule();
        rule.setResource("test");
        rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
        rule.setLimitApp("appA,appB");
        AuthorityRuleManager.loadRules(Collections.singletonList(rule));
    }
}
