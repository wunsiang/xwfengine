package com.oilpeddler.wfengine.schedulecomponent.mq;




import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceState;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfActivtityInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.element.BpmnModel;
import com.oilpeddler.wfengine.schedulecomponent.service.*;
import com.oilpeddler.wfengine.schedulecomponent.tools.BpmnXMLConvertUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Autowired
    WfProcessDefinitionService wfProcessDefinitionService;

    @Autowired
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Autowired
    WfActivtityInstanceService wfActivtityInstanceService;

    @Autowired
    TokenService tokenService;

    @Override
    public void onMessage(ScheduleRequestMessage scheduleRequestMessage) {
        //合并并发活动数据以及判断是否存在关联活动未完成的情况
        //注：通过当前userTask的下一个元素的入度并结合当前处于正在执行状态的活动判断是否都已完成
        if(scheduleRequestMessage.getWfProcessInstanceMessage() != null){
            WfProcessInstanceMessage wfProcessInstanceMessage = scheduleRequestMessage.getWfProcessInstanceMessage();
            WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getFromCache(wfProcessInstanceMessage.getPdId());
            if(wfProcessDefinitionBO == null){
                wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(wfProcessInstanceMessage.getPdId());
                wfProcessDefinitionBO.setBpmnModel(BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent()));
            }
            //新版从这开始，新建一个token
            Token rootToken = new Token();
            rootToken.setPiId(scheduleRequestMessage.getWfProcessInstanceMessage().getId());
            rootToken.setPdId(scheduleRequestMessage.getWfProcessInstanceMessage().getPdId());
            //no在start任务里面配
            rootToken.setParentId("0");
            //将rootToken加入缓存
            //tokenService.setToCache(rootToken);
            wfProcessDefinitionBO.getBpmnModel().getProcess().getStartEvent().execute(rootToken);

        }else if(scheduleRequestMessage.getWfTaskInstanceMessage() != null) {
            //根据接收到的WfTaskInstanceMessage判断接下来的流转情况
            //记录任务带的流程参数数据(任务级)
            WfActivtityInstanceBO wfActivtityInstanceBO = wfActivtityInstanceService.getById(scheduleRequestMessage.getWfTaskInstanceMessage().getAiId());
            wfProcessParamsRecordService.recordRequiredData(wfActivtityInstanceBO.getId(),wfActivtityInstanceBO.getUsertaskNo(),wfActivtityInstanceBO.getPdId(),scheduleRequestMessage.getWfTaskInstanceMessage().getRequiredData());
            //先判断是否有会签关联任务未完成，查看activeTiNum数目，减1并更新，若减1后不为0，则等待
            wfActivtityInstanceBO.setActiveTiNum(wfActivtityInstanceBO.getActiveTiNum() - 1);
            WfActivtityInstanceDTO wfActivtityInstanceDTO =  WfActivtityInstanceConvert.INSTANCE.convertBOToDTO(wfActivtityInstanceBO);
            wfActivtityInstanceService.update(wfActivtityInstanceDTO);
            if(wfActivtityInstanceDTO.getActiveTiNum() > 0)
                return;
            /*//会签活动通过与操作计算结果，非会签活动直接复制任务参数即可，并存储
            wfProcessParamsRecordService.calculateActivityData(wfActivtityInstanceDTO,scheduleRequestMessage.getWfTaskInstanceMessage().getId());
       */     //活动状态置已完成
            wfActivtityInstanceDTO.setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_COMPLETED);
            wfActivtityInstanceService.update(wfActivtityInstanceDTO);
            //活动历史库打时间戳记录
            scheduleManageService.recordActivityHistory(wfActivtityInstanceDTO);
            //之后开始找接下来的活动，返回值为空代表当前还有关联活动未完成，返回值为endevent说明要结束流程
            WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getFromCache(wfActivtityInstanceBO.getPdId());
            if(wfProcessDefinitionBO == null){
                wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(wfActivtityInstanceBO.getPdId());
                wfProcessDefinitionBO.setBpmnModel(BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent()));
            }
            //Token复原，关键在子父关系一整套都要复原
            Token cToken = tokenService.recoverTokens(wfActivtityInstanceBO.getPiId(),wfActivtityInstanceBO.getPdId(),wfActivtityInstanceBO.getUsertaskNo(),wfProcessDefinitionBO.getBpmnModel().getProcess());
            cToken.signal();
        }
    }

}