package com.oilpeddler.wfengine.formcomponent;

import com.oilpeddler.wfengine.common.api.formservice.ClientProcessService;
import com.oilpeddler.wfengine.common.api.formservice.ClientTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FormcomponentApplicationTests {
    @Autowired
    ClientProcessService clientProcessService;

    @Autowired
    ClientTaskService clientTaskService;

    @Test
    public void contextLoads() {
        String pdId = "1213398349152014337";
        clientProcessService.startProcess(pdId,"流程实例2","申请人2","businesskey2");
    }

    @Test
    public void endTask(){
        String tiId = "1213441140771049473";
        String pdId = "1213398349152014337";
        String taskNo = "ut003";
        String dynamicAss = "e78dsdf78678sd,sdf89s7f89sdfsd89";
        Map<String,Object> requiredData = new HashMap<>();
        //requiredData.put("businessdynamicassignee01",dynamicAss);
        requiredData.put("businessday",6);
        //requiredData.put("businesspass",true);
        //requiredData.put("businessok",true);
        clientTaskService.completeTask(tiId,pdId,taskNo,requiredData);
    }

}
