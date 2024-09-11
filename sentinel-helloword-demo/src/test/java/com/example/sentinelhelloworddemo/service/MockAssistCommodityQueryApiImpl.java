package com.example.sentinelhelloworddemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:31
 */

public class MockAssistCommodityQueryApiImpl implements AssistCommodityQueryApi {

    public static final ThreadLocal<Date> data = new ThreadLocal<>();

    @Override
    public List<String> queryCommoditySale(List<String> commodityIds) throws IOException, URISyntaxException {
        System.out.println("#433 invoke queryCommoditySale");
        final String config = readResourceFile("ruleconfig.json");
        final JSONObject jsonObject = JSON.parseObject(config);
        //模拟服务异常
        try {
            Thread.sleep(jsonObject.getInteger("sleep"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    private String readResourceFile(String fileName) throws URISyntaxException {
        // 获取文件路径
        URI uri = Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).toURI();
        Path path = Paths.get(uri);

        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }
}
