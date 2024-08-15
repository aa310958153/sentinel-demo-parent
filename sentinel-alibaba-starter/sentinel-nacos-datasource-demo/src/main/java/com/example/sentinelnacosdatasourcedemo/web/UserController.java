/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sentinelnacosdatasourcedemo.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.sentinelnacosdatasourcedemo.service.UserService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/helloSentinelResource")
    @ResponseBody
    @SentinelResource(value = "helloQuery",blockHandler ="helloSentinelResourceBlockHandler" )
    public String helloSentinelResource(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }
    public String helloSentinelResourceBlockHandler(String name, BlockException ex) {
        return  ex.getClass().getName();
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello2(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }


    @RequestMapping("/user/{id}")
    @ResponseBody
    public User user(@PathVariable("id") Long id) {
        return userService.findByUser(id);
    }

    @RequestMapping("/user")
    @ResponseBody
    @SentinelResource(blockHandler = "blockHandlerForQuery")
    public List<User> query(@RequestParam(name ="name") String name) {
        return userService.listByUserName(name)   ;
    }

    /**
     * blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
     * @param name
     * @return
     */
    public List<User> blockHandlerForQuery(@RequestParam(name ="name") String name) {
        User user= new User();
        user.setAge(13);
        user.setName("我是降级返回");
        return Collections.singletonList(user);
    }


    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
