package com.yxt.starter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import java.lang.reflect.UndeclaredThrowableException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理代理类通过UndeclaredThrowableException 包装过的流控异常
 *
 * @Author liqiang
 */
public class YxtSentinelHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(YxtSentinelHandlerExceptionResolver.class);
    @Resource
    private YxtCustomBlockExceptionHandler yxtCustomBlockExceptionHandler;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {
        // 检查是否为UndeclaredThrowableException,不符合处理条件，交给后续异常处理器处理
        if (!(ex instanceof UndeclaredThrowableException)) {
            return null;
        }

        // 获取嵌套的异常
        Throwable cause = ex.getCause();
        // 检查是否为BlockException
        if (cause instanceof BlockException) {
            BlockException blockException = (BlockException) cause;
            try {
                // 调用自定义异常处理器
                yxtCustomBlockExceptionHandler.handle(request, response, blockException);
                //
                return new ModelAndView();
            } catch (Exception e) {
                logger.error("#42 YxtSentinelHandlerExceptionResolver 处理流控异常失败", e);
                // 返回NULL  交给后续异常处理器处理
                return null;
            }
        }
        // 不符合处理条件，交给后续异常处理器处理
        return null;
    }
}
