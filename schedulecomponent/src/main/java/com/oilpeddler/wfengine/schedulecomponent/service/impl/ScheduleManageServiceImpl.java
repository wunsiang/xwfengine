package com.oilpeddler.wfengine.schedulecomponent.service.impl;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.element.*;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.tools.BpmnXMLConvertUtil;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfActivtityInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfActivityHistoryInstanceMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfActivityHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.service.ScheduleManageService;
import com.oilpeddler.wfengine.schedulecomponent.tools.PathParseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleManageServiceImpl implements ScheduleManageService {
    @Autowired
    PathParseUtil pathParseUtil;

    @Reference
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Autowired
    WfActivityHistoryInstanceMapper wfActivityHistoryInstanceMapper;

    @Override
    public List<BaseElement> getFirstActivity(Process process,String processInstanceId) {
        StartEvent startEvent = null;
        for(Event event : process.getEventList()){
            if(event instanceof StartEvent){
                startEvent = (StartEvent) event;
            }
        }
        List<BaseElement> nextSteps = new ArrayList<>();
        nextSteps.add(startEvent);
        while (true){
            boolean flag = true;
            List<BaseElement> addList = new ArrayList<>();
            List<BaseElement> delList = new ArrayList<>();
            for(BaseElement element : nextSteps){
                if(element instanceof Gateway || element instanceof StartEvent){
                    flag = false;
                    pathParseUtil.startHandle(nextSteps,process,addList,delList,processInstanceId);
                    break;
                }
            }
            nextSteps.removeAll(delList);
            nextSteps.addAll(addList);
            if(flag)
                break;
        }
        return nextSteps;
    }

    @Override
    public List<BaseElement> getNextSteps(UserTask currentUserTask, Process process,String processInstanceId) {
        BaseElement nextElement = BpmnXMLConvertUtil.findMatchElement(currentUserTask.getOutgoingFlows().get(0).getTargetRef(),process);
        List<BaseElement> nextSteps = new ArrayList<>();
        nextSteps.add(nextElement);
        while (true){
             boolean flag = true;
             List<BaseElement> addList = new ArrayList<>();
             List<BaseElement> delList = new ArrayList<>();
             for(BaseElement element : nextSteps){
                 if(element instanceof Gateway){
                     flag = false;
                     pathParseUtil.startHandle(nextSteps,process,addList,delList,processInstanceId);
                     break;
                 }
             }
             nextSteps.removeAll(delList);
             nextSteps.addAll(addList);
             if(flag)
                 break;
        }
        return nextSteps;
    }

    @Override
    public UserTask findUserTaskByNo(String no, Process process) {
        for(UserTask userTask : process.getUserTaskList()){
            if(userTask.getNo().equals(no))
                return userTask;
        }
        return null;
    }

    @Override
    public void recordActivityHistory(WfActivtityInstanceDTO wfActivtityInstanceDTO) {
        WfActivityHistoryInstanceDO wfActivityHistoryInstanceDO = WfActivtityInstanceConvert.INSTANCE.convertRunToHistoryDO(wfActivtityInstanceDTO);
        wfActivityHistoryInstanceDO.setCreatetime(new Date());
        wfActivityHistoryInstanceDO.setUpdatetime(wfActivityHistoryInstanceDO.getCreatetime());
        wfActivityHistoryInstanceMapper.insert(wfActivityHistoryInstanceDO);
    }


}
