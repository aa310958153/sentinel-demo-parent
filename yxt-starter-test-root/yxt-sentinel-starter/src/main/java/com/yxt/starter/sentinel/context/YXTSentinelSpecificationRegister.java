package com.yxt.starter.sentinel.context;

import com.yxt.starter.sentinel.annotation.YxtSentinel;
import com.yxt.starter.sentinel.constants.YXTSentinelConstants;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @Author liqiang
 * @Date 2024/8/28 15:51
 */
public class YXTSentinelSpecificationRegister implements BeanPostProcessor {


    private final List<YXTSentinelSpecification> yxtSentinelSpecificationList = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        registerClassYXTSentinelSpecification(bean);
        registerMethodYXTSentinelSpecification(bean);
        return bean;
    }

    public void registerClassYXTSentinelSpecification(Object bean) {
        YxtSentinel annotation = AnnotationUtils.getAnnotation(bean.getClass(), YxtSentinel.class);
        if (annotation == null) {
            return;
        }
        Class<?>[] configurationArr = annotation.configuration();
        if (configurationArr == null || configurationArr.length <= 0) {
            return;
        }
        YXTSentinelSpecification yxtSentinelSpecification = new YXTSentinelSpecification();
        yxtSentinelSpecification.setName(YXTSentinelConstants.CONTEXT_NAME);
        yxtSentinelSpecification.setConfiguration(configurationArr);
        yxtSentinelSpecificationList.add(yxtSentinelSpecification);
    }

    public void registerMethodYXTSentinelSpecification(Object bean) {
        Class<?> userType = bean.getClass();
        Map<Method, YxtSentinel> methods = MethodIntrospector.selectMethods(bean.getClass(),
            (MethodIntrospector.MetadataLookup<YxtSentinel>) method -> {
                try {
                    return getYXTSentinelForMethod(method);
                } catch (Throwable ex) {
                    throw new IllegalStateException("Invalid YXTSentinel on handler class [" +
                        userType.getName() + "]: " + method, ex);
                }
            });
        for (Entry<Method, YxtSentinel> methodYXTSentinelEntry : methods.entrySet()) {
            YxtSentinel yxtSentinel = methodYXTSentinelEntry.getValue();
            if (yxtSentinel != null && yxtSentinel.configuration() != null
                && yxtSentinel.configuration().length > 0) {
                YXTSentinelSpecification yxtSentinelSpecification = new YXTSentinelSpecification();
                yxtSentinelSpecification.setName(YXTSentinelConstants.CONTEXT_NAME);
                yxtSentinelSpecification.setConfiguration(yxtSentinel.configuration());
                yxtSentinelSpecificationList.add(yxtSentinelSpecification);
            }

        }

    }


    private YxtSentinel getYXTSentinelForMethod(Method method) {
        return AnnotatedElementUtils.findMergedAnnotation(method, YxtSentinel.class);

    }

    private boolean isYXTSentinelHandle(Object bean) {
        return (AnnotatedElementUtils.hasAnnotation(bean.getClass(), YxtSentinel.class));
    }

    public List<YXTSentinelSpecification> getYxtSentinelSpecificationList() {
        return yxtSentinelSpecificationList;
    }
}
