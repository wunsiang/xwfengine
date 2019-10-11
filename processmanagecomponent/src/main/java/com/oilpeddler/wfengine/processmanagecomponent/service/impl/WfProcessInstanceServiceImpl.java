package com.oilpeddler.wfengine.processmanagecomponent.service.impl;

import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessInstanceService;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfProcessInstanceStartDTO;
import com.oilpeddler.wfengine.common.message.ScheduleRequestMessage;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.processmanagecomponent.constant.ProcessInstanceState;
import com.oilpeddler.wfengine.processmanagecomponent.convert.WfProcessInstanceConvert;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessHistoryInstanceMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessInstanceMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessDefinitionDO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessHistoryInstanceDO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessInstanceDO;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 流程管理器开启新流程RPC服务
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-08
 */
//测试用
@org.springframework.stereotype.Service
@Service
public class WfProcessInstanceServiceImpl implements WfProcessInstanceService {
    @Autowired
    WfProcessInstanceMapper wfProcessInstanceMapper;

    @Autowired
    WfProcessHistoryInstanceMapper wfProcessHistoryInstanceMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 向数据库wf_process_instance表中插入一条新纪录，之后通过mq向调度器请求调度任务
     * @param wfProcessInstanceStartDTO
     * @return
     */
    @Override
    public boolean startProcess(WfProcessInstanceStartDTO wfProcessInstanceStartDTO) {
        WfProcessInstanceDO wfProcessInstanceDO = WfProcessInstanceConvert.INSTANCE.convertStartDTOToDO(wfProcessInstanceStartDTO);
        wfProcessInstanceDO.setPiStatus(ProcessInstanceState.PROCESS_INSTANCE_STATE_RUNNING);
        wfProcessInstanceDO.setCreatetime(new Date());
        wfProcessInstanceDO.setUpdatetime(wfProcessInstanceDO.getCreatetime());
        int count = wfProcessInstanceMapper.insert(wfProcessInstanceDO);
        if(count > 0){
            //向调度器请求调度任务
            WfProcessInstanceMessage wfProcessInstanceMessage = WfProcessInstanceConvert.INSTANCE.convertDOToMessage(wfProcessInstanceDO);
            sendScheduleRequestMessage(wfProcessInstanceMessage);
        }
        return true;
    }

    /**
     * 终结流程
     * @param piId
     */
    @Override
    public void endProcess(String piId) {
        //修改状态并删除运行实例表中的数据，并在历史表中进行时间戳记录
        WfProcessInstanceDO wfProcessInstanceDO = wfProcessInstanceMapper.selectById(piId);
        wfProcessInstanceDO.setPiStatus(ProcessInstanceState.PROCESS_INSTANCE_STATE_COMPLETED);
        wfProcessInstanceDO.setUpdatetime(new Date());
        wfProcessInstanceDO.setEndtime(wfProcessInstanceDO.getUpdatetime());
        WfProcessHistoryInstanceDO wfProcessHistoryInstanceDO = WfProcessInstanceConvert.INSTANCE.convertRunToHistory(wfProcessInstanceDO);
        wfProcessHistoryInstanceDO.setCreatetime(new Date());
        wfProcessHistoryInstanceDO.setUpdatetime(wfProcessHistoryInstanceDO.getCreatetime());
        wfProcessInstanceMapper.deleteById(piId);
        wfProcessHistoryInstanceMapper.insert(wfProcessHistoryInstanceDO);
    }

    public WfProcessInstanceBO getById(String id){
        WfProcessInstanceDO wfProcessInstanceDO = wfProcessInstanceMapper.selectById(id);
        return WfProcessInstanceConvert.INSTANCE.convertDOToBO(wfProcessInstanceDO);
    }


    private void sendScheduleRequestMessage(WfProcessInstanceMessage wfProcessInstanceMessage) {
        rocketMQTemplate.convertAndSend(ScheduleRequestMessage.TOPIC, new ScheduleRequestMessage().setWfProcessInstanceMessage(wfProcessInstanceMessage));
    }
}
