package com.oilpeddler.wfengine.common.api.formservice;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.dataobject.ParmObject;

import java.util.List;
import java.util.Map;

public interface ClientProcessService {
    boolean startProcess(String pdId, String piName, String piStarter, String piBusinesskey, Map<String, ParmObject> requiredData);
    List<WfProcessDefinitionBO> queryDefinitionList();
    void changeProcessState(String piId, String state);
    List<WfProcessInstanceBO> getProcessListByUserId(String piStarter);
}
