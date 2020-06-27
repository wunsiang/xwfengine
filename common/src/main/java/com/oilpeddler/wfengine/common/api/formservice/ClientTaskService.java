package com.oilpeddler.wfengine.common.api.formservice;

import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;

import java.util.List;
import java.util.Map;

public interface ClientTaskService {
    boolean completeTask(String tiId, String pdId,String taskNo,Map<String, Object> requiredData);

    List<WfTaskInstanceBO> selectUnCompletedTask(String tiAssigner,String tiAssignerType);

    WfTaskInstanceDTO obtainTask(String id,String tiAssigner);

    List<WfTaskInstanceBO> selectUnObtainTask(String tiAssigner);
}
