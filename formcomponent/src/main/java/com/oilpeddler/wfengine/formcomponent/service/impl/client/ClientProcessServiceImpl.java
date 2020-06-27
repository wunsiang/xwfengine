package com.oilpeddler.wfengine.formcomponent.service.impl.client;

import com.oilpeddler.wfengine.common.api.formservice.ClientProcessService;
import com.oilpeddler.wfengine.common.api.formservice.WfProcessParamsRelationService;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfProcessDefinitionService;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfProcessInstanceService;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.dataobject.ParmObject;
import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import com.oilpeddler.wfengine.formcomponent.dto.WfBusinessFormDTO;
import com.oilpeddler.wfengine.formcomponent.service.WfBusinessFormService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@Service
public class ClientProcessServiceImpl implements ClientProcessService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Reference
    WfProcessDefinitionService wfProcessDefinitionService;

    @Reference
    WfProcessInstanceService wfProcessInstanceService;

    @Autowired
    WfBusinessFormService wfBusinessFormService;

    @Autowired
    WfProcessParamsRelationService wfProcessParamsRelationService;

    @Override
    public boolean startProcess(String pdId, String piName, String piStarter, String piBusinesskey, Map<String, ParmObject> requiredData) {
        WfProcessInstanceMessage wfProcessInstanceMessage = new WfProcessInstanceMessage();
        Map<String, ParmObject> engineData = new HashMap<>();
        for (Map.Entry<String, ParmObject> entry : requiredData.entrySet()){
            ParmObject parmObject = new ParmObject();
            WfProcessParamsRelationDTO wfProcessParamsRelationDTO = wfProcessParamsRelationService.getEnginePpName(pdId,entry.getKey(),"startevent1");
            parmObject.setPpType(wfProcessParamsRelationDTO.getPpType());
            parmObject.setVal(entry.getValue().getVal());
            engineData.put(wfProcessParamsRelationDTO.getEnginePpName(),parmObject);
        }
        wfProcessInstanceMessage.setPdId(pdId)
                .setPiBusinesskey(piBusinesskey)
                .setPiName(piName)
                .setPiStarter(piStarter)
                .setRequiredData(engineData);
        sendMessageInTransaction(wfProcessInstanceMessage);
        return true;
    }

    @Override
    public List<WfProcessDefinitionBO> queryDefinitionList() {
        List<WfProcessDefinitionBO> wfProcessDefinitionBOList = wfProcessDefinitionService.queryDefinitionList();
        for(WfProcessDefinitionBO wfProcessDefinitionBO : wfProcessDefinitionBOList){
            WfBusinessFormDTO wfBusinessFormDTO = wfBusinessFormService.selectById(wfProcessDefinitionBO.getStartForm());
            wfProcessDefinitionBO.setStartForm(wfBusinessFormDTO.getFormUrl());
        }
        return wfProcessDefinitionBOList;
    }

    @Override
    public void changeProcessState(String piId, String state) {
        wfProcessInstanceService.changeProcessState(piId,state);
    }

    @Override
    public List<WfProcessInstanceBO> getProcessListByUserId(String piStarter) {
        return wfProcessInstanceService.getProcessListByUserId(piStarter);
    }

    public TransactionSendResult sendMessageInTransaction(WfProcessInstanceMessage wfProcessInstanceMessage) {
        // 创建 Demo07Message 消息
        Message<ScheduleRequestMessage> message = MessageBuilder.withPayload(new ScheduleRequestMessage().setWfProcessInstanceMessage(wfProcessInstanceMessage))
                .build();
        // 发送事务消息,最后一个参数事务处理用
        return rocketMQTemplate.sendMessageInTransaction("form-transaction-producer-group", ScheduleRequestMessage.TOPIC, message, null);
    }

}
