package com.oilpeddler.wfengine.common.tools;

import com.oilpeddler.wfengine.common.constant.BpmnXMLConstants;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.element.*;
import org.dom4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * BpmnXML转换工具类
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-23
 */
public class BpmnXMLConvertUtil {
    /**
     * 将字符串形式的流程描述文件内容转换为对象
     * @param ptContent
     * @return
     */
    public static BpmnModel ConvertToBpmnModel(String ptContent){
        try {
            Document document = DocumentHelper.parseText(ptContent);
            Element root = document.getRootElement();
            BpmnModel bpmnModel = new BpmnModel();
            //遍历根节点的所有子节点
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()){
                Element currentElement = iterator.next();
                switch (currentElement.getName()){
                    case BpmnXMLConstants.ELEMENT_PROCESS:
                        bpmnModel.setProcess(ConvertToProcess(currentElement));
                        break;
                }
            }
            //提取BpmnModel级别的属性
            List<Attribute> attributeList = root.attributes();
            for(Attribute attribute : attributeList){
                switch (attribute.getName()){
                    case BpmnXMLConstants.ATTRIBUTE_NO:
                        bpmnModel.setNo(attribute.getValue());
                        break;
                    case BpmnXMLConstants.ATTRIBUTE_NAME:
                        bpmnModel.setName(attribute.getValue());
                        break;
                }
            }
            return bpmnModel;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转换Process element为Process对象
     * @param element
     * @return
     */
    private static Process ConvertToProcess(Element element){
        Process process = new Process();
        //提取process的属性
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    process.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    process.setName(attribute.getValue());
                    break;
            }
        }

        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()){
            Element currentElement = iterator.next();
            switch (currentElement.getName()){
                case BpmnXMLConstants.ELEMENT_TASK_USER:
                    process.getUserTaskList().add(ConvertToUserTask(currentElement));
                    break;
                case BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW:
                    process.getSequenceFlowList().add(ConvertToSequenceFlow(currentElement));
                    break;
                case BpmnXMLConstants.ELEMENT_GATEWAY_EXCLUSIVE:
                    process.getGatewayList().add(ConvertToExclusiveGateway(currentElement));
                    break;
                case BpmnXMLConstants.ELEMENT_GATEWAY_PARALLEL:
                    process.getGatewayList().add(ConvertToparallelGateway(currentElement));
                    break;
                case BpmnXMLConstants.ELEMENT_EVENT_START:
                    process.getEventList().add(ConvertToStartEvent(currentElement));
                    break;
                case BpmnXMLConstants.ELEMENT_EVENT_END:
                    process.getEventList().add(ConvertToEndEvent(currentElement));
                    break;
            }
        }
        return process;
    }

    /**
     * 转换UserTask element为UserTask对象
     * @param element
     * @return
     */
    private static UserTask ConvertToUserTask(Element element){
        UserTask userTask = new UserTask();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    userTask.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    userTask.setName(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_TASK_USER_ASSIGNEETYPE:
                    userTask.setAssigneeType(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_TASK_USER_ASSIGNEE:
                    String[] assigneesArray = attribute.getValue().split(",");
                    List<String> assigneesList = Arrays.asList(assigneesArray);
                    userTask.setAssignees(assigneesList);
                    break;
                case BpmnXMLConstants.ATTRIBUTE_TASK_USER_TYPE:
                    userTask.setAssigneeType(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_TASK_USER_PAGEKEY:
                    userTask.setPageKey(attribute.getValue());
                    break;
            }
        }
        return userTask;
    }

    /**
     * 转换SequenceFlow element为SequenceFlow对象
     * @param element
     * @return
     */
    private static SequenceFlow ConvertToSequenceFlow(Element element){
        SequenceFlow sequenceFlow = new SequenceFlow();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    sequenceFlow.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    sequenceFlow.setName(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_SEQUENCE_FLOW_CONDITION:
                    sequenceFlow.setConditionExpression(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_SEQUENCE_FLOW_SOURCE_REF:
                    sequenceFlow.setSourceRef(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_SEQUENCE_FLOW_TARGET_REF:
                    sequenceFlow.setTargetRef(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_SEQUENCE_FLOW_PARAMLIST:
                    String content = attribute.getValue();
                    String[] params = content.split(";");
                    List<DataParam> dataParamList = new ArrayList<>();
                    for(String paramData : params){
                        String[] attrs = paramData.split(",");
                        DataParam dataParam = new DataParam().setTaskNo(attrs[0])
                                .setPpName(attrs[1])
                                .setPpType(attrs[2])
                                .setEnginePpName(attrs[4]);
                        dataParamList.add(dataParam);
                    }
                    sequenceFlow.setParamList(dataParamList);
            }
        }
        return sequenceFlow;
    }

    /**
     * 转换ExclusiveGateway element为ExclusiveGateway对象
     * @param element
     * @return
     */
    private static ExclusiveGateway ConvertToExclusiveGateway(Element element){
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    exclusiveGateway.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    exclusiveGateway.setName(attribute.getValue());
                    break;
            }
        }
        return exclusiveGateway;
    }

    /**
     * 转换ParallelGateway element为ParallelGateway对象
     * @param element
     * @return
     */
    private static ParallelGateway ConvertToparallelGateway(Element element){
        ParallelGateway parallelGateway = new ParallelGateway();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    parallelGateway.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    parallelGateway.setName(attribute.getValue());
                    break;
            }
        }
        return parallelGateway;
    }

    /**
     * 转换StartEvent element为StartEvent对象
     * @param element
     * @return
     */
    private static StartEvent ConvertToStartEvent(Element element){
        StartEvent startEvent = new StartEvent();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    startEvent.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    startEvent.setName(attribute.getValue());
                    break;
            }
        }
        return startEvent;
    }

    /**
     * 转换EndEvent element为EndEvent对象
     * @param element
     * @return
     */
    private static EndEvent ConvertToEndEvent(Element element){
        EndEvent endEvent = new EndEvent();
        List<Attribute> attributeList = element.attributes();
        for(Attribute attribute : attributeList){
            switch (attribute.getName()){
                case BpmnXMLConstants.ATTRIBUTE_NO:
                    endEvent.setNo(attribute.getValue());
                    break;
                case BpmnXMLConstants.ATTRIBUTE_NAME:
                    endEvent.setName(attribute.getValue());
                    break;
            }
        }
        return endEvent;
    }

    public static BaseElement findMatchElement(String elementNo,Process process){
        for(BaseElement currentElement : process.getUserTaskList()){
            if(currentElement.getNo().equals(elementNo)){
                return currentElement;
            }
        }

        for (BaseElement currentElement : process.getEventList()){
            if(currentElement.getNo().equals(elementNo)){
                return currentElement;
            }
        }

        for (BaseElement currentElement : process.getGatewayList()){
            if(currentElement.getNo().equals(elementNo)){
                return currentElement;
            }
        }
        return null;
    }

}
