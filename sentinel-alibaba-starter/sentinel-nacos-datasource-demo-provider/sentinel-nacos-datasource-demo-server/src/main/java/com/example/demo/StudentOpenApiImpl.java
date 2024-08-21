package com.example.demo;

import com.example.sentinelnacosdatasourcedemosdk.Student;
import com.example.sentinelnacosdatasourcedemosdk.StudentOpenApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liqiang
 * @Date 2024/8/20 21:39
 */
@RestController
@FeignClient(value ="sentinel-nacos-datasource-demo-server")
public class StudentOpenApiImpl implements StudentOpenApi {

    @Override
    public Student getStudent(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(13);
        return student;
    }
}
