package com.oilpeddler.wfengine.common.api.scheduleservice;

import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceQueryDTO;

import java.util.List;

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

    String getFirstTaskId(String piId);

    WfTaskInstanceDTO updateById(WfTaskInstanceDTO wfTaskInstanceDTO);

    List<WfTaskInstanceBO> selectUnCompletedTask(String tiAssigner, String tiAssignerType);

    List<WfTaskInstanceBO> selectUnObtainedTask(String tiAssigner);
}
