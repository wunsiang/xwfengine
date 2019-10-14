package com.oilpeddler.wfengine.schedulecomponent.tools.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.element.BaseElement;
import com.oilpeddler.wfengine.common.element.ParallelGateway;
import com.oilpeddler.wfengine.common.element.Process;
import com.oilpeddler.wfengine.common.element.SequenceFlow;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfElementMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfElementDO;
import com.oilpeddler.wfengine.schedulecomponent.tools.JexlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//并行网关不判断条件
@Component
public class ParallelGatewayPathParseHandler extends AbstractPathParseHandler{

    @Autowired
    WfElementMapper wfElementMapper;
    @Override
    public void parseOrPass(List<BaseElement> currentElementList,Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId) {
        List<BaseElement> currentLevelResultList = currentElementList;
        for(BaseElement currentElement : currentElementList){
            if(currentElement instanceof ParallelGateway){
                delList.add(currentElement);
                pathParse(currentElement,process,addList,delList,processInstanceId);
            }
        }
        //nextPathParseHandler.parseOrPass(currentLevelResultList,requiredData,process,addList,delList,processInstanceId);
    }

    @Override
    protected void pathParse(BaseElement currentElement, Process process,List<BaseElement> addList,List<BaseElement> delList,String processInstanceId) {
        ParallelGateway currentParallelGateway = (ParallelGateway)currentElement;
        //ToDo wenxiang这块其实只可能有一个元素，还在考虑后期要不要直接改成get(0)，好像也不能一个元素也没有
        //若为聚合并行网关，则需要修改并判定当前没获取到的令牌数目，若还有令牌没拿到，则等待
        if(currentParallelGateway.getIncomingFlows().size() > 1){
            QueryWrapper<WfElementDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("element_no", currentParallelGateway.getNo());
            queryWrapper.eq("element_role",2);
            queryWrapper.eq("element_process_id",processInstanceId);
            WfElementDO wfElementDO = wfElementMapper.selectOne(queryWrapper);
            wfElementDO.setTokenNumber(wfElementDO.getTokenNumber() - 1);
            wfElementMapper.updateById(wfElementDO);
            if(wfElementDO.getTokenNumber() != 0){
                return;
            }else {
                wfElementMapper.deleteById(wfElementDO);
            }
        }
        if(currentParallelGateway.getOutgoingFlows().size() > 1){
/*            //分支功能的并行网关存储发放令牌数量
            WfElementDO wfElementDO = new WfElementDO();
            wfElementDO.setElementNo(currentParallelGateway.getNo());
            wfElementDO.setElementProcessId(processInstanceId);
            wfElementDO.setTokenNumber(currentParallelGateway.getOutgoingFlows().size());
            wfElementDO.setCreatetime(new Date());
            wfElementDO.setUpdatetime(new Date());
            wfElementDO.setElementRole(1);
            wfElementMapper.insert(wfElementDO);*/
            //对应的关联聚合作用的并行网关
            String relatedParallelGateway = currentParallelGateway.getRelatedGateWay();
            WfElementDO relatedWfElementDO = new WfElementDO();
            relatedWfElementDO.setElementNo(relatedParallelGateway);
            relatedWfElementDO.setElementProcessId(processInstanceId);
            relatedWfElementDO.setTokenNumber(currentParallelGateway.getOutgoingFlows().size());
            relatedWfElementDO.setCreatetime(new Date());
            relatedWfElementDO.setUpdatetime(new Date());
            relatedWfElementDO.setElementRole(2);
            wfElementMapper.insert(relatedWfElementDO);
        }
        //无论是分支功能的并行网关还是聚合的并行网关往下走，都不需要条件判断，无脑走
        for(SequenceFlow sequenceFlow : currentParallelGateway.getOutgoingFlows()){
            //条件判断开始
            addList.add(findMatchElement(sequenceFlow.getTargetRef(),process));
        }
    }
}
