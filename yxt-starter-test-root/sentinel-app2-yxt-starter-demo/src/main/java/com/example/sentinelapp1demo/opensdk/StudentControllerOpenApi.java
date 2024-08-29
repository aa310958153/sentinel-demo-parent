package com.example.sentinelapp1demo.opensdk;

import com.example.sentinelapp1demo.demos.web.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liqiang
 * @Date 2024/8/29 15:05
 */
public interface StudentControllerOpenApi {

    @RequestMapping("/student/{id}")
    @ResponseBody
    User user(@PathVariable("id") Long id);
}
