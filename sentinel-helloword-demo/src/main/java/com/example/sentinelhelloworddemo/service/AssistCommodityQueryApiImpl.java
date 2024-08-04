package com.example.sentinelhelloworddemo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:30
 */
@Service
public class AssistCommodityQueryApiImpl implements AssistCommodityQueryApi{

    @Override
    public List<String> queryCommoditySale(List<String> commodityIds) {
        //正常这里是调用外部接口获取数据
        return Collections.singletonList("");
    }
}
