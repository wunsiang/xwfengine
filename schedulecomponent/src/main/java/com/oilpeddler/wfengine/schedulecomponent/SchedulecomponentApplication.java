package com.oilpeddler.wfengine.schedulecomponent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.oilpeddler.wfengine.schedulecomponent.dao") // 扫描对应的 Mapper 接口
@SpringBootApplication
public class SchedulecomponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulecomponentApplication.class, args);
    }

}
