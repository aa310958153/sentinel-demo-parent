package com.example.sentinelapp1demo.demos.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowArgument;
import com.example.sentinelapp1demo.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liqiang
 * @Date 2024/8/17 16:01
 */
@Controller
public class SentinelBlockTestController {
    @Resource
    UserService userService;
    @RequestMapping("/flow-rule-qps")
    @ResponseBody
    public  Map<String, Object> flowRuleQps() {
        Map<String, Object> resData=new HashMap<>();
        resData.put("status",200);
        resData.put("msg","操作成功");
        return resData;
    }

    /**
     * 线程数和qps区别 qps是一秒内允许的流量，如果设置10就是10.
     * 线程是1秒内允许执行的线程数量设置10则不代表qps是10.如果接口性能是100ms 那么一个线程一秒能执行10个请求 10个线程则是100个qps
     * @param name
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-thread")
    @ResponseBody
    public  Map<String, Object> flowRuleThread(@RequestParam(name = "name", defaultValue = "unknown user") String name)
        throws InterruptedException {
        Thread.sleep(500);
        Map<String, Object> resData=new HashMap<>();
        resData.put("status",200);
        resData.put("msg","操作成功");
        return resData;
    }

    /**
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-relation1")
    @ResponseBody
    public  Map<String, Object> flowRuleRelation(@RequestParam(value = "type",required = false) String type)
        throws InterruptedException {
        Map<String, Object> resData=new HashMap<>();
        resData.put("status",200);
        resData.put("msg","操作成功");
        if(!StringUtils.isEmpty(type)){
            userService.flowRuleRelationQuery();
        }
        return resData;
    }



    /**
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/flow-rule-warmUp")
    @ResponseBody
    public  Map<String, Object> flowRuleWarmUp()
        throws InterruptedException {
        Map<String, Object> resData=new HashMap<>();
        resData.put("status",200);
        resData.put("msg","操作成功");
        return resData;
    }


}
