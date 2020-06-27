package com.oilpeddler.wfengine.schedulecomponent.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceState;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfActivtityInstanceConvert;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfActivityHistoryInstanceMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfActivtityInstanceMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessParamsRecordMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfActivtityInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRecordDO;
import com.oilpeddler.wfengine.schedulecomponent.element.BaseElement;
import com.oilpeddler.wfengine.schedulecomponent.element.StartEvent;
import com.oilpeddler.wfengine.schedulecomponent.element.UserTask;
import com.oilpeddler.wfengine.schedulecomponent.service.WfActivtityInstanceService;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-09
 */
@Service
@org.springframework.stereotype.Service
public class WfActivtityInstanceServiceImpl implements WfActivtityInstanceService {

    @Autowired
    WfActivtityInstanceMapper wfActivtityInstanceMapper;

    @Autowired
    WfActivityHistoryInstanceMapper wfActivityHistoryInstanceMapper;

    @Autowired
    WfProcessParamsRecordMapper wfProcessParamsRecordMapper;

    @Autowired
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Override
    public WfActivtityInstanceBO getById(String id) {
        WfActivtityInstanceDO wfActivtityInstanceDO = wfActivtityInstanceMapper.selectById(id);
        return WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO);
    }

    @Override
    public WfActivtityInstanceBO getOneByMap(Map<String, Object> conditionMap) {
        List<WfActivtityInstanceDO> wfActivtityInstanceDOList = wfActivtityInstanceMapper.selectByMap(conditionMap);
        if(wfActivtityInstanceDOList.size() == 0)
            return null;
        return WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDOList.get(0));
    }

    @Override
    public void update(WfActivtityInstanceDTO wfActivtityInstanceDTO){
        wfActivtityInstanceMapper.updateById(WfActivtityInstanceConvert.INSTANCE.convertDTOToDO(wfActivtityInstanceDTO));
    }

    @Override
    public void clearActivityOfProcess(String piId) {
        QueryWrapper<WfActivtityInstanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("pi_id",piId);
        wfActivtityInstanceMapper.delete(queryWrapper);
    }

    //TODO 活动重新开始需要置之前的参数失效
    @Override
    public List<WfActivtityInstanceBO> addActivityList(List<BaseElement> userTaskList, String piId, String pdId) {
        List<WfActivtityInstanceBO> wfActivtityInstanceBOList = new ArrayList<>();
        for(BaseElement baseElement : userTaskList){
            UserTask userTask = (UserTask)baseElement;
            QueryWrapper<WfActivtityInstanceDO> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("pi_id",piId)
                    .eq("usertask_no",userTask.getNo());
            WfActivtityInstanceDO wfActivtityInstanceDO = wfActivtityInstanceMapper.selectOne(queryWrapper);
            List<String> assList = new ArrayList<>();//即席和固定执行人的合集
            if(userTask.getAssignees() != null && userTask.getAssignees().size() > 0)
                assList.addAll(userTask.getAssignees());
            //即席判断与处理
            if(userTask.getDynamicAssignees() != null && userTask.getDynamicAssignees().length() > 0){
                String[] dynamicAssigneesParam = userTask.getDynamicAssignees().split(",");
                WfProcessParamsRecordBO dynamicAssigneesPP = wfProcessParamsRecordService.getByEnginePpName(dynamicAssigneesParam[3],piId,pdId,dynamicAssigneesParam[0]);
                String[] dynamicAssignees = dynamicAssigneesPP.getPpRecordValue().split(",");
                for(String member : dynamicAssignees){
                    assList.add(member);
                }
            }
            //即席内容处理结束
            if(wfActivtityInstanceDO != null){
                wfActivtityInstanceDO.setAiAssignerId(JSON.toJSONString(assList));
                wfActivtityInstanceDO.setActiveTiNum(assList.size());
                wfActivtityInstanceDO.setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_RUNNING);
                wfActivtityInstanceDO.setUpdatetime(new Date());
                wfActivtityInstanceMapper.updateById(wfActivtityInstanceDO);
                //若之前活动产生了参数，则删除
                Map<String,Object> conditionMap = new HashMap<>();
                conditionMap.put("ai_id",wfActivtityInstanceDO.getId());
                wfProcessParamsRecordMapper.deleteByMap(conditionMap);
                wfActivtityInstanceBOList.add(WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO));
                //状态转为运行，打时间戳
                wfActivityHistoryInstanceMapper.insert(WfActivtityInstanceConvert.INSTANCE.convertRunDOToHistoryDO(wfActivtityInstanceDO));
            }else {
                wfActivtityInstanceDO = new WfActivtityInstanceDO()
                        .setAiName(userTask.getName())
                        .setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_RUNNING)
                        .setAiAssignerId(JSON.toJSONString(assList))
                        .setAiAssignerType(userTask.getAssigneeType())
                        .setBfId(userTask.getPageKey())
                        .setUsertaskNo(userTask.getNo())
                        .setAiCategory(userTask.getTaskType())
                        .setPiId(piId)
                        .setPdId(pdId)
                        .setActiveTiNum(assList.size());
                //这块做个补丁，但其实是流程图设计的不好先这么弄下
                if(wfActivtityInstanceDO.getAiAssignerType() == null){
                    wfActivtityInstanceDO.setAiAssignerType(userTask.getDynamicAssigneeType());
                }
                wfActivtityInstanceDO.setCreatetime(new Date());
                wfActivtityInstanceDO.setUpdatetime(wfActivtityInstanceDO.getCreatetime());
                wfActivtityInstanceMapper.insert(wfActivtityInstanceDO);
                wfActivtityInstanceBOList.add(WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO));
                //1025新增，之前活动开启时忘了打时间戳
                wfActivityHistoryInstanceMapper.insert(WfActivtityInstanceConvert.INSTANCE.convertRunDOToHistoryDO(wfActivtityInstanceDO));
            }
        }
        return wfActivtityInstanceBOList;
    }

    @Override
    public WfActivtityInstanceBO addStartEventActivity(StartEvent startEvent, String piId, String pdId, String piStarter) {
        QueryWrapper<WfActivtityInstanceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("pi_id",piId)
                .eq("usertask_no",startEvent.getNo());
        WfActivtityInstanceDO wfActivtityInstanceDO = wfActivtityInstanceMapper.selectOne(queryWrapper);
        if(wfActivtityInstanceDO != null){
            wfActivtityInstanceDO.setAiAssignerId(JSON.toJSONString(piStarter));
            wfActivtityInstanceDO.setActiveTiNum(1);
            wfActivtityInstanceDO.setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_RUNNING);
            wfActivtityInstanceDO.setUpdatetime(new Date());
            wfActivtityInstanceMapper.updateById(wfActivtityInstanceDO);
            //若之前活动产生了参数，则删除
            Map<String,Object> conditionMap = new HashMap<>();
            conditionMap.put("ai_id",wfActivtityInstanceDO.getId());
            wfProcessParamsRecordMapper.deleteByMap(conditionMap);
            //wfActivtityInstanceBOList.add(WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO));
            //状态转为运行，打时间戳
            wfActivityHistoryInstanceMapper.insert(WfActivtityInstanceConvert.INSTANCE.convertRunDOToHistoryDO(wfActivtityInstanceDO));
        }else{
            wfActivtityInstanceDO = new WfActivtityInstanceDO()
                    .setAiName(startEvent.getName())
                    .setAiStatus(ActivityInstanceState.TASK_ACTIVITY_STATE_RUNNING)
                    .setAiAssignerId(JSON.toJSONString(piStarter))
                    .setAiAssignerType("0")
                    .setUsertaskNo(startEvent.getNo())
                    .setAiCategory("01")
                    .setPiId(piId)
                    .setPdId(pdId)
                    .setActiveTiNum(1);
            wfActivtityInstanceDO.setCreatetime(new Date());
            wfActivtityInstanceDO.setUpdatetime(wfActivtityInstanceDO.getCreatetime());
            wfActivtityInstanceMapper.insert(wfActivtityInstanceDO);
            //wfActivtityInstanceBOList.add(WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO));
            //1025新增，之前活动开启时忘了打时间戳
            wfActivityHistoryInstanceMapper.insert(WfActivtityInstanceConvert.INSTANCE.convertRunDOToHistoryDO(wfActivtityInstanceDO));
        }
        return WfActivtityInstanceConvert.INSTANCE.convertDOToBO(wfActivtityInstanceDO);
    }
}
