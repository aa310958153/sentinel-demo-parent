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

import java.lang.reflect.Method;

/**
 * 存储降级方法的元数据
 *
 * @Author liqiang
 * @Date 2024/9/3 10:19
 */
public class MethodWrapper {

    /**
     * method元数据
     */
    private final Method method;

    /**
     * 防止缓存穿透，如果未false 表示不存在
     */
    private final boolean present;

    private MethodWrapper(Method method, boolean present) {
        this.method = method;
        this.present = present;
    }

    public static MethodWrapper wrap(Method method) {
        if (method == null) {
            return none();
        }
        return new MethodWrapper(method, true);
    }

    public static MethodWrapper none() {
        return new MethodWrapper(null, false);
    }

    public Method getMethod() {
        return method;
    }

    public boolean isPresent() {
        return present;
    }

}
