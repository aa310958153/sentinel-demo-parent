package com.example.sentinelhelloworddemo;

import com.alibaba.csp.sentinel.slots.statistic.metric.ArrayMetric;
import com.alibaba.csp.sentinel.slots.statistic.metric.Metric;
import org.junit.Test;

/**
 * @Author: qiang.li
 * @Date: 2024/10/24 15:39
 * @Description: 滑动窗口测试 sentinel 很多统计都是基于滑动窗口统计的，原理是将时间戳映射到数组的索引上，然后进行累加(保证了原子性)，比如下面的例子 窗口长度为 60 * 1000 * 60/60
 */
public class MetricTest {

    //窗口长度
    private int sampleCount = 60;

    //滑动窗口时间  每个窗口的时间间隔为intervalInMs/sampleCount
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
