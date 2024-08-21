package com.example.sentinelnacosdatasourcedemosdk;

/**
 * @Author liqiang
 * @Date 2024/8/21 12:24
 */
public class StudentOpenApiFallback implements StudentOpenApi {

    @Override
    public Student getStudent(String name) {
        Student student=new Student();
        student.setName("降级策略返回");
        return student;
    }
}
