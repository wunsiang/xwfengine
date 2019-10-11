package com.oilpeddler.wfengine.common.api.taskmanagservice;

import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceQueryDTO;

import java.util.List;
import java.util.Map;

public interface WfTaskInstanceService {
    boolean completeTask(String tiId, Map<String, Object> requiredData);

    int count(WfTaskInstanceQueryDTO wfTaskInstanceQueryDTO);

    List<WfTaskInstanceDTO> findRelatedTaskList(String aiId);

    void moveRelatedTaskToHistory(String aiId);

    WfTaskInstanceBO save(WfTaskInstanceDTO wfTaskInstanceDTO);
}
