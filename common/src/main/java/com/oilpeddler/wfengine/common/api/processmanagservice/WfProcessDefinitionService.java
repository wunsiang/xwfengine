package com.oilpeddler.wfengine.common.api.processmanagservice;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

public interface WfProcessDefinitionService {
    WfProcessDefinitionBO getWfProcessDefinitionById(String id);
    WfProcessDefinitionBO generatePDFromTemplateFile(WfProcessTemplateDTO wfProcessTemplateDTO);
}
