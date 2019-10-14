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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RocketMQMessageListener(
        topic = TaskRequestMessage.TOPIC,
        consumerGroup = "task-consumer-group")
public class TaskManageConsumer implements RocketMQListener<TaskRequestMessage> {

    @Autowired
    WfTaskInstanceService wfTaskInstanceService;

    @Override
    public void onMessage(TaskRequestMessage taskRequestMessage) {
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
                    .setAiId(wfActivtityInstanceBO.getId());
            wfTaskInstanceDO.setCreatetime(new Date());
            wfTaskInstanceDO.setUpdatetime(wfTaskInstanceDO.getCreatetime());
            wfTaskInstanceService.save(WfTaskInstanceConvert.INSTANCE.convertDOToDTO(wfTaskInstanceDO));
        }
    }
}
