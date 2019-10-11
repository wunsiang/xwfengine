package com.oilpeddler.wfengine.processmanagecomponent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.oilpeddler.wfengine.processmanagecomponent.dao") // 扫描对应的 Mapper 接口
public class ProcessmanagecomponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessmanagecomponentApplication.class, args);
    }

}
