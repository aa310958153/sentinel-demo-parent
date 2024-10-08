package com.yxt.starter.sentinel.spring.web;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxt.starter.sentinel.annotation.YxtSentinel;
import com.yxt.starter.sentinel.aspectj.AbstractYxtSentinelAspectSupport;
import com.yxt.starter.sentinel.aspectj.MethodWrapper;
import com.yxt.starter.sentinel.aspectj.YxtSentinelMetadataRegistry;
import com.yxt.starter.sentinel.constants.YxtSentinelConstants;
import com.yxt.starter.sentinel.context.YxtSentinelContext;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

/**
 * 一心堂的自定义流控处理器
 *
 * @Author liqiang
 * @Date 2024/8/17 10:09
 */

public class YxtCustomBlockExceptionHandler extends AbstractYxtSentinelAspectSupport implements BlockExceptionHandler,
    SmartInitializingSingleton,
    ApplicationContextAware {


    private static final Logger logger = LoggerFactory.getLogger(
        YxtCustomBlockExceptionHandler.class);
    @Resource
    @Lazy
    private YxtSentinelContext yxtSentinelContext;

    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    private ApplicationContext applicationContext;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        BlockException blockException)
        throws Exception {
        try {
            Optional<HandlerExecutionChain> handlerExecutionChainOpt = getFallbackHandle(httpServletRequest);
            if (handlerExecutionChainOpt.isPresent()) {
                Object handler = handlerExecutionChainOpt.get().getHandler();
                Optional<HandlerAdapter> handlerAdapterOpt = getHandlerAdapter(handler);
                if (handlerAdapterOpt.isPresent()) {
                    handlerAdapterOpt.get().handle(httpServletRequest, httpServletResponse, handler);
                    return;
                }
            }
        } catch (Exception ex) {
            logger.error("#60 YXTCustomBlockExceptionHandler handle error method={},url={}",
                httpServletRequest.getMethod(),
                httpServletRequest.getPathInfo(), ex);
            //发生异常走默认的降级规则
            defaultFallback(httpServletRequest, httpServletResponse, blockException);
            return;
        }
        //没有对应处理器走默认降级规则
        defaultFallback(httpServletRequest, httpServletResponse, blockException);
    }

    protected void defaultFallback(HttpServletRequest request, HttpServletResponse httpServletResponse,
        BlockException e) throws IOException {
        Map<String, Object> resData = new HashMap<>();
        if (e instanceof AuthorityException) {
            resData.put("code", "999002");
            resData.put("msg", "当前处于用户访问高峰期，请重新刷新页面！");
            resData.put("timestamp", System.currentTimeMillis());
        } else {
            resData.put("code", "999001");
            resData.put("msg", "当前处于用户访问高峰期，请重新刷新页面！");
            resData.put("timestamp", System.currentTimeMillis());
        }
        // http状态码
        httpServletResponse.setStatus(10000);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        new ObjectMapper()
            .writeValue(
                httpServletResponse.getWriter(),
                resData
            );
    }

    protected Optional<HandlerExecutionChain> getFallbackHandle(HttpServletRequest httpServletRequest) {
        try {
            HandlerExecutionChain mappingHandler = getHandler(httpServletRequest);
            if (mappingHandler == null) {
                return Optional.empty();
            }
            Object handler = mappingHandler.getHandler();
            if (!(handler instanceof HandlerMethod)) {
                return Optional.empty();
            }
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            YxtSentinel annotation = handlerMethod.getBeanType().getAnnotation(YxtSentinel.class);
            if (annotation == null) {
                Class<?> targetClass = handlerMethod.getBeanType();
                annotation = targetClass.getAnnotation(YxtSentinel.class);
            }
            if (annotation == null) {
                return Optional.empty();
            }
            Object fallbackObject = yxtSentinelContext.getInstance(YxtSentinelConstants.CONTEXT_NAME,
                annotation.configFallbackClass());
            MethodWrapper methodWrapper = getMethodWrapper(annotation, handlerMethod);
            if (!methodWrapper.isPresent()) {
                return Optional.empty();
            }
            HandlerMethod fallbackHandler = new HandlerMethod(fallbackObject, methodWrapper.getMethod());
            HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(fallbackHandler);
            return Optional.of(handlerExecutionChain);
        } catch (Exception e) {
            logger.error("#102 getFallbackHandle error method={},url={}", httpServletRequest.getMethod(),
                httpServletRequest.getPathInfo(), e);
            return Optional.empty();
        }

    }

    /**
     * 避免大量无效反射
     *
     * @param annotation
     * @param handlerMethod
     * @return
     */
    private MethodWrapper getMethodWrapper(YxtSentinel annotation, HandlerMethod handlerMethod) {
        MethodWrapper methodWrapper = YxtSentinelMetadataRegistry.lookupBlockHandler(
            annotation.configFallbackClass(), handlerMethod.getMethod().getName(),
            handlerMethod.getMethod().getParameterTypes());
        if (methodWrapper != null) {
            return methodWrapper;
        }
        String lockKey = String.format("%s.%s(%s)", annotation.configFallbackClass().getName(),
            handlerMethod.getMethod().getName(),
            Arrays.stream(handlerMethod.getMethod().getParameterTypes()).map(Class::getCanonicalName).collect(
                Collectors.joining(",")));
        synchronized (lockKey) {
            methodWrapper = YxtSentinelMetadataRegistry.lookupBlockHandler(
                annotation.configFallbackClass(), handlerMethod.getMethod().getName(),
                handlerMethod.getMethod().getParameterTypes());
            if (methodWrapper != null) {
                return methodWrapper;
            }
            Method declaredMethodFor = getDeclaredMethodFor(annotation.configFallbackClass(),
                handlerMethod.getMethod().getName(),
                handlerMethod.getMethod().getParameterTypes());
            YxtSentinelMetadataRegistry.updateBlockHandlerFor(annotation.configFallbackClass(),
                handlerMethod.getMethod().getName(), handlerMethod.getMethod().getParameterTypes(), declaredMethodFor);
            return YxtSentinelMetadataRegistry.lookupBlockHandler(annotation.configFallbackClass(),
                handlerMethod.getMethod().getName(), handlerMethod.getMethod().getParameterTypes());
        }
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

    protected Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return Optional.of(adapter);
                }
            }
        }
        return Optional.empty();
    }

    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, HandlerMapping> matchingBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerMappings = new ArrayList<>(matchingBeans.values());
            // We keep HandlerMappings in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerMappings);
        }
        Map<String, HandlerAdapter> matchingAdapterBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerAdapter.class, true, false);
        if (!matchingAdapterBeans.isEmpty()) {
            this.handlerAdapters = new ArrayList<>(matchingAdapterBeans.values());
            // We keep HandlerAdapters in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerAdapters);
        }
    }


    protected JsonEncoding getJsonEncoding(@Nullable MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            Charset charset = contentType.getCharset();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }
}
