package com.oilpeddler.wfengine.common.api.scheduleservice;

import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;

import java.util.Map;

public interface WfProcessParamsRecordService {
    void recordRequiredData(String tiId, String aiId, String pdId, String taskNo, Map<String, Object> requiredData);

    void calculateActivityData(WfActivtityInstanceDTO wfActivtityInstanceDTO, String tiId);

    WfProcessParamsRecordBO getByEnginePpName(String enginePpName, String processInstanceId, String pdId, String usertaskNo);
}
