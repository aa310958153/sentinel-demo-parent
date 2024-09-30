package com.yxt.starter.sentinel.transport.heartbeat;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.transport.HeartbeatSender;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.transport.heartbeat.client.SimpleHttpClient;
import com.alibaba.csp.sentinel.transport.heartbeat.client.SimpleHttpRequest;
import com.alibaba.csp.sentinel.transport.heartbeat.client.SimpleHttpResponse;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: qiang.li
 * @Date: 2024/9/29 11:15
 * @Description: 重写发送心跳实现，增加权限相关信息上报
 */
public class YxtHttpHeartbeatSender implements HeartbeatSender {

    private static final int OK_STATUS = 200;

    private static final long DEFAULT_INTERVAL = 1000 * 10;

    private final YxtHeartbeatMessage heartBeat = new YxtHeartbeatMessage();
    private final SimpleHttpClient httpClient = new SimpleHttpClient();

    private final List<Tuple2<String, Integer>> addressList;

    private int currentAddressIdx = 0;

    public YxtHttpHeartbeatSender() {
        // Retrieve the list of default addresses.
        List<Tuple2<String, Integer>> newAddrs = TransportConfig.getConsoleServerList();
        if (newAddrs.isEmpty()) {
            RecordLog.warn("[SimpleHttpHeartbeatSender] Dashboard server address not configured or not available");
        } else {
            RecordLog.info("[SimpleHttpHeartbeatSender] Default console address list retrieved: " + newAddrs);
        }
        this.addressList = newAddrs;
    }

    @Override
    public boolean sendHeartbeat() throws Exception {
        //是否有接入dashboard
        if (TransportConfig.getRuntimePort() <= 0) {
            RecordLog.info("[SimpleHttpHeartbeatSender] Command server port not initialized, won't send heartbeat");
            return false;
        }
        //获取dashboard配置信息 key为ip value为端口
        Tuple2<String, Integer> addrInfo = getAvailableAddress();
        if (addrInfo == null) {
            return false;
        }

        //构建请求地址
        InetSocketAddress addr = new InetSocketAddress(addrInfo.r1, addrInfo.r2);
        //发送心跳默认/registry/machine
        SimpleHttpRequest request = new SimpleHttpRequest(addr, TransportConfig.getHeartbeatApiPath());
        //构建请求参数
        request.setParams(heartBeat.generateCurrentMessage());
        try {
            SimpleHttpResponse response = httpClient.post(request);
            if (response.getStatusCode() == OK_STATUS) {
                return true;
            } else if (clientErrorCode(response.getStatusCode()) || serverErrorCode(response.getStatusCode())) {
                RecordLog.warn("[SimpleHttpHeartbeatSender] Failed to send heartbeat to " + addr
                    + ", http status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            RecordLog.warn("[SimpleHttpHeartbeatSender] Failed to send heartbeat to " + addr, e);
        }
        return false;
    }

    @Override
    public long intervalMs() {
        return DEFAULT_INTERVAL;
    }

    private Tuple2<String, Integer> getAvailableAddress() {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        if (currentAddressIdx < 0) {
            currentAddressIdx = 0;
        }
        int index = currentAddressIdx % addressList.size();
        return addressList.get(index);
    }

    private boolean clientErrorCode(int code) {
        return code > 399 && code < 500;
    }

    private boolean serverErrorCode(int code) {
        return code > 499 && code < 600;
    }
}
