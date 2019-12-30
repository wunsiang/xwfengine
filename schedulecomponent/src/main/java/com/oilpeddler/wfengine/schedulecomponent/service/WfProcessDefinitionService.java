package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;

public interface WfProcessDefinitionService {
    WfProcessDefinitionBO getWfProcessDefinitionById(String id);
    WfProcessDefinitionBO generatePDFromTemplateFile(WfProcessTemplateDTO wfProcessTemplateDTO);
    WfProcessDefinitionBO getFromCache(String id);
    void setToCache(WfProcessDefinitionBO wfProcessDefinitionBO);
}
