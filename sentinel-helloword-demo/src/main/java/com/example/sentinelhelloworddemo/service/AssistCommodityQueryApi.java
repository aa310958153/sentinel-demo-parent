package com.example.sentinelhelloworddemo.service;

import java.io.IOException;
import java.util.List;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:29
 */
public interface  AssistCommodityQueryApi {
    List<String> queryCommoditySale(List<String> commodityIds) throws IOException;
}
