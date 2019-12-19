package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

public interface WfProcessDefinitionService {
    WfProcessDefinitionBO getWfProcessDefinitionById(String id);
    WfProcessDefinitionBO generatePDFromTemplateFile(WfProcessTemplateDTO wfProcessTemplateDTO);
}
