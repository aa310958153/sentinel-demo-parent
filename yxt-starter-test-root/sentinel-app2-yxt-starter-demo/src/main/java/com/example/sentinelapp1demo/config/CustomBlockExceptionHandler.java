package com.example.sentinelapp1demo.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @Author liqiang
 * @Date 2024/8/17 10:09
 */
public class CustomBlockExceptionHandler implements BlockExceptionHandler {

    @Lazy
    @Resource
    private List<HandlerMapping> handlerMappings;
    @Lazy
    @Resource
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e)
        throws Exception {
        Map<String, Object> resData = new HashMap<>();
        if (e instanceof FlowException) {
            resData.put("status", 100);
            resData.put("msg", "触发限流规则");

        } else if (e instanceof DegradeException) {
            resData.put("status", 101);
            resData.put("msg", "降级了");

        } else if (e instanceof ParamFlowException) {
            resData.put("status", 102);
            resData.put("msg", "热点参数限流");
        } else if (e instanceof SystemBlockException) {
            resData.put("status", 103);
            resData.put("msg", "系统规则（负载/...不满足要求");
        } else if (e instanceof AuthorityException) {
            resData.put("code", "999001");
            resData.put("msg", "授权规则不通过");
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

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
            "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }


}
