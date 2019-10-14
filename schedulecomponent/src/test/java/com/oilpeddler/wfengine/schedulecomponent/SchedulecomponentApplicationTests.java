package com.oilpeddler.wfengine.schedulecomponent;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessInstanceService;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulecomponentApplicationTests {
    @Reference
    WfProcessInstanceService wfProcessInstanceService;
    @Test
    public void contextLoads() {
        wfProcessInstanceService.haha();;
        // WfProcessInstanceBO wfProcessInstanceBO = wfProcessInstanceService.getById("1183246872479858690");
    }



}
