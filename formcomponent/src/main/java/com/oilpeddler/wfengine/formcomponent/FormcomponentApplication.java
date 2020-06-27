package com.oilpeddler.wfengine.formcomponent;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
@MapperScan("com.oilpeddler.wfengine.formcomponent.dao")
public class FormcomponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormcomponentApplication.class, args);
    }

}
