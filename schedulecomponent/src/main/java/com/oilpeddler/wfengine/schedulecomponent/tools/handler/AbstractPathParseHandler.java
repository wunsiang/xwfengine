package com.oilpeddler.wfengine.schedulecomponent.tools.handler;

import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.Process;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public abstract class AbstractPathParseHandler {
    protected AbstractPathParseHandler nextPathParseHandler;
    abstract public void parseOrPass(List<BaseElement> currentElementList, Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId,String pdId);
    abstract protected void pathParse(BaseElement currentElement, Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId,String pdId);
    public BaseElement findMatchElement(String elementNo,Process process){
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
