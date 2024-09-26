package com.example.sentinelapp1demo.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 自定义请求来源解析器
 *
 * @Author liqiang
 * @Date 2024/8/18 21:31
 */
@Component
public class CustomRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        //根据url 的origin作为来源
        return request.getParameter("origin");
    }
}
