package com.oilpeddler.wfengine.schedulecomponent.tools.handler;

import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.element.SequenceFlow;
import com.oilpeddler.wfengine.common.element.StartEvent;
import com.oilpeddler.wfengine.schedulecomponent.tools.JexlUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class StartEventPathParseHandler extends AbstractPathParseHandler{

    @Override
    public void parseOrPass(List<BaseElement> currentElementList,  Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId,String pdId) {
        for(BaseElement currentElement : currentElementList){
            if(currentElement instanceof StartEvent){
                delList.add(currentElement);
                pathParse(currentElement,process,addList,delList,processInstanceId,pdId);
                break;
            }
        }
        nextPathParseHandler.parseOrPass(currentElementList,process,addList,delList,processInstanceId,pdId);
    }

    @Override
    protected void pathParse(BaseElement currentElement,  Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId,String pdId) {
        if(currentElement instanceof StartEvent){
            StartEvent currentStartEvent = (StartEvent)currentElement;
            //遍历currentStartEvent的出边集合，找出接下来的出边,按照画图规则，可能是网关，也可能是活动或者事件，但这一级应该只有一个元素
            for(SequenceFlow sequenceFlow : currentStartEvent.getOutgoingFlows()){
                addList.add(findMatchElement(sequenceFlow.getTargetRef(),process));
            }
        }
    }
}
