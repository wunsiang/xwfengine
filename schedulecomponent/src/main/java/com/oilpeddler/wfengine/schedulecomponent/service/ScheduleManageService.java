package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.element.UserTask;

import java.util.List;
import java.util.Map;

public interface ScheduleManageService {
    /**
     *
     * @param process
     * @param requiredData
     * @param processInstanceId
     * @return 返回值为空代表当前还有关联任务未完成，返回值为endevent说明要结束流程
     */
    List<BaseElement> getFirstActivity(Process process,String processInstanceId);

    //返回值为空代表当前还有关联任务未完成，返回值为endevent说明要结束流程
    List<BaseElement> getNextSteps(UserTask currentUserTask, Process process,String processInstanceId);

    UserTask findUserTaskByNo(String no,Process process);
    //boolean IsRelatedUserTaskCompleted()

    void recordActivityHistory(WfActivtityInstanceDTO wfActivtityInstanceDTO);

}
