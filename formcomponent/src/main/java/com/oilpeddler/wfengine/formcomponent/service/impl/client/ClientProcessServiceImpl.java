package com.oilpeddler.wfengine.formcomponent.service.impl.client;

import com.oilpeddler.wfengine.common.api.formservice.ClientProcessService;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@org.apache.dubbo.config.annotation.Service
public class ClientProcessServiceImpl implements ClientProcessService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean startProcess(String pdId, String piName, String piStarter, String piBusinesskey) {
        WfProcessInstanceMessage wfProcessInstanceMessage = new WfProcessInstanceMessage();
        wfProcessInstanceMessage.setPdId(pdId)
                .setPiBusinesskey(piBusinesskey)
                .setPiName(piName)
                .setPiStarter(piStarter);
        sendMessageInTransaction(wfProcessInstanceMessage);
        return true;
    }

    public TransactionSendResult sendMessageInTransaction(WfProcessInstanceMessage wfProcessInstanceMessage) {
        // 创建 Demo07Message 消息
        Message<ScheduleRequestMessage> message = MessageBuilder.withPayload(new ScheduleRequestMessage().setWfProcessInstanceMessage(wfProcessInstanceMessage))
                .build();
        // 发送事务消息,最后一个参数事务处理用
        return rocketMQTemplate.sendMessageInTransaction("form-transaction-producer-group", ScheduleRequestMessage.TOPIC, message, null);
    }

}
