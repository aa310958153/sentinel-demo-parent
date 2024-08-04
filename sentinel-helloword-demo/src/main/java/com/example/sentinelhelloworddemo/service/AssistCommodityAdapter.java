package com.example.sentinelhelloworddemo.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.sentinelhelloworddemo.constans.SentinelConstants;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author liqiang
 * @Date 2024/8/1 17:34
 */
@Service
public class AssistCommodityAdapter {

    @Resource
    AssistCommodityQueryApi assistCommodityQueryApi;
    public List<String> queryCommoditySale(List<String> commodityIds) {
        try (Entry entry = SphU.entry(SentinelConstants.RESOURCE_QUERY_COMMODITY_SALE)) {
            List<String> strings = assistCommodityQueryApi.queryCommoditySale(commodityIds);
            return strings;
        } catch (BlockException ex) {
            System.out.println("#633 pageQueryMainCommodity 触发sentinel降级");
            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
