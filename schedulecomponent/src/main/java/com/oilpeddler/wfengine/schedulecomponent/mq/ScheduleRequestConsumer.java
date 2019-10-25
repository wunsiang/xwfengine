package com.oilpeddler.wfengine.schedulecomponent.mq;



import com.oilpeddler.wfengine.common.api.scheduleservice.WfActivtityInstanceService;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfProcessDefinitionService;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceState;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.BpmnModel;
import com.oilpeddler.wfengine.common.element.EndEvent;
import com.oilpeddler.wfengine.common.element.UserTask;
import com.oilpeddler.wfengine.common.message.ProcessRequestMessage;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.TaskRequestMessage;
import com.oilpeddler.wfengine.common.tools.BpmnXMLConvertUtil;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfActivtityInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.service.ScheduleManageService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 调度器调度请求消费者
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-26
 */
@Service
@RocketMQMessageListener(
        topic = ScheduleRequestMessage.TOPIC,
        consumerGroup = "schedule-consumer-group")
public class ScheduleRequestConsumer implements RocketMQListener<ScheduleRequestMessage> {
    @Autowired
    ScheduleManageService scheduleManageService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    WfProcessDefinitionService wfProcessDefinitionService;

/*    @Reference
    WfTaskInstanceService wfTaskInstanceService;*/

    @Autowired
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Autowired
    WfActivtityInstanceService wfActivtityInstanceService;

/*    @Reference
    WfProcessInstanceService wfProcessInstanceService;*/

    @Override
    public void onMessage(ScheduleRequestMessage scheduleRequestMessage) {
        //合并并发活动数据以及判断是否存在关联活动未完成的情况
        //注：通过当前userTask的下一个元素的入度并结合当前处于正在执行状态的活动判断是否都已完成
        if(scheduleRequestMessage.getWfProcessInstanceMessage() != null){
            WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(scheduleRequestMessage.getWfProcessInstanceMessage().getPdId());
            BpmnModel bpmnModel = BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent());
            //返回值为endevent说明要结束流程
            //目前版本开启新流程时没啥必填项数据，所以为了简单，就先new个hashmap算了
            List<BaseElement> readyExecuteUserTaskList =  scheduleManageService.getFirstActivity(bpmnModel.getProcess(),scheduleRequestMessage.getWfProcessInstanceMessage().getId(),wfProcessDefinitionBO.getId());
            if(readyExecuteUserTaskList == null || readyExecuteUserTaskList.size() == 0){
                //返回值为空代表当前还有关联任务未完成,等待
                return;
            }
            //按照我们的规则，应该EndEvent的入度只能唯一，并且真正能执行到的只有一个
            else if(readyExecuteUserTaskList.get(0) instanceof EndEvent){
                //发消息给流程管理器关闭流程
                sendProcessRequestMessage(scheduleRequestMessage.getWfProcessInstanceMessage().getId());
            }else {
                //调度任务管理器做接下来的任务
                //开启新活动，如已存在（驳回循环情况）则更新活动状态
                //TODO wenxiang后面可以考虑在新开启活动时判断如果是驳回情况就去参数表里将之前的活动数据移到历史参数库中
                List<WfActivtityInstanceBO> wfActivtityInstanceBOList = wfActivtityInstanceService.addActivityList(readyExecuteUserTaskList,scheduleRequestMessage.getWfProcessInstanceMessage().getId(),wfProcessDefinitionBO.getId());
                sendTaskRequestMessage(wfActivtityInstanceBOList);
            }
        }else if(scheduleRequestMessage.getWfTaskInstanceMessage() != null) {
            //根据接收到的WfTaskInstanceMessage判断接下来的流转情况
            //记录任务带的流程参数数据(任务级)
            WfActivtityInstanceBO wfActivtityInstanceBO = wfActivtityInstanceService.getById(scheduleRequestMessage.getWfTaskInstanceMessage().getAiId());
            wfProcessParamsRecordService.recordRequiredData(scheduleRequestMessage.getWfTaskInstanceMessage().getId(),wfActivtityInstanceBO.getId(),wfActivtityInstanceBO.getPdId(),wfActivtityInstanceBO.getUsertaskNo(),scheduleRequestMessage.getWfTaskInstanceMessage().getRequiredData());
            //先判断是否有会签关联任务未完成，查看activeTiNum数目，减1并更新，若减1后不为0，则等待
            wfActivtityInstanceBO.setActiveTiNum(wfActivtityInstanceBO.getActiveTiNum() - 1);
            WfActivtityInstanceDTO wfActivtityInstanceDTO =  WfActivtityInstanceConvert.INSTANCE.convertBOToDTO(wfActivtityInstanceBO);
            wfActivtityInstanceService.update(wfActivtityInstanceDTO);
            if(wfActivtityInstanceDTO.getActiveTiNum() > 0)
                return;
            //会签活动通过与操作计算结果，非会签活动直接复制任务参数即可，并存储
            wfProcessParamsRecordService.calculateActivityData(wfActivtityInstanceDTO,scheduleRequestMessage.getWfTaskInstanceMessage().getId());
            //活动状态置已完成
            wfActivtityInstanceDTO.setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_COMPLETED);
            wfActivtityInstanceService.update(wfActivtityInstanceDTO);
            //活动历史库打时间戳记录
            scheduleManageService.recordActivityHistory(wfActivtityInstanceDTO);
            //之后开始找接下来的活动，返回值为空代表当前还有关联活动未完成，返回值为endevent说明要结束流程
            WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(wfActivtityInstanceBO.getPdId());
            BpmnModel bpmnModel = BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent());
            UserTask currentUserTask = scheduleManageService.findUserTaskByNo(wfActivtityInstanceBO.getUsertaskNo(),bpmnModel.getProcess());
            List<BaseElement> readyExecuteUserTaskList =  scheduleManageService.getNextSteps(currentUserTask,bpmnModel.getProcess(),wfActivtityInstanceBO.getPiId(),wfActivtityInstanceBO.getPdId());
            if(readyExecuteUserTaskList == null || readyExecuteUserTaskList.size() == 0){
                //返回值为空代表当前还有关联任务未完成,等待
                return;
            }
            //按照我们的规则，应该EndEvent的入度只能唯一，并且真正能执行到的只有一个
            else if(readyExecuteUserTaskList.get(0) instanceof EndEvent){
                //发消息给流程管理器关闭流程,流程实例由流程管理器负责扫尾
                sendProcessRequestMessage(wfActivtityInstanceBO.getPiId());
                //流程所属活动扫尾
                wfActivtityInstanceService.clearActivityOfProcess(wfActivtityInstanceBO.getPiId());
            }else {
                //调度任务管理器做接下来的任务
                //开启新活动，如已存在（驳回循环情况）则更新活动状态
                //TODO wenxiang后面可以考虑在新开启活动时判断如果是驳回情况就去参数表里将之前的活动数据移到历史参数库中
                List<WfActivtityInstanceBO> wfActivtityInstanceBOList = wfActivtityInstanceService.addActivityList(readyExecuteUserTaskList,wfActivtityInstanceBO.getPiId(),wfActivtityInstanceDTO.getPdId());
                sendTaskRequestMessage(wfActivtityInstanceBOList);
            }
        }
    }

    private void sendProcessRequestMessage(String piId) {
        rocketMQTemplate.convertAndSend(ProcessRequestMessage.TOPIC, new ProcessRequestMessage().setPiId(piId));
    }

    private void sendTaskRequestMessage(List<WfActivtityInstanceBO> wfActivtityInstanceBOList) {
        rocketMQTemplate.convertAndSend(TaskRequestMessage.TOPIC, new TaskRequestMessage().setWfActivtityInstanceBOList(wfActivtityInstanceBOList));
    }
}