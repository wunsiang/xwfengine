package com.oilpeddler.wfengine.processmanagecomponent.service.impl;
import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessDefinitionService;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.processmanagecomponent.convert.WfProcessDefinitionConvert;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessDefinitionMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessTemplateMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessDefinitionDO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessTemplateDO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class WfProcessDefinitionServiceImpl implements WfProcessDefinitionService {
    @Autowired
    WfProcessDefinitionMapper wfProcessDefinitionMapper;

    @Autowired
    WfProcessTemplateMapper wfProcessTemplateMapper;

    @Override
    public WfProcessDefinitionBO getWfProcessDefinitionById(String id) {
        WfProcessDefinitionDO wfProcessDefinitionDO = wfProcessDefinitionMapper.selectById(id);
        WfProcessDefinitionBO wfProcessDefinitionBO = WfProcessDefinitionConvert.INSTANCE.convertDOToBO(wfProcessDefinitionDO);
        WfProcessTemplateDO wfProcessTemplateDO = wfProcessTemplateMapper.selectById(wfProcessDefinitionDO.getPtId());
        wfProcessDefinitionBO.setPtContent(wfProcessTemplateDO.getPtContent());
        return wfProcessDefinitionBO;
    }
}
