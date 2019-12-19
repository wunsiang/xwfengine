package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 无指定开始事件类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-12-17
 */
@Data
@Accessors(chain = true)
public class StartEvent extends Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();
    TokenMapper tokenMapper = applicationContext.getBean(TokenMapper.class);

    public void execute(Token token){
        //开始流程,目前的设计是，只有usertask会持久化token到数据库
        token.setCreatetime(new Date());
        //这块本没必要入库，但是考虑到如果流程一开始就进入fork的情况，需要parentId，所以先入库拿Id
        tokenMapper.insert(token);
        leave(token);
    }
}
