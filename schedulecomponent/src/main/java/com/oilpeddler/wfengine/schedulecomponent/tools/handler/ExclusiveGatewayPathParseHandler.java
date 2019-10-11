package com.oilpeddler.wfengine.schedulecomponent.tools.handler;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.common.constant.ParamType;
import com.oilpeddler.wfengine.common.element.*;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.schedulecomponent.tools.JexlUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExclusiveGatewayPathParseHandler extends AbstractPathParseHandler {
    @Reference
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Override
    public void parseOrPass(List<BaseElement> currentElementList,  Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId) {
        List<BaseElement> currentLevelResultList = currentElementList;
        for(BaseElement currentElement : currentElementList){
            if(currentElement instanceof ExclusiveGateway){
                delList.add(currentElement);
                pathParse(currentElement,process,addList,delList,processInstanceId);
            }
        }
        nextPathParseHandler.parseOrPass(currentLevelResultList,process,addList,delList,processInstanceId);
    }

    @Override
    protected void pathParse(BaseElement currentElement, Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId) {
        List<BaseElement> resultList = new ArrayList<>();
        ExclusiveGateway currentExclusiveGateway = (ExclusiveGateway)currentElement;
        //若入度大于1，则说明为聚合排他网关，则无脑通过(且判断非双功能)
        if(currentExclusiveGateway.getIncomingFlows().size() > 1 && currentExclusiveGateway.getOutgoingFlows().size() == 1){
            addList.add(findMatchElement(currentExclusiveGateway.getOutgoingFlows().get(0).getTargetRef(),process));
        }
        else {
            for(SequenceFlow sequenceFlow : currentExclusiveGateway.getOutgoingFlows()){
                //条件判断开始
                Map<String,Object> requiredData = new HashMap<>();
                for(DataParam dataParam : sequenceFlow.getParamList()) {
                    WfProcessParamsRecordBO wfProcessParamsRecordBO = wfProcessParamsRecordService.getByEnginePpName(dataParam.getEnginePpName(),processInstanceId,dataParam.getTaskNo());
                    if(wfProcessParamsRecordBO != null){
                        switch (wfProcessParamsRecordBO.getPpType()){
                            case ParamType.PARAM_TYPE_BOOL:
                                requiredData.put(dataParam.getEnginePpName(),wfProcessParamsRecordBO.getPpRecordValue().equals(1) ? true : false);
                                break;
                            case ParamType.PARAM_TYPE_INT:
                                requiredData.put(dataParam.getEnginePpName(),Integer.parseInt(wfProcessParamsRecordBO.getPpRecordValue()));
                                break;
                            case ParamType.PARAM_TYPE_FLOAT:
                                requiredData.put(dataParam.getEnginePpName(),Float.parseFloat(wfProcessParamsRecordBO.getPpRecordValue()));
                                break;
                            case ParamType.PARAM_TYPE_STRING:
                                requiredData.put(dataParam.getEnginePpName(),wfProcessParamsRecordBO.getPpRecordValue());
                                break;
                        }
                    }
                }
                if(JexlUtil.conditionIsMacth(sequenceFlow.getConditionExpression(),requiredData)){
                    addList.add(findMatchElement(sequenceFlow.getTargetRef(),process));
                    break;
                }
            }
        }
    }
}
