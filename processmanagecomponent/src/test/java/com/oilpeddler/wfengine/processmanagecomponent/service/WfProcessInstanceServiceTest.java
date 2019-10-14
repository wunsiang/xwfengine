package com.oilpeddler.wfengine.processmanagecomponent.service;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessInstanceService;
import com.oilpeddler.wfengine.common.dto.WfProcessInstanceStartDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WfProcessInstanceServiceTest {
    @Reference
    WfProcessInstanceService wfProcessInstanceService;

    @Test
    public void serviceTest() {
        WfProcessInstanceStartDTO wfProcessInstanceStartDTO = new WfProcessInstanceStartDTO()
                .setPdId("aaa")
                .setPiBusinesskey("aaa")
                .setPiName("aaa")
                .setPiStarter("aaa");
        wfProcessInstanceService.startProcess(wfProcessInstanceStartDTO);
    }
}
