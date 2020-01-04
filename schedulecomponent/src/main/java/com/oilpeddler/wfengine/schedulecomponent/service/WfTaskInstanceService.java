package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.schedulecomponent.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfTaskInstanceQueryDTO;

import java.util.List;
import java.util.Map;

public interface WfTaskInstanceService {
    //boolean completeTask(String tiId, Map<String, Object> requiredData);

    void ending(WfTaskInstanceDTO wfTaskInstanceDTO);

    int count(WfTaskInstanceQueryDTO wfTaskInstanceQueryDTO);

    List<WfTaskInstanceDTO> findRelatedTaskList(String aiId);

    void moveRelatedTaskToHistory(String aiId);

    WfTaskInstanceBO save(WfTaskInstanceDTO wfTaskInstanceDTO);

    void recordHistory(WfTaskInstanceDTO wfTaskInstanceDTO);

    WfTaskInstanceBO getById(String id);

    void delete(String id);
}
