package com.oilpeddler.wfengine.taskmanagecomponent.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskHistoryInstanceService;
import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskInstanceService;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceCategory;
import com.oilpeddler.wfengine.common.constant.TaskInstanceState;
import com.oilpeddler.wfengine.common.message.TaskRequestMessage;
import com.oilpeddler.wfengine.taskmanagecomponent.convert.WfTaskInstanceConvert;
import com.oilpeddler.wfengine.taskmanagecomponent.dataobject.WfTaskInstanceDO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RocketMQMessageListener(
        topic = TaskRequestMessage.TOPIC,
        consumerGroup = "task-consumer-group")
public class TaskManageConsumer implements RocketMQListener<TaskRequestMessage> {

    @Autowired
    WfTaskInstanceService wfTaskInstanceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public void onMessage(TaskRequestMessage taskRequestMessage) {
        //幂等性保证
        if(taskRequestMessage.getWfActivtityInstanceBOList() == null || taskRequestMessage.getWfActivtityInstanceBOList().size() == 0)
            return;
        boolean absentBoolean = stringRedisTemplate.opsForValue().setIfAbsent(taskRequestMessage.getWfActivtityInstanceBOList().get(0).getId() + taskRequestMessage.getWfActivtityInstanceBOList().get(0).getUpdatetime(),"1",1000, TimeUnit.SECONDS);
        if(!absentBoolean)
            return;
        for(WfActivtityInstanceBO wfActivtityInstanceBO : taskRequestMessage.getWfActivtityInstanceBOList()){
            if(wfActivtityInstanceBO.getAiCategory().equals(ActivityInstanceCategory.ACTIVITY_CATEGORY_SINGLE) || wfActivtityInstanceBO.getAiCategory().equals(ActivityInstanceCategory.ACTIVITY_CATEGORY_SINGLE_COUNTSIGN)) {
                List<String> assigners = JSON.parseObject(wfActivtityInstanceBO.getAiAssignerId(), new TypeReference<List<String>>() {});
                pushAllTask(wfActivtityInstanceBO,assigners);
            }/*else if(wfActivtityInstanceBO.getAiCategory().equals(ActivityInstanceCategory.ACTIVITY_CATEGORY_POSITION_COUNTSIGN)){
                //TODO wenxiang岗位会签，因无明确需求，暂时不处理
            }*/
        }
    }

    private void pushAllTask(WfActivtityInstanceBO wfActivtityInstanceBO,List<String> assigners){
        for(String assignerId : assigners){
            WfTaskInstanceDO wfTaskInstanceDO = new WfTaskInstanceDO()
                    .setTiName(wfActivtityInstanceBO.getAiName())
                    .setTiAssigner(assignerId)
                    .setTiStatus(TaskInstanceState.TASK_INSTANCE_STATE_RUNNING)
                    .setBfId(wfActivtityInstanceBO.getBfId())
                    .setAiId(wfActivtityInstanceBO.getId())
                    .setPdId(wfActivtityInstanceBO.getPdId())
                    .setPiId(wfActivtityInstanceBO.getPiId());
            wfTaskInstanceDO.setCreatetime(new Date());
            wfTaskInstanceDO.setUpdatetime(wfTaskInstanceDO.getCreatetime());
            wfTaskInstanceService.save(WfTaskInstanceConvert.INSTANCE.convertDOToDTO(wfTaskInstanceDO));
        }
    }
}
