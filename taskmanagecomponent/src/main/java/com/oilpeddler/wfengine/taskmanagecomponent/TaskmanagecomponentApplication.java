package com.oilpeddler.wfengine.taskmanagecomponent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.oilpeddler.wfengine.taskmanagecomponent.dao")
public class TaskmanagecomponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagecomponentApplication.class, args);
    }

}
