package com.oilpeddler.wfengine.processmanagecomponent.mq;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessInstanceService;
import com.oilpeddler.wfengine.common.message.ProcessRequestMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
        topic = ProcessRequestMessage.TOPIC,
        consumerGroup = "process-consumer-group")
public class ProcessRequestConsumer implements RocketMQListener<ProcessRequestMessage> {

    @Autowired
    WfProcessInstanceService wfProcessInstanceService;

    @Override
    public void onMessage(ProcessRequestMessage processRequestMessage) {
        wfProcessInstanceService.endProcess(processRequestMessage.getPiId());
    }
}
