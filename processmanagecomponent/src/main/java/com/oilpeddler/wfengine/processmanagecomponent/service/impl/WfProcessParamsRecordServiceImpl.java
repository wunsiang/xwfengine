package com.oilpeddler.wfengine.processmanagecomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfActivtityInstanceService;
import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskInstanceService;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceCategory;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.processmanagecomponent.constant.ProcessParamState;
import com.oilpeddler.wfengine.processmanagecomponent.convert.WfProcessParamsRecordConvert;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessInstanceMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessParamsRecordMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessParamsRelationMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessInstanceDO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessParamsRecordDO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessParamsRelationDO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class WfProcessParamsRecordServiceImpl implements WfProcessParamsRecordService {
    @Autowired
    WfProcessParamsRelationMapper wfProcessParamsRelationMapper;

    @Autowired
    WfProcessParamsRecordMapper wfProcessParamsRecordMapper;

    @Autowired
    WfProcessInstanceMapper wfProcessInstanceMapper;

    @Reference
    WfTaskInstanceService wfTaskInstanceService;

    @Reference
    WfActivtityInstanceService wfActivtityInstanceService;
    /**
     * 更新record参数表中开启任务时插入行为留下的记录的值
     * @param tiId
     * @param requiredData
     */
    @Override
    public void recordRequiredData(String tiId, String pdId,String taskNo,Map<String, Object> requiredData) {
        if(requiredData == null)
            return;
        System.out.println("准备记录数据！！！");
        for (Map.Entry<String, Object> entry : requiredData.entrySet()) {
            WfProcessParamsRecordDO wfProcessParamsRecordDO = new WfProcessParamsRecordDO();
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("business_name",entry.getKey());
            conditionMap.put("task_no",taskNo);
            conditionMap.put("pd_id",pdId);
            List<WfProcessParamsRelationDO> wfProcessParamsRelationDOList = wfProcessParamsRelationMapper.selectByMap(conditionMap);
            //TODO 安全检查，验重
            if(wfProcessParamsRelationDOList.size() == 1){
                wfProcessParamsRecordDO.setPpRelationId(wfProcessParamsRelationDOList.get(0).getId());
                String val;
                if(entry.getValue() instanceof  String)
                    val = (String) entry.getValue();
                else if (entry.getValue() instanceof Boolean)
                    val = ((Boolean)(entry.getValue())) == true ? "1" : "0";
                else
                    val = String.valueOf(entry.getValue());
                wfProcessParamsRecordDO.setPpRecordValue(val);
                wfProcessParamsRecordDO.setTiId(tiId);
                wfProcessParamsRecordDO.setStatus(ProcessParamState.PROCESS_PARAM_EFFECT);
                wfProcessParamsRecordDO.setCreatetime(new Date());
                wfProcessParamsRecordDO.setUpdatetime(wfProcessParamsRecordDO.getUpdatetime());
                wfProcessParamsRecordMapper.insert(wfProcessParamsRecordDO);
            }
        }
    }

    @Override
    public void calculateActivityData(WfActivtityInstanceDTO wfActivtityInstanceDTO, String tiId){
        //普通活动，直接把任务参数复制一下弄成活动参数就行啦
        if(wfActivtityInstanceDTO.getAiCategory().equals(ActivityInstanceCategory.ACTIVITY_CATEGORY_SINGLE)){
            Map<String,Object> conditionMap = new HashMap<>();
            conditionMap.put("ti_id",tiId);
            conditionMap.put("status",ProcessParamState.PROCESS_PARAM_EFFECT);
            List<WfProcessParamsRecordDO> wfProcessParamsRecordDOList = wfProcessParamsRecordMapper.selectByMap(conditionMap);
            for(WfProcessParamsRecordDO wfProcessParamsRecordDO : wfProcessParamsRecordDOList){
                wfProcessParamsRecordDO.setId(null);
                wfProcessParamsRecordDO.setTiId(null);
                wfProcessParamsRecordDO.setAiId(wfActivtityInstanceDTO.getId());
                wfProcessParamsRecordDO.setCreatetime(new Date());
                wfProcessParamsRecordDO.setUpdatetime(wfProcessParamsRecordDO.getCreatetime());
                wfProcessParamsRecordDO.setStatus(ProcessParamState.PROCESS_PARAM_EFFECT);
                wfProcessParamsRecordMapper.insert(wfProcessParamsRecordDO);
            }
        }else {//TODO 会签活动，所有任务数据相与，暂时版本，待优化
            List<WfTaskInstanceDTO> wfTaskInstanceDTOList = wfTaskInstanceService.findRelatedTaskList(wfActivtityInstanceDTO.getId());
            //逻辑上应该不会出现这种活动没有相关任务的情况
            if(wfTaskInstanceDTOList.size() == 0)
                return;
            Map<String,Object> conditionMap = new HashMap<>();
            QueryWrapper<WfProcessParamsRecordDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ti_id", wfTaskInstanceDTOList.get(0).getId());
            List<WfProcessParamsRecordDO> taskLevelRecordList = wfProcessParamsRecordMapper.selectList(queryWrapper);
            List<WfProcessParamsRecordDO> activityRecordList = new ArrayList<>();
            for(WfProcessParamsRecordDO wfProcessParamsRecordDO : taskLevelRecordList){
                wfProcessParamsRecordDO.setUpdatetime(new Date());
                wfProcessParamsRecordDO.setCreatetime(wfProcessParamsRecordDO.getUpdatetime());
                wfProcessParamsRecordDO.setTiId(null);
                wfProcessParamsRecordDO.setAiId(wfActivtityInstanceDTO.getId());
                activityRecordList.add(wfProcessParamsRecordDO);
            }
            for(WfTaskInstanceDTO wfTaskInstanceDTO : wfTaskInstanceDTOList){
                QueryWrapper<WfProcessParamsRecordDO> queryWrapperTemp = new QueryWrapper<>();
                queryWrapperTemp.eq("ti_id", wfTaskInstanceDTO.getId());
                List<WfProcessParamsRecordDO> taskLevelRecordListTemp = wfProcessParamsRecordMapper.selectList(queryWrapperTemp);
                for(WfProcessParamsRecordDO wfProcessParamsRecordDO : taskLevelRecordListTemp){
                    for(WfProcessParamsRecordDO activityRecordDO : activityRecordList){
                        if(wfProcessParamsRecordDO.getPpRelationId().equals(activityRecordDO.getPpRelationId())){
                            int taskValue = Integer.parseInt(wfProcessParamsRecordDO.getPpRecordValue());
                            int activityValue = Integer.parseInt(activityRecordDO.getPpRecordValue());
                            activityRecordDO.setPpRecordValue(String.valueOf(taskValue&activityValue));
                            break;
                        }
                    }
                }
            }

            for(WfProcessParamsRecordDO activityRecordDO : activityRecordList){
                activityRecordDO.setId(null);
                wfProcessParamsRecordMapper.insert(activityRecordDO);
            }
        }
    }

    @Override
    public WfProcessParamsRecordBO getByEnginePpName(String enginePpName, String processInstanceId, String usertaskNo) {
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("pi_id",processInstanceId);
        conditionMap.put("usertask_no",usertaskNo);
        WfActivtityInstanceBO wfActivtityInstanceBO = wfActivtityInstanceService.getOneByMap(conditionMap);
        WfProcessInstanceDO wfProcessInstanceDO = wfProcessInstanceMapper.selectById(processInstanceId);
        conditionMap = new HashMap<>();
        conditionMap.put("engine_pp_name",enginePpName);
        conditionMap.put("pd_id",wfProcessInstanceDO.getPdId());
        conditionMap.put("task_no",usertaskNo);
        List<WfProcessParamsRelationDO> wfProcessParamsRelationDOList = wfProcessParamsRelationMapper.selectByMap(conditionMap);
        conditionMap = new HashMap<>();
        conditionMap.put("pp_relation_id",wfProcessParamsRelationDOList.get(0).getId());
        conditionMap.put("ai_id",wfActivtityInstanceBO.getId());
        conditionMap.put("status",ProcessParamState.PROCESS_PARAM_EFFECT);
        QueryWrapper<WfProcessParamsRecordDO> queryWrapper = new QueryWrapper<>();
        //为了兼容驳回的情况，取最新的orderByDesc
        queryWrapper.allEq(conditionMap).orderByDesc("createtime");
        List<WfProcessParamsRecordDO> wfProcessParamsRecordDOList = wfProcessParamsRecordMapper.selectList(queryWrapper);
        WfProcessParamsRecordBO wfProcessParamsRecordBO =  WfProcessParamsRecordConvert.INSTANCE.convertDOToBO(wfProcessParamsRecordDOList.get(0));
        wfProcessParamsRecordBO.setPpType(wfProcessParamsRelationDOList.get(0).getPpType());
        return wfProcessParamsRecordBO;
    }
}
