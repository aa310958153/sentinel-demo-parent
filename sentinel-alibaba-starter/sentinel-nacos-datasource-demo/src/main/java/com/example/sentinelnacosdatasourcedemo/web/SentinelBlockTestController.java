package com.example.sentinelnacosdatasourcedemo.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.example.sentinelnacosdatasourcedemo.service.UserService;
import com.example.sentinelnacosdatasourcedemosdk.Student;
import com.example.sentinelnacosdatasourcedemosdk.StudentOpenApi;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liqiang
 * @Date 2024/8/17 16:01
 */
@Controller
public class SentinelBlockTestController implements ApplicationRunner {

    @Resource
    StudentOpenApi studentOpenApi;
    @Resource
    UserService userService;

    /**
     * 流控qps
     *
     * @return
     */
    @RequestMapping("/flow-rule-qps")
    @ResponseBody
    public Map<String, Object> flowRuleQps() {
        return getResData();
    }

    /**
     * 流控并发线程 线程数和qps区别 qps是一秒内允许的流量，如果设置10就是10. 线程是1秒内允许执行的线程数量设置10则不代表qps是10.如果接口性能是100ms 那么一个线程一秒能执行10个请求
     * 10个线程则是100个qps
     *
     * @param name
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-thread")
    @ResponseBody
    public Map<String, Object> flowRuleThread(@RequestParam(name = "name", defaultValue = "unknown user") String name)
        throws InterruptedException {
        Thread.sleep(500);
        return getResData();
    }

    /**
     * 流控关联
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-relation1")
    @ResponseBody
    public Map<String, Object> flowRuleRelation(@RequestParam(value = "type", required = false) String type)
        throws InterruptedException {
        if (StringUtils.isNotBlank(type)) {
            userService.flowRuleRelationQuery();
        }
        return getResData();
    }

    /**
     * spring cloud: sentinel: web-context-unify: false # 关闭context整合 避免链路失效 默认会由context为根链路
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-chain1")
    @ResponseBody
    public Map<String, Object> flowRuleChain() throws InterruptedException {
        userService.flowRuleChain();
        return getResData();
    }

    @RequestMapping("/flow-rule-chain2")
    @ResponseBody
    public Map<String, Object> flowRuleChain2() {
        userService.flowRuleChain();
        return getResData();
    }


    /**
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-warmUp")
    @ResponseBody
    public Map<String, Object> flowRuleWarmUp()
        throws InterruptedException {
        return getResData();
    }

    /**
     * 流控热点参数
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-params-string")
    @SentinelResource("flowRuleParamsString")
    @ResponseBody
    public Map<String, Object> flowRuleParams(@RequestParam(value = "name", required = false) String name)
        throws InterruptedException {
        return getResData();
    }

    /**
     * 流控热点参数 com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowSlot#checkFlow
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-params-object")
    @SentinelResource("flowRuleParamsObject")
    @ResponseBody
    public Map<String, Object> flowRuleParams(@RequestBody User user)
        throws InterruptedException {
        return getResData();
    }

    /**
     * com.example.sentinelnacosdatasourcedemo.config.CustomRequestOriginParser
     *
     * @return
     */
    @RequestMapping("/authority-rule")
    @ResponseBody
    public Map<String, Object> authorityRule() {
        return getResData();
    }

    @RequestMapping("/system-rule-rq")
    @ResponseBody
    public Map<String, Object> systemRuleRt() {
        return getResData();
    }

    @RequestMapping("/system-rule-rt")
    @ResponseBody
    public Map<String, Object> systemRuleRt(@RequestParam(value = "seconds", required = false) Integer seconds)
        throws InterruptedException {
        Thread.sleep(seconds);
        return getResData();
    }

    @RequestMapping("/feign-rule-rq")
    @ResponseBody
    public Student feginRuleRq()
        throws InterruptedException {
        return studentOpenApi.getStudent("测试");
    }


    private Map<String, Object> getResData() {
        Map<String, Object> resData = new HashMap<>();
        resData.put("status", 200);
        resData.put("msg",
            "操作成功|" + Thread.currentThread().getId() + "|" + DateFormatUtils.format(System.currentTimeMillis()
                , "yyyy-MM-dd HH:mm:ss:SSS"));
        return resData;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initParamFlowRules();
    }

    /**
     * com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowSlot#checkFlow 默认是string.valueof dashborad有bug先写死
     *
     * @throws IOException
     */
    public void initParamFlowRules() throws IOException {

        ParamFlowRule rule = new ParamFlowRule("flowRuleParamsString")
            .setParamIdx(0)
            .setCount(20);//总阀20 按各个参数分别统计
        // 针对 String 类型的参数,参数值为李强 单独设置限流 QPS 阈值为 11，而不是全局的阈值 20.
        ParamFlowItem item = new ParamFlowItem().setObject(String.valueOf("李强"))
            .setClassType(String.class.getName())
            .setCount(1);
        rule.setParamFlowItemList(Collections.singletonList(item));

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }


}
