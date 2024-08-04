package com.example.sentinelhelloworddemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sentinelhelloworddemo.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:31
 */

public class MockAssistCommodityQueryApiImpl implements AssistCommodityQueryApi {
    public static final ThreadLocal<Date> data = new ThreadLocal<>();

    @Override
    public List<String> queryCommoditySale(List<String> commodityIds) throws IOException {
        System.out.println("#433 invoke queryCommoditySale");
        final String config = FileUtil.readFile("/Users/liqiang/Desktop/MyGithubProject/sentinel-demo-parent/sentinel-helloword-demo/src/test/resources/ruleconfig.json");
        final JSONObject jsonObject = JSON.parseObject(config);
        //模拟服务异常
        try {
            Thread.sleep(jsonObject.getInteger("sleep"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }
}
