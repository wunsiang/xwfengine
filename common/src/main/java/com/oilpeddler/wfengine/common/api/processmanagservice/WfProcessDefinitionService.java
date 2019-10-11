package com.oilpeddler.wfengine.common.api.processmanagservice;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;

public interface WfProcessDefinitionService {
    WfProcessDefinitionBO getWfProcessDefinitionById(String id);
}
