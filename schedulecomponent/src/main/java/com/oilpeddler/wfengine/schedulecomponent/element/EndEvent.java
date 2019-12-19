package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.common.message.ProcessRequestMessage;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.WfActivtityInstanceService;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * <p>
 * 无指定结束事件类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class EndEvent extends Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();

    TokenMapper tokenMapper = applicationContext.getBean(TokenMapper.class);

    WfActivtityInstanceService wfActivtityInstanceService = applicationContext.getBean(WfActivtityInstanceService.class);;

    private RocketMQTemplate rocketMQTemplate = applicationContext.getBean(RocketMQTemplate.class);

    public void execute(Token token){
        //结束流程
        tokenMapper.deleteById(token.getId());
        wfActivtityInstanceService.clearActivityOfProcess(token.getPiId());
        sendProcessRequestMessage(token.getPiId());
    }

    private void sendProcessRequestMessage(String piId) {
        rocketMQTemplate.convertAndSend(ProcessRequestMessage.TOPIC, new ProcessRequestMessage().setPiId(piId));
    }
}
