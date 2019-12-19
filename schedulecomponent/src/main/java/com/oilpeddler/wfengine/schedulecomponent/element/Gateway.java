package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
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

    private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();

    TokenMapper tokenMapper = applicationContext.getBean(TokenMapper.class);
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
        for(SequenceFlow sequenceFlow : outgoingFlows){
            Token childToken = new Token();
            childToken.setParent(token);
            childToken.setParentId(token.getId());
            childToken.setCurrentNode(this);
            childToken.setElementNo(no);
            childToken.setPdId(token.getPdId());
            childToken.setPiId(token.getPiId());
            token.getChildren().add(childToken);
            leave(childToken,sequenceFlow);
        }
    }

    public void merge(Token token){
        List<Token> concurrentTokens = token.getParent().getChildren();
        boolean reactivate = true;
        for(Token concurrentToken : concurrentTokens){
            if(concurrentToken.getCurrentNode() != token.getCurrentNode()){
                reactivate = false;
            }else{
                //TODO 到子令牌一个删一个，应该不会影响啥,但是等加了缓存机制之后恐怕就要修改下啦
                tokenMapper.deleteById(concurrentToken.getId());
            }
        }
        //删除当前的并发子节点，父节点直接到达当前merge网关节点
        if(reactivate){
            Token father = token.getParent();
            father.setCurrentNode(this);
            father.setChildren(new ArrayList<>());
            leave(father);
        }
    }
    /*protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();*/
}
