package com.example.sentinelhelloworddemo;

import com.alibaba.csp.sentinel.slots.statistic.metric.ArrayMetric;
import com.alibaba.csp.sentinel.slots.statistic.metric.Metric;
import org.junit.Test;

/**
 * @Author: qiang.li
 * @Date: 2024/10/24 15:39
 * @Description: 滑动窗口测试
 */
public class MetricTest {

    //窗口长度
    private int sampleCount = 60;

    //每个长度窗口期
    private int intervalInMs = 60 * 1000 * 60;


    private transient Metric rollingCounterInMinute = new ArrayMetric(sampleCount, intervalInMs, false);

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 130; i++) {
            Thread.sleep(1000);
            rollingCounterInMinute.addBlock(1);
        }
        System.out.println(rollingCounterInMinute.block());
    }

    @Test
    public void test_CalculateTimeIdx() throws InterruptedException {
        //计算每个窗口占用多少ms
        int windowLengthInMs = intervalInMs / sampleCount;
        long startTime = System.currentTimeMillis(); // 获取当前时间戳
        for (int i = 0; i < 130; i++) {
            long timeMillis = startTime + i * 60000; // 每次滚动一分钟
            long timeId = timeMillis / windowLengthInMs;
            // Calculate current index so we can map the timestamp to the leap array.
            int index = (int) (timeId % sampleCount);
            System.out.println(index);
            assert index >= 0 && index <= sampleCount;
        }

    }
}
