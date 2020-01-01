package com.oilpeddler.wfengine.taskmanagecomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskInstanceService;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfTaskHistoryInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.constant.TaskInstanceState;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceQueryDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.TaskRequestMessage;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import com.oilpeddler.wfengine.taskmanagecomponent.convert.WfTaskInstanceConvert;
import com.oilpeddler.wfengine.taskmanagecomponent.dao.WfTaskHistoryInstanceMapper;
import com.oilpeddler.wfengine.taskmanagecomponent.dao.WfTaskInstanceMapper;
import com.oilpeddler.wfengine.taskmanagecomponent.dataobject.WfTaskHistoryInstanceDO;
import com.oilpeddler.wfengine.taskmanagecomponent.dataobject.WfTaskInstanceDO;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;



@Service
@org.springframework.stereotype.Service
public class WfTaskInstanceServiceImpl implements WfTaskInstanceService {
    @Autowired
    WfTaskInstanceMapper wfTaskInstanceMapper;

    @Autowired
    WfTaskHistoryInstanceMapper wfTaskHistoryInstanceMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 完成当前任务，并将必填项和任务ID通过消息队列发送给调度器
     * @param tiId
     * @param requiredData
     * @return
     */
    @Override
    public boolean completeTask(String tiId, Map<String, Object> requiredData) {
        //更新任务状态为已完成
        WfTaskInstanceDO wfTaskInstanceDO = wfTaskInstanceMapper.selectById(tiId);
        wfTaskInstanceDO.setTiStatus(TaskInstanceState.TASK_INSTANCE_STATE_COMPLETED);
        //数据转换准备发送给调度器
        WfTaskInstanceMessage wfTaskInstanceMessage = WfTaskInstanceConvert.INSTANCE.convertDOToMessage(wfTaskInstanceDO);
        wfTaskInstanceMessage.setRequiredData(requiredData);
        //sendScheduleRequestMessage(wfTaskInstanceMessage);
        sendMessageInTransaction(wfTaskInstanceMessage,wfTaskInstanceDO);
        return true;
    }

    public TransactionSendResult sendMessageInTransaction(WfTaskInstanceMessage wfTaskInstanceMessage,WfTaskInstanceDO wfTaskInstanceDO) {
        // 创建 Demo07Message 消息
        Message<ScheduleRequestMessage> message = MessageBuilder.withPayload(new ScheduleRequestMessage().setWfTaskInstanceMessage(wfTaskInstanceMessage))
                .build();
        // 发送事务消息,最后一个参数事务处理用
        return rocketMQTemplate.sendMessageInTransaction("task-transaction-producer-group", ScheduleRequestMessage.TOPIC, message, WfTaskInstanceConvert.INSTANCE.convertDOToDTO(wfTaskInstanceDO));
    }

    @Transactional
    @Override
    public void ending(WfTaskInstanceDTO wfTaskInstanceDTO){
        //进行历史记录
        WfTaskInstanceDO wfTaskInstanceDO = WfTaskInstanceConvert.INSTANCE.convertDTOToDO(wfTaskInstanceDTO);
        wfTaskInstanceMapper.updateById(wfTaskInstanceDO);
        WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO = WfTaskInstanceConvert.INSTANCE.convertRunToHistory(wfTaskInstanceDO);
        wfTaskHistoryInstanceDO.setCreatetime(new Date());
        wfTaskHistoryInstanceDO.setUpdatetime(wfTaskHistoryInstanceDO.getCreatetime());
        wfTaskHistoryInstanceMapper.insert(wfTaskHistoryInstanceDO);
        //从运行表中清除当前记录
        wfTaskInstanceMapper.deleteById(wfTaskInstanceDO.getId());
    }

    /**
     * 根据状态和aiId进行查询
     * @param wfTaskInstanceQueryDTO
     * @return
     */
    @Override
    public int count(WfTaskInstanceQueryDTO wfTaskInstanceQueryDTO) {
        HashMap<String,String> map = new HashMap<>();
        map.put("ti_status",wfTaskInstanceQueryDTO.getTiStatus());
        map.put("ai_id",wfTaskInstanceQueryDTO.getAiId());
        QueryWrapper<WfTaskInstanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(map);
        return wfTaskInstanceMapper.selectCount(queryWrapper);
    }


    @Override
    public List<WfTaskInstanceDTO> findRelatedTaskList(String aiId) {
        HashMap<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("ai_id",aiId);
        conditionMap.put("ti_status",TaskInstanceState.TASK_INSTANCE_STATE_COMPLETED);
        List<WfTaskInstanceDO> wfTaskInstanceDOList = wfTaskInstanceMapper.selectByMap(conditionMap);
        List<WfTaskInstanceDTO> wfTaskInstanceDTOList = new ArrayList<>();
        for(WfTaskInstanceDO wfTaskInstanceDO : wfTaskInstanceDOList){
            wfTaskInstanceDTOList.add(WfTaskInstanceConvert.INSTANCE.convertDOToDTO(wfTaskInstanceDO));
        }
        return wfTaskInstanceDTOList;
    }

    @Override
    public void moveRelatedTaskToHistory(String aiId) {
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("ai_id",aiId);
        List<WfTaskInstanceDO> wfTaskInstanceDOList = wfTaskInstanceMapper.selectByMap(conditionMap);
        for(WfTaskInstanceDO wfTaskInstanceDO : wfTaskInstanceDOList){
            WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO = WfTaskInstanceConvert.INSTANCE.convertRunToHistory(wfTaskInstanceDO);
            wfTaskHistoryInstanceDO.setTiStatus(TaskInstanceState.TASK_INSTANCE_STATE_PAST);
            wfTaskHistoryInstanceDO.setUpdatetime(new Date());
            wfTaskHistoryInstanceDO.setCreatetime(wfTaskHistoryInstanceDO.getUpdatetime());
            wfTaskHistoryInstanceMapper.insert(wfTaskHistoryInstanceDO);
            wfTaskInstanceMapper.deleteById(wfTaskInstanceDO.getId());
        }
    }

    @Override
    public WfTaskInstanceBO save(WfTaskInstanceDTO wfTaskInstanceDTO) {
        WfTaskInstanceDO wfTaskInstanceDO = WfTaskInstanceConvert.INSTANCE.convertDTOToDO(wfTaskInstanceDTO);
        wfTaskInstanceMapper.insert(wfTaskInstanceDO);
        //打时间戳记录
        WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO = WfTaskInstanceConvert.INSTANCE.convertRunToHistory(wfTaskInstanceDO);
        wfTaskHistoryInstanceDO.setCreatetime(new Date());
        wfTaskHistoryInstanceDO.setUpdatetime(wfTaskHistoryInstanceDO.getCreatetime());
        wfTaskHistoryInstanceMapper.insert(wfTaskHistoryInstanceDO);
        return WfTaskInstanceConvert.INSTANCE.convertDOToBO(wfTaskInstanceDO);
    }

    @Override
    public void recordHistory(WfTaskInstanceDTO wfTaskInstanceDTO) {
        WfTaskInstanceDO wfTaskInstanceDO = WfTaskInstanceConvert.INSTANCE.convertDTOToDO(wfTaskInstanceDTO);
        WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO = WfTaskInstanceConvert.INSTANCE.convertRunToHistory(wfTaskInstanceDO);
        wfTaskHistoryInstanceDO.setCreatetime(new Date());
        wfTaskHistoryInstanceDO.setUpdatetime(wfTaskHistoryInstanceDO.getCreatetime());
        wfTaskHistoryInstanceMapper.insert(wfTaskHistoryInstanceDO);
    }

    private void sendScheduleRequestMessage(WfTaskInstanceMessage wfTaskInstanceMessage) {
        rocketMQTemplate.convertAndSend(ScheduleRequestMessage.TOPIC, new ScheduleRequestMessage().setWfTaskInstanceMessage(wfTaskInstanceMessage));
    }
}
