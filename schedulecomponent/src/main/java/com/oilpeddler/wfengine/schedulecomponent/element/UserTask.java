package com.oilpeddler.wfengine.schedulecomponent.element;


import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.message.TaskRequestMessage;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.TokenService;
import com.oilpeddler.wfengine.schedulecomponent.service.WfActivtityInstanceService;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动要素类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class UserTask extends Node {
    /*private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();

    TokenMapper tokenMapper = applicationContext.getBean(TokenMapper.class);

    WfActivtityInstanceService wfActivtityInstanceService = applicationContext.getBean(WfActivtityInstanceService.class);
*/
    //TokenMapper tokenMapper = SpringUtil.getBean(TokenMapper.class);
    //WfActivtityInstanceService wfActivtityInstanceService = SpringUtil.getBean(WfActivtityInstanceService.class);
    //RocketMQTemplate rocketMQTemplate = SpringUtil.getBean(RocketMQTemplate.class);

    //private RocketMQTemplate rocketMQTemplate = applicationContext.getBean(RocketMQTemplate.class);

    /**
     * 活动名称
     */
    protected String name;

    /**
     * 活动参与人类型
     */
    protected String assigneeType;

    /**
     * 活动执行人
     */
    protected List<String> assignees;

    /**
     * 活动类型
     */
    protected String taskType;

    /**
     * 即席活动执行人
     */
    protected String dynamicAssignees;

    /**
     * 即席活动参与人类型
     */
    protected String dynamicAssigneeType;

    /**
     * 挂接表单页面
     */
    protected String pageKey;

    protected List<DataParam> paramList;

    public void execute(Token token){
        //TODO 真正开始执行节点，发送消息给表单服务开启任务
        token.setUpdatetime(new Date());
        if(token.getId() == null)
            SpringUtil.getBean(TokenMapper.class).insert(token);
        else
            SpringUtil.getBean(TokenMapper.class).updateById(token);
        List<BaseElement> readyExecuteUserTaskList = new ArrayList<>();
        readyExecuteUserTaskList.add(this);
        List<WfActivtityInstanceBO> wfActivtityInstanceBOList = SpringUtil.getBean(WfActivtityInstanceService.class).addActivityList(readyExecuteUserTaskList,token.getPiId(),token.getPdId());
        sendTaskRequestMessage(wfActivtityInstanceBOList);
    }

    private void sendTaskRequestMessage(List<WfActivtityInstanceBO> wfActivtityInstanceBOList) {
        SpringUtil.getBean(RocketMQTemplate.class).convertAndSend(TaskRequestMessage.TOPIC, new TaskRequestMessage().setWfActivtityInstanceBOList(wfActivtityInstanceBOList));
    }

    //TODO 调度器这事务消息有点问题，先停在这
    public TransactionSendResult sendMessageInTransaction(List<WfActivtityInstanceBO> wfActivtityInstanceBOList) {
        // 创建 Demo07Message 消息
        Message<TaskRequestMessage> message = MessageBuilder.withPayload(new TaskRequestMessage().setWfActivtityInstanceBOList(wfActivtityInstanceBOList))
                .build();
        // 发送事务消息,最后一个参数乱写的
        return SpringUtil.getBean(RocketMQTemplate.class).sendMessageInTransaction("schedule-transaction-producer-group", TaskRequestMessage.TOPIC, message, wfActivtityInstanceBOList);
    }
    /*protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();*/

}
