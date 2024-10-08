
package com.example.sentinelhelloworddemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

/**
 * @author jialiang.linjl
 */
public class SystemGuardDemo {

    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();
    private static AtomicInteger total = new AtomicInteger();

    private static volatile boolean stop = false;
    private static final int threadCount = 100;

    private static int seconds = 60 + 40;

    public static void main(String[] args) throws Exception {

        //指标输出
        tick();
        //初始化系统规则
        initSystemRule();

        for (int i = 0; i < threadCount; i++) {
            Thread entryThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Entry entry = null;
                        try {
                            //模拟入口流量
                            entry = SphU.entry("methodA", EntryType.IN);
                            //通过请求
                            pass.incrementAndGet();
                            try {
                                //线程暂停20毫秒
                                TimeUnit.MILLISECONDS.sleep(1000);
                            } catch (InterruptedException e) {
                                // ignore
                            }
                        } catch (BlockException e1) {
                            //降级后拒绝请求+1
                            block.incrementAndGet();
                            try {
                                //等待20毫秒
                                TimeUnit.MILLISECONDS.sleep(20);
                            } catch (InterruptedException e) {
                                // ignore
                            }
                        } catch (Exception e2) {
                            // biz exception
                        } finally {
                            //总请求数+1
                            total.incrementAndGet();
                            //模拟你入口流量退出
                            if (entry != null) {
                                entry.exit();
                            }
                        }
                    }
                }

            });
            entryThread.setName("working-thread");
            entryThread.start();
        }
    }


    /**
     * 多个参数香相互隔离的，如果其中一个满足条件 则会减少流量
     * 根据监控大盘观察调整
     * 一个请求的处理时间=排队时间+处理时间
     */
    private static void initSystemRule() {
        SystemRule rule = new SystemRule();
        // max load is 3
       // rule.setHighestSystemLoad(3.0);
        // max cpu usage is 60%
         // rule.setHighestCpuUsage(0.6);
        /**
         * 平均RT 如果超过这个RT则会减少流量
         *  比如当系统正常情况平均RT是1000ms,当超过2000，标识开始有排队请求，则防止进一步恶化，设置此阈值
         */
        //rule.setAvgRt(2100);
        // max total qps is 20
        rule.setQps(30);
        // max parallel working thread is 10
        /**
         * 仅允许的最大入口线程数量,比如http连接池的线程 比如允许最大处理中的线程数量是100,如果超过100则会减少流量
         * 但是不应该设置跟http连接池一样，比如连接池设置100,RT是1，标识每秒能够处理100个请求，但是我们允许一定时间范围为内的排队等待 比如3秒 我们则*3
         */
        rule.setMaxThread(10);

        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }

    private static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }

    static class TimerTask implements Runnable {
        @Override
        public void run() {
            System.out.println("begin to statistic!!!");
            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;
            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                //结束输出倒计时,当前时间,总流量,通过流量，熔断流量
                System.out.println(seconds + ", " + TimeUtil.currentTimeMillis() + ", total:"
                    + oneSecondTotal + ", pass:"
                    + oneSecondPass + ", block:" + oneSecondBlock);
                //100次后退出
                if (seconds-- <= 0) {
                    stop = true;
                }
            }
            System.exit(0);
        }
    }
}