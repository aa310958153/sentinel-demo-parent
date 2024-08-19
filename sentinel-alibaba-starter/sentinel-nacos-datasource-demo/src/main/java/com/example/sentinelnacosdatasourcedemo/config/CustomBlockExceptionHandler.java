package com.example.sentinelnacosdatasourcedemo.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

/**
 * @Author liqiang
 * @Date 2024/8/17 10:09
 */
@Component
public class CustomBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e)
        throws Exception {
        Map<String, Object> resData=new HashMap<>();
        if (e instanceof FlowException) {
            resData.put("status",100);
            resData.put("msg","触发限流规则|"+Thread.currentThread().getId()+"|"+ DateFormatUtils.format(System.currentTimeMillis()
                , "yyyy-MM-dd HH:mm:ss:SSS"));

        } else if (e instanceof DegradeException) {
            resData.put("status",101);
            resData.put("msg","降级了|" +Thread.currentThread().getId()+"|"+ DateFormatUtils.format(System.currentTimeMillis()
                , "yyyy-MM-dd HH:mm:ss:SSS")
);

        } else if (e instanceof ParamFlowException) {
            resData.put("status",102);
            resData.put("msg","热点参数限流"+Thread.currentThread().getId()+"|"+ DateFormatUtils.format(System.currentTimeMillis()
                , "yyyy-MM-dd HH:mm:ss:SSS")
);
        } else if (e instanceof SystemBlockException) {
            resData.put("status",103);
            resData.put("msg","系统规则（负载/...不满足要求"+Thread.currentThread().getId()+"|"+ DateFormatUtils.format(System.currentTimeMillis()
                , "yyyy-MM-dd HH:mm:ss:SSS")
);
        } else if (e instanceof AuthorityException) {
            resData.put("status",104);
            resData.put("msg","授权规则不通过"+Thread.currentThread().getId());
        }
        // http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        new ObjectMapper()
            .writeValue(
                httpServletResponse.getWriter(),
                resData
            );
    }
}
