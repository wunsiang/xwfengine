package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网关抽象基类
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public abstract class Gateway extends Node {

    //private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();

    //TokenMapper tokenMapper = applicationContext.getBean(TokenMapper.class);
    //TokenMapper tokenMapper = SpringUtil.getBean(TokenMapper.class);
    /**
     * 网关名称
     */
    protected String name;

    public void execute(Token token){
        if(outgoingFlows.size() > 1){
            fork(token);
        }else {
            merge(token);
        }
    }

    public void fork(Token token){
        token.setChildNum(outgoingFlows.size());
        SpringUtil.getBean(TokenMapper.class).updateById(token);
        //TODO 。。。
        for(SequenceFlow sequenceFlow : outgoingFlows){
            Token childToken = new Token();
            childToken.setParent(token);
            childToken.setParentId(token.getId());
            childToken.setCurrentNode(this);
            childToken.setElementNo(no);
            childToken.setPdId(token.getPdId());
            childToken.setPiId(token.getPiId());
            //主要是为了拿Id
            SpringUtil.getBean(TokenMapper.class).insert(childToken);
            token.getChildren().add(childToken);
            leave(childToken,sequenceFlow);
        }
    }

    public void merge(Token token){
        //TODO 判断当前token是不是父token的最后一个子token，如果是就把爹弄来，不是就删了自个了事
        TokenMapper tokenMapper = SpringUtil.getBean(TokenMapper.class);
        tokenMapper.deleteById(token.getId());
        tokenMapper.decrementChildNum(token.getParentId());
        Token parentToken = tokenMapper.selectById(token.getParentId());
        if(parentToken.getChildNum() == 0){
            parentToken.setCurrentNode(this);
            parentToken.setElementNo(this.getNo());
            parentToken.setChildren(new ArrayList<>());
            leave(parentToken);
        }

        /*List<Token> concurrentTokens = token.getParent().getChildren();
        boolean reactivate = true;
        for(Token concurrentToken : concurrentTokens){
            if(concurrentToken.getCurrentNode() != token.getCurrentNode()){
                reactivate = false;
            }else{
                SpringUtil.getBean(TokenMapper.class).deleteById(concurrentToken.getId());
            }
        }
        //删除当前的并发子节点，父节点直接到达当前merge网关节点
        if(reactivate){
            Token father = token.getParent();
            father.setCurrentNode(this);
            father.setChildren(new ArrayList<>());
            leave(father);
        }*/
    }
    /*protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();*/
}
