package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.element.BaseElement;

import java.util.List;
import java.util.Map;

public interface WfActivtityInstanceService {
    WfActivtityInstanceBO getById(String id);

    WfActivtityInstanceBO getOneByMap(Map<String, Object> conditionMap);

    void update(WfActivtityInstanceDTO wfActivtityInstanceDTO);

    void clearActivityOfProcess(String piId);

    List<WfActivtityInstanceBO> addActivityList(List<BaseElement> userTaskList, String piId, String pdId);

}
