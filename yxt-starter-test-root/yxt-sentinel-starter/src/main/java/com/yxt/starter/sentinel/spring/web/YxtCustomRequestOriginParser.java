package com.yxt.starter.sentinel.spring.web;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerMapping;

/**
 * 来源解析器
 *
 * @Author liqiang
 * @Date 2024/09/26 13:56
 */
public class YxtCustomRequestOriginParser implements RequestOriginParser {

    private static final Logger logger = LoggerFactory.getLogger(
        YxtCustomRequestOriginParser.class);
    @Resource
    private SentinelWebMvcConfig config;

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
        String resourceName = getResourceName(request);

        //只有配置了来源限流，做来源访问计数统计，防止统计规则占用大量内存
        boolean hasAuthorityRule = hasAuthorityRule(resourceName, origin);
        boolean hasFlowRule = hasFlowRule(resourceName, origin);
        if (!hasAuthorityRule && !hasFlowRule) {
            return Optional.empty();
        }
        return Optional.of(origin);
    }


    protected String getResourceName(HttpServletRequest request) {
        // 获取请求Url
        Object resourceNameObject = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (resourceNameObject == null || !(resourceNameObject instanceof String)) {
            return null;
        }
        String resourceName = (String) resourceNameObject;
        //对Url做处理的扩展
        UrlCleaner urlCleaner = config.getUrlCleaner();
        if (urlCleaner != null) {
            resourceName = urlCleaner.clean(resourceName);
        }
        // 是否根据方法做区分
        if (StringUtil.isNotEmpty(resourceName) && config.isHttpMethodSpecify()) {
            resourceName = request.getMethod().toUpperCase() + ":" + resourceName;
        }
        return resourceName;
    }


    private boolean hasAuthorityRule(String resourceName, String origin) {
        try {
            if (StringUtils.isBlank(origin)) {
                return false;
            }
            if (!AuthorityRuleManager.hasConfig(resourceName)) {
                return false;
            }
            List<AuthorityRule> authorityRuleList = AuthorityRuleManager.getRules();
            if (CollectionUtils.isEmpty(authorityRuleList)) {
                return false;
            }
            return authorityRuleList.stream().anyMatch(c -> origin.equals(c.getLimitApp()));
        } catch (Exception e) {
            logger.error("#89 hasAuthorityRule error resourceName={}，origin={}", resourceName, origin, e);
        }
        return false;
    }

    private boolean hasFlowRule(String resourceName, String origin) {
        try {
            if (StringUtils.isBlank(origin)) {
                return false;
            }
            if (!FlowRuleManager.hasConfig(resourceName)) {
                return false;
            }
            List<FlowRule> flowRuleList = FlowRuleManager.getRules();
            if (CollectionUtils.isEmpty(flowRuleList)) {
                return false;
            }
            return flowRuleList.stream().anyMatch(c -> origin.equals(c.getLimitApp()));
        } catch (Exception e) {
            logger.error("#118 hasFlowRule error resourceName={}，origin={}", resourceName, origin, e);
        }
        return false;
    }
}
