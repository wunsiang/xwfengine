package com.oilpeddler.wfengine.schedulecomponent.mq;




import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceState;
import com.oilpeddler.wfengine.common.constant.TaskInstanceState;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfTaskHistoryInstanceBO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfActivtityInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfProcessInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfTaskHistoryInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfTaskInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfProcessInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfProcessInstanceStartDTO;
import com.oilpeddler.wfengine.schedulecomponent.service.*;
import com.oilpeddler.wfengine.schedulecomponent.tools.BpmnXMLConvertUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    WfTaskInstanceService wfTaskInstanceService;

    @Autowired
    WfTaskHistoryInstanceService wfTaskHistoryInstanceService;

    @Autowired
    WfProcessInstanceService wfProcessInstanceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    SqlSessionTemplate sqlsession;


    @Transactional
    @Override
    public void onMessage(ScheduleRequestMessage scheduleRequestMessage) {
        //合并并发活动数据以及判断是否存在关联活动未完成的情况
        //注：通过当前userTask的下一个元素的入度并结合当前处于正在执行状态的活动判断是否都已完成
        if(scheduleRequestMessage.getWfProcessInstanceMessage() != null){
            /*//幂等性保证
            boolean absentBoolean = stringRedisTemplate.opsForValue().setIfAbsent(scheduleRequestMessage.getWfProcessInstanceMessage().getId(),"1",1000, TimeUnit.SECONDS);
            if(!absentBoolean)
                return;*/
            WfProcessInstanceBO wfProcessInstanceBO = wfProcessInstanceService.startProcess(WfProcessInstanceConvert.INSTANCE.convertMessageToStartDTO(scheduleRequestMessage.getWfProcessInstanceMessage()));
            WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getFromCache(wfProcessInstanceBO.getPdId());
            if(wfProcessDefinitionBO == null){
                wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(wfProcessInstanceBO.getPdId());
                wfProcessDefinitionBO.setBpmnModel(BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent()));
            }
            //新版从这开始，新建一个token
            Token rootToken = new Token();
            rootToken.setPiId(wfProcessInstanceBO.getId());
            rootToken.setPdId(wfProcessInstanceBO.getPdId());
            //no在start任务里面配
            rootToken.setParentId("0");
            //将rootToken加入缓存
            //tokenService.setToCache(rootToken);
            wfProcessDefinitionBO.getBpmnModel().getProcess().getStartEvent().execute(rootToken);

        }else if(scheduleRequestMessage.getWfTaskInstanceMessage() != null) {
            //幂等性保证
            //stringRedisTemplate.setEnableTransactionSupport(true);
            if (!stringRedisTemplate.delete(scheduleRequestMessage.getWfTaskInstanceMessage().getId())) {
                return;
            }
            try {
                taskSchedule(scheduleRequestMessage);
            }catch (Exception e){
                stringRedisTemplate.opsForValue().set(scheduleRequestMessage.getWfTaskInstanceMessage().getId(),"1");
                System.out.println(stringRedisTemplate.opsForValue().get(scheduleRequestMessage.getWfTaskInstanceMessage().getId()));
                throw new RuntimeException();
            }
        }
    }

    @Transactional
    void taskSchedule(ScheduleRequestMessage scheduleRequestMessage){
        WfTaskInstanceBO wfTaskInstanceBO = wfTaskInstanceService.getById(scheduleRequestMessage.getWfTaskInstanceMessage().getId());
        wfTaskInstanceBO.setTiStatus(TaskInstanceState.TASK_INSTANCE_STATE_COMPLETED);
        WfTaskHistoryInstanceBO wfTaskHistoryInstanceBO = WfTaskInstanceConvert.INSTANCE.convertBOToHistory(wfTaskInstanceBO);
        wfTaskHistoryInstanceBO.setCreatetime(new Date());
        wfTaskHistoryInstanceBO.setUpdatetime(wfTaskHistoryInstanceBO.getCreatetime());
        wfTaskHistoryInstanceService.save(WfTaskHistoryInstanceConvert.INSTANCE.convertBOToDTO(wfTaskHistoryInstanceBO));
        //从运行表中清除当前记录
        wfTaskInstanceService.delete(wfTaskInstanceBO.getId());
        //根据接收到的WfTaskInstanceMessage判断接下来的流转情况
        //记录任务带的流程参数数据(任务级)
        WfActivtityInstanceBO wfActivtityInstanceBO = wfActivtityInstanceService.getById(wfTaskInstanceBO.getAiId());
        wfProcessParamsRecordService.recordRequiredData(wfActivtityInstanceBO.getId(), scheduleRequestMessage.getWfTaskInstanceMessage().getId(), scheduleRequestMessage.getWfTaskInstanceMessage().getRequiredData());
        sqlsession.clearCache();
        //先判断是否有会签关联任务未完成，查看activeTiNum数目，减1并更新，若减1后不为0，则等待
        wfActivtityInstanceBO.setActiveTiNum(wfActivtityInstanceBO.getActiveTiNum() - 1);
        WfActivtityInstanceDTO wfActivtityInstanceDTO = WfActivtityInstanceConvert.INSTANCE.convertBOToDTO(wfActivtityInstanceBO);
        wfActivtityInstanceService.update(wfActivtityInstanceDTO);
        if (wfActivtityInstanceDTO.getActiveTiNum() > 0)
            return;
        wfActivtityInstanceDTO.setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_COMPLETED);
        wfActivtityInstanceService.update(wfActivtityInstanceDTO);
        //活动历史库打时间戳记录
        scheduleManageService.recordActivityHistory(wfActivtityInstanceDTO);
        //之后开始找接下来的活动，返回值为空代表当前还有关联活动未完成，返回值为endevent说明要结束流程
        WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.getFromCache(wfActivtityInstanceBO.getPdId());
        if (wfProcessDefinitionBO == null) {
            wfProcessDefinitionBO = wfProcessDefinitionService.getWfProcessDefinitionById(wfActivtityInstanceBO.getPdId());
            wfProcessDefinitionBO.setBpmnModel(BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent()));
        }
        //Token复原，关键在子父关系一整套都要复原
        //Token cToken = tokenService.recoverTokens(wfActivtityInstanceBO.getPiId(), wfActivtityInstanceBO.getPdId(), wfActivtityInstanceBO.getUsertaskNo(), wfProcessDefinitionBO.getBpmnModel().getProcess());
        Token cToken = tokenService.getCurrentToken(wfActivtityInstanceBO.getPiId(),  wfActivtityInstanceBO.getUsertaskNo(), wfProcessDefinitionBO.getBpmnModel().getProcess());
        cToken.signal();
    }

}