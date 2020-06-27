package com.oilpeddler.wfengine.common.api.scheduleservice;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

import java.util.List;

public interface WfProcessDefinitionService {
    WfProcessDefinitionBO getWfProcessDefinitionById(String id);
    WfProcessDefinitionBO generatePDFromTemplateFile(WfProcessTemplateDTO wfProcessTemplateDTO);
    List<WfProcessDefinitionBO> queryDefinitionList();
}
