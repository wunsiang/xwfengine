package com.oilpeddler.wfengine.processmanagecomponent;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessTemplateService;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.processmanagecomponent.service.impl.WfProcessDefinitionServiceImpl;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessmanagecomponentApplicationTests {

    @Autowired
    WfProcessTemplateService wfProcessTemplateService;

    @Autowired
    WfProcessDefinitionServiceImpl wfProcessDefinitionService;
    @Test
    public void contextLoads() {
        /**
         * 模拟流程图绘制提交功能
         */
        WfProcessTemplateDTO wfProcessTemplateDTO = wfProcessTemplateService.selectByPtFilename("汇报用流程图");
        WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.generatePDFromTemplateFile(wfProcessTemplateDTO);
        /**
         * TODO 模拟开发人员配置业务表单参数名称和流程引擎参数映射关系
         */
    }

}
