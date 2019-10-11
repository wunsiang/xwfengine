package com.oilpeddler.wfengine.schedulecomponent.tools;

import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.element.UserTask;
import com.oilpeddler.wfengine.schedulecomponent.tools.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PathParseUtil {

    @Autowired
    StartEventPathParseHandler startEventPathParseHandler;

    @Autowired
    ExclusiveGatewayPathParseHandler exclusiveGatewayPathParseHandler;

    @Autowired
    ParallelGatewayPathParseHandler parallelGatewayPathParseHandler;

/*    public PathParseUtil(){

    }*/

    /**
     * 责任链入口
     * @param elementList
     * @param process
     * @param addList
     * @param delList
     * @param processInstanceId
     * @return
     */
    public void startHandle(List<BaseElement> elementList, Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId){
        startEventPathParseHandler.setNextPathParseHandler(exclusiveGatewayPathParseHandler);
        exclusiveGatewayPathParseHandler.setNextPathParseHandler(parallelGatewayPathParseHandler);
        startEventPathParseHandler.parseOrPass(elementList,process,addList,delList,processInstanceId);
    }
}
