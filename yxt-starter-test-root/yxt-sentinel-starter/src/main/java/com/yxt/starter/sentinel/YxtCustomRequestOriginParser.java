package com.yxt.starter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 来源解析器
 *
 * @Author liqiang
 * @Date 2024/09/26 13:56
 */
public class YxtCustomRequestOriginParser implements RequestOriginParser {


    @Override
    public String parseOrigin(HttpServletRequest request) {
        Optional<String> originOpt = getOrigin(request);
        return originOpt.orElse(null);
    }


    /**
     * 获取来源标识 针对服务间调用有serviceName，针对外部调用则获取调用方ip
     *
     * @param request
     * @return
     */
    public Optional<String> getOrigin(HttpServletRequest request) {

        String clientIp = request.getHeader(YxtSentinelConstants.HEAD_CLIENT_IP_KEY);
        String serviceName = request.getHeader(YxtSentinelConstants.HEAD_SERVICE_NAME_KEY);
        String origin = StringUtils.isBlank(serviceName) ? clientIp : serviceName;
        if (StringUtils.isBlank(origin)) {
            return Optional.empty();
        }
        return Optional.of(origin);
    }
}
