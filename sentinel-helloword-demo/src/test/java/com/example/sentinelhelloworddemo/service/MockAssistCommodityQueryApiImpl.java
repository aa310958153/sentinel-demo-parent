package com.example.sentinelhelloworddemo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:31
 */

public class MockAssistCommodityQueryApiImpl  implements AssistCommodityQueryApi{
    public static final ThreadLocal<Date> data = new ThreadLocal<>();
    @Override
    public List<String> queryCommoditySale(List<String> commodityIds ) {
            Date date = MockAssistCommodityQueryApiImpl.data.get();
            System.out.println("#433 invoke queryCommoditySale");
            //结束熔断降级 模拟服务恢复
            if (date.before(new Date())) {
                return new ArrayList<>();
            }
            //模拟服务异常
            try {
                Thread.sleep(7100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
             return new ArrayList<>();
    }
}
