package com.example.sentinelnacosdatasourcedemosdk;

import java.io.Serializable;

/**
 * @Author liqiang
 * @Date 2024/8/20 21:40
 */
public class Student implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    private Integer age;

}
