package com.oilpeddler.wfengine.schedulecomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.common.constant.ActivityInstanceCategory;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.constant.ProcessParamRecordLevel;
import com.oilpeddler.wfengine.schedulecomponent.constant.ProcessParamState;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfProcessParamsRecordConvert;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessParamsRecordMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessParamsRelationMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRecordDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRelationDO;
import com.oilpeddler.wfengine.schedulecomponent.service.WfActivtityInstanceService;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WfProcessParamsRecordServiceImpl implements WfProcessParamsRecordService {
    @Autowired
    WfProcessParamsRelationMapper wfProcessParamsRelationMapper;

    @Autowired
    WfProcessParamsRecordMapper wfProcessParamsRecordMapper;

/*    @Autowired
    WfProcessInstanceMapper wfProcessInstanceMapper;*/

/*    @Reference
    WfTaskInstanceService wfTaskInstanceService;*/

    @Autowired
    WfActivtityInstanceService wfActivtityInstanceService;
    /**
     * 更新record参数表中开启任务时插入行为留下的记录的值
     * @param tiId
     * @param requiredData
     */
    @Override
    public void recordRequiredData(String tiId,String aiId, String pdId,String taskNo,Map<String, Object> requiredData) {
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
                wfProcessParamsRecordDO.setAiId(aiId);
                wfProcessParamsRecordDO.setUpdatetime(wfProcessParamsRecordDO.getUpdatetime());
                wfProcessParamsRecordDO.setPpRecordLevel(ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_TASK);
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
            conditionMap.put("pp_record_level",ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_TASK);
            List<WfProcessParamsRecordDO> wfProcessParamsRecordDOList = wfProcessParamsRecordMapper.selectByMap(conditionMap);
            for(WfProcessParamsRecordDO wfProcessParamsRecordDO : wfProcessParamsRecordDOList){
                String deleteId = wfProcessParamsRecordDO.getId();
                //TODO 1025新增之前如果有驳回的循环操作生成过改活动级别参数的话就先置失效，后面有了历史库再挪过去
                QueryWrapper<WfProcessParamsRecordDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pp_relation_id", wfProcessParamsRecordDO.getPpRelationId());
                queryWrapper.eq("ai_id", wfProcessParamsRecordDO.getAiId());
                queryWrapper.eq("status",ProcessParamState.PROCESS_PARAM_EFFECT);
                queryWrapper.eq("pp_record_level",ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_ACTIVITY);
                List<WfProcessParamsRecordDO> wfProcessParamsRecordDOPreList = wfProcessParamsRecordMapper.selectList(queryWrapper);
                for(WfProcessParamsRecordDO wfProcessParamsRecordPreDO : wfProcessParamsRecordDOPreList){
                    wfProcessParamsRecordPreDO.setStatus(ProcessParamState.PROCESS_PARAM_FAILURE);
                    wfProcessParamsRecordMapper.updateById(wfProcessParamsRecordPreDO);
                }
                wfProcessParamsRecordDO.setId(null);
                wfProcessParamsRecordDO.setTiId(null);
                wfProcessParamsRecordDO.setAiId(wfActivtityInstanceDTO.getId());
                wfProcessParamsRecordDO.setCreatetime(new Date());
                wfProcessParamsRecordDO.setUpdatetime(wfProcessParamsRecordDO.getCreatetime());
                wfProcessParamsRecordDO.setStatus(ProcessParamState.PROCESS_PARAM_EFFECT);
                wfProcessParamsRecordDO.setPpRecordLevel(ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_ACTIVITY);
                wfProcessParamsRecordMapper.insert(wfProcessParamsRecordDO);
                //TODO 后续增加参数历史表后将任务数据记录在历史表中
                //1025新增，生成活动数据后就删除相关任务数据
                wfProcessParamsRecordMapper.deleteById(deleteId);
            }
        }else {//TODO 会签活动，所有任务数据相与，暂时版本，待优化
            //List<WfTaskInstanceDTO> wfTaskInstanceDTOList = wfTaskInstanceService.findRelatedTaskList(wfActivtityInstanceDTO.getId());
            //逻辑上应该不会出现这种活动没有相关任务的情况
/*            if(wfTaskInstanceDTOList.size() == 0)
                return;*/
            Map<String,Object> conditionMap = new HashMap<>();
            QueryWrapper<WfProcessParamsRecordDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ti_id", tiId);
            List<WfProcessParamsRecordDO> taskLevelRecordList = wfProcessParamsRecordMapper.selectList(queryWrapper);
            List<WfProcessParamsRecordDO> activityRecordList = new ArrayList<>();
            for(WfProcessParamsRecordDO wfProcessParamsRecordDO : taskLevelRecordList){
                wfProcessParamsRecordDO.setUpdatetime(new Date());
                wfProcessParamsRecordDO.setCreatetime(wfProcessParamsRecordDO.getUpdatetime());
                wfProcessParamsRecordDO.setTiId(null);
                wfProcessParamsRecordDO.setAiId(wfActivtityInstanceDTO.getId());
                wfProcessParamsRecordDO.setPpRecordLevel(ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_ACTIVITY);
                wfProcessParamsRecordDO.setPpRecordValue("0");
                activityRecordList.add(wfProcessParamsRecordDO);
            }

            //计算活动级参数并存储
            for(WfProcessParamsRecordDO activityRecordDO : activityRecordList){
                activityRecordDO.setId(null);
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pp_relation_id", activityRecordDO.getPpRelationId());
                queryWrapper.eq("ai_id", activityRecordDO.getAiId());
                queryWrapper.eq("status",ProcessParamState.PROCESS_PARAM_EFFECT);
                queryWrapper.eq("pp_record_level",ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_TASK);
                List<WfProcessParamsRecordDO> taskRecordDoList = wfProcessParamsRecordMapper.selectList(queryWrapper);
                for(WfProcessParamsRecordDO wfProcessParamsRecordDO : taskRecordDoList){
                    int taskValue = Integer.parseInt(wfProcessParamsRecordDO.getPpRecordValue());
                    int activityValue = Integer.parseInt(activityRecordDO.getPpRecordValue());
                    activityRecordDO.setPpRecordValue(String.valueOf(taskValue + activityValue));
                    wfProcessParamsRecordDO.setStatus(ProcessParamState.PROCESS_PARAM_FAILURE);
                    wfProcessParamsRecordMapper.updateById(wfProcessParamsRecordDO);
                    wfProcessParamsRecordMapper.deleteById(wfProcessParamsRecordDO.getId());
                    //TODO 后期增加参数历史表，将参数记录移到历史表
                }
                /**
                 * 驳回情况些可能会出现一个参数多条记录，虽然在取得时候也做了只取最新的一条的
                 * 限制，但还是也把之前的置为失效，安全起见。
                 * TODO 其实这应该删除之前的活动数据，并转移到历史库
                 */
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pp_relation_id", activityRecordDO.getPpRelationId());
                queryWrapper.eq("ai_id", activityRecordDO.getAiId());
                queryWrapper.eq("status",ProcessParamState.PROCESS_PARAM_EFFECT);
                queryWrapper.eq("pp_record_level",ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_ACTIVITY);
                List<WfProcessParamsRecordDO> wfProcessParamsRecordDOList = wfProcessParamsRecordMapper.selectList(queryWrapper);
                for(WfProcessParamsRecordDO wfProcessParamsRecordDO : wfProcessParamsRecordDOList){
                    wfProcessParamsRecordDO.setStatus(ProcessParamState.PROCESS_PARAM_FAILURE);
                    wfProcessParamsRecordMapper.updateById(wfProcessParamsRecordDO);
                }
                wfProcessParamsRecordMapper.insert(activityRecordDO);
            }


        }
    }

    /**
     * TODO 重要，由于目前没有模拟引擎唯一参数的生成，是直接手动写的，所以xml的参数格式比较详细，而且目前是能够做到只要
     * 引擎生成的参数名称是流程内唯一的就可以的，但是如果引擎生成全库唯一的参数的话，再加上在record表引入全都写上piid，
     * 可以优化这个方法
     * @param enginePpName
     * @param processInstanceId
     * @param pdId
     * @param usertaskNo
     * @return
     */
    @Override
    public WfProcessParamsRecordBO getByEnginePpName(String enginePpName, String processInstanceId,String pdId, String usertaskNo) {
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("pi_id",processInstanceId);
        conditionMap.put("usertask_no",usertaskNo);
        WfActivtityInstanceBO wfActivtityInstanceBO = wfActivtityInstanceService.getOneByMap(conditionMap);
        conditionMap = new HashMap<>();
        conditionMap.put("engine_pp_name",enginePpName);
        conditionMap.put("pd_id",pdId);
        conditionMap.put("task_no",usertaskNo);
        List<WfProcessParamsRelationDO> wfProcessParamsRelationDOList = wfProcessParamsRelationMapper.selectByMap(conditionMap);
        conditionMap = new HashMap<>();
        conditionMap.put("pp_relation_id",wfProcessParamsRelationDOList.get(0).getId());
        conditionMap.put("ai_id",wfActivtityInstanceBO.getId());
        //流程算法只会读取活动级别的数据
        conditionMap.put("pp_record_level",ProcessParamRecordLevel.PROCESS_PARAM_RECORD_LEVEL_ACTIVITY);
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
