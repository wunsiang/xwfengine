package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessParamsRecordBO;

import java.util.Map;

public interface WfProcessParamsRecordService {
    void recordRequiredData(String aiId, String taskNo, String pdId, Map<String, Object> requiredData);

    @Deprecated
    void calculateActivityData(WfActivtityInstanceDTO wfActivtityInstanceDTO, String tiId);

    WfProcessParamsRecordBO getByEnginePpName(String enginePpName, String processInstanceId, String pdId, String usertaskNo);
}
