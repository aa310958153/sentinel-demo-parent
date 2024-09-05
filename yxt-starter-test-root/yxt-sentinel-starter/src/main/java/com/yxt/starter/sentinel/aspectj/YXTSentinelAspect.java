/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yxt.starter.sentinel.aspectj;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.yxt.starter.sentinel.annotation.YXTSentinel;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 自定注解切面
 */
@Aspect
public class YXTSentinelAspect extends AbstractYXTSentinelAspectSupport {


    @Pointcut("@within(com.yxt.starter.sentinel.annotation.YXTSentinel)")
    public void yxtSentinelAnnotationPointcut() {
    }

    @Around("yxtSentinelAnnotationPointcut()")
    public Object invokeResourceWithSentinel(ProceedingJoinPoint pjp) throws Throwable {
        Method originMethod = resolveMethod(pjp);

        YXTSentinel annotation = originMethod.getAnnotation(YXTSentinel.class);
        if (annotation == null) {
            Class<?> targetClass = pjp.getTarget().getClass();
            annotation = targetClass.getAnnotation(YXTSentinel.class);
        }
        if (annotation == null) {
            throw new IllegalStateException("Wrong state for YxtSentinel annotation");
        }

        EntryType entryType = annotation.entryType();
        int resourceType = annotation.resourceType();
        Entry entry = null;
        try {
            if (annotation.shouldReportResource()) {
                String resourceName = getResourceName(annotation.value(), originMethod);
                entry = SphU.entry(resourceName, resourceType, entryType, pjp.getArgs());
            }
            return pjp.proceed();
        } catch (BlockException ex) {
            return handleBlockException(pjp, annotation, ex);
        } catch (Throwable ex) {
            Class<? extends Throwable>[] exceptionsToIgnore = annotation.exceptionsToIgnore();
            // The ignore list will be checked first.
            if (exceptionsToIgnore.length > 0 && exceptionBelongsTo(ex, exceptionsToIgnore)) {
                throw ex;
            }
            if (exceptionBelongsTo(ex, annotation.exceptionsToTrace())) {
                traceException(ex);
                return handleFallback(pjp, annotation, ex);
            }
            // No fallback function can handle the exception, so throw it out.
            throw ex;
        } finally {
            if (entry != null && annotation.shouldReportResource()) {
                entry.exit(1, pjp.getArgs());
            }
        }
    }


}
