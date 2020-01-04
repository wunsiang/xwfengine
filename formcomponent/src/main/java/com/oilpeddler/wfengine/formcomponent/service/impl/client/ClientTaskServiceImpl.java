package com.oilpeddler.wfengine.formcomponent.service.impl.client;

import com.oilpeddler.wfengine.common.api.formservice.ClientTaskService;
import com.oilpeddler.wfengine.common.api.formservice.WfProcessParamsRelationService;
import com.oilpeddler.wfengine.common.constant.TaskInstanceState;
import com.oilpeddler.wfengine.common.dataobject.ParmObject;
import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@org.apache.dubbo.config.annotation.Service
public class ClientTaskServiceImpl implements ClientTaskService {

    @Autowired
    WfProcessParamsRelationService wfProcessParamsRelationService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean completeTask(String tiId, String pdId, String taskNo, Map<String, Object> requiredData) {

        WfTaskInstanceMessage wfTaskInstanceMessage = new WfTaskInstanceMessage();
        wfTaskInstanceMessage.setId(tiId);

        Map<String, ParmObject> engineData = new HashMap<>();
        for (Map.Entry<String, Object> entry : requiredData.entrySet()){
            ParmObject parmObject = new ParmObject();
            WfProcessParamsRelationDTO wfProcessParamsRelationDTO = wfProcessParamsRelationService.getEnginePpName(pdId,entry.getKey(),taskNo);
            parmObject.setPpType(wfProcessParamsRelationDTO.getPpType());
            parmObject.setVal(entry.getValue());
            engineData.put(wfProcessParamsRelationDTO.getEnginePpName(),parmObject);
        }
        wfTaskInstanceMessage.setRequiredData(engineData);
        sendMessageInTransaction(wfTaskInstanceMessage);
        return true;
    }

    public TransactionSendResult sendMessageInTransaction(WfTaskInstanceMessage wfTaskInstanceMessage) {
        // 创建 Demo07Message 消息
        Message<ScheduleRequestMessage> message = MessageBuilder.withPayload(new ScheduleRequestMessage().setWfTaskInstanceMessage(wfTaskInstanceMessage))
                .build();
        // 发送事务消息,最后一个参数事务处理用
        return rocketMQTemplate.sendMessageInTransaction("form-transaction-producer-group", ScheduleRequestMessage.TOPIC, message, null);
    }

}
