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
        String pdId = "1214539031835500546";
        clientProcessService.startProcess(pdId,"流程实例3","申请人3","businesskey3");
    }

    @Test
    public void endTask(){
        String tiId = "1213451203787186177";
        String pdId = "1214539031835500546";
        String taskNo = "ut005";
        //String dynamicAss = "e78dsdf78678sd,sdf89s7f89sdfsd89";
        Map<String,Object> requiredData = new HashMap<>();
        //requiredData.put("businessdynamicassignee01",dynamicAss);
        //requiredData.put("businessday",6);
        //requiredData.put("businesspass",true);
        //requiredData.put("businessok",true);
        clientTaskService.completeTask(tiId,pdId,taskNo,requiredData);
    }

}
