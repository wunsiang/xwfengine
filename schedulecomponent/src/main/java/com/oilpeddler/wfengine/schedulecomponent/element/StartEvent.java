package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.constant.TaskInstanceState;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessInstanceMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfTaskInstanceMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.service.WfActivtityInstanceService;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.List;

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
public class StartEvent extends Event {

    /**
     * 挂接表单页面
     */
    protected String pageKey;

    protected List<DataParam> paramList;

    public void execute(Token token){
        //开始流程,目前的设计是，只有usertask会持久化token到数据库
        token.setCreatetime(new Date());
        //这块本没必要入库，但是考虑到如果流程一开始就进入fork的情况，需要parentId，所以先入库拿Id
        SpringUtil.getBean(TokenMapper.class).insert(token);
        WfProcessInstanceDO wfProcessInstanceDO =  SpringUtil.getBean(WfProcessInstanceMapper.class).selectById(token.getPiId());
        WfActivtityInstanceBO wfActivtityInstanceBO = SpringUtil.getBean(WfActivtityInstanceService.class).addStartEventActivity(this,token.getPiId(),token.getPdId(),wfProcessInstanceDO.getPiStarter());
        pushTask(wfActivtityInstanceBO,wfProcessInstanceDO.getPiStarter());
        //SpringUtil.getBean(TokenCacheDao.class).set(token.getId(),token);
        //leave(token);
    }

    public WfTaskInstanceDO pushTask(WfActivtityInstanceBO wfActivtityInstanceBO,String ass){
        WfTaskInstanceDO wfTaskInstanceDO = new WfTaskInstanceDO()
                .setTiName(wfActivtityInstanceBO.getAiName())
                .setTiAssigner(ass)
                .setTiStatus(TaskInstanceState.TASK_INSTANCE_STATE_RUNNING)
                .setBfId(wfActivtityInstanceBO.getBfId())
                .setAiId(wfActivtityInstanceBO.getId())
                .setPdId(wfActivtityInstanceBO.getPdId())
                .setTiAssignerType(wfActivtityInstanceBO.getAiAssignerType())
                .setPiId(wfActivtityInstanceBO.getPiId())
                .setUsertaskNo(wfActivtityInstanceBO.getUsertaskNo());
        wfTaskInstanceDO.setCreatetime(new Date());
        wfTaskInstanceDO.setUpdatetime(wfTaskInstanceDO.getCreatetime());
        SpringUtil.getBean(WfTaskInstanceMapper.class).insert(wfTaskInstanceDO);
        SpringUtil.getBean(StringRedisTemplate.class).opsForValue().set(wfTaskInstanceDO.getId(),"1");
        return wfTaskInstanceDO;
    }
}
