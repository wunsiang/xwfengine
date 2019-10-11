package com.oilpeddler.wfengine.schedulecomponent.tools;

import com.oilpeddler.wfengine.common.element.BpmnModel;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessTemplateMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessTemplateDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BpmnXMLConvertUtilTest {
    @Autowired
    private WfProcessTemplateMapper wfProcessTemplateMapper;

    @Test
    public void testConvert(){
       /* WfProcessTemplateDO wfProcessTemplateDO = wfProcessTemplateMapper.selectById(1);
        BpmnModel bpmnModel = BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessTemplateDO.getPtContent());
        System.out.println(bpmnModel.getName());*/
    }
}
