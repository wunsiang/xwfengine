package com.oilpeddler.wfengine.schedulecomponent.service.impl;

import com.oilpeddler.wfengine.common.api.formservice.WfProcessParamsRelationService;
import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.schedulecomponent.convert.WfProcessDefinitionConvert;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessDefinitionMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.WfProcessTemplateMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.redis.WfProcessDefinitionCacheDao;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessDefinitionDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessTemplateDO;
import com.oilpeddler.wfengine.schedulecomponent.element.BpmnModel;
import com.oilpeddler.wfengine.schedulecomponent.element.DataParam;
import com.oilpeddler.wfengine.schedulecomponent.element.UserTask;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessDefinitionService;
import com.oilpeddler.wfengine.schedulecomponent.tools.BpmnXMLConvertUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
@Service
public class WfProcessDefinitionServiceImpl implements WfProcessDefinitionService {
    @Autowired
    WfProcessDefinitionMapper wfProcessDefinitionMapper;

    @Autowired
    WfProcessTemplateMapper wfProcessTemplateMapper;


    @Autowired
    WfProcessDefinitionCacheDao wfProcessDefinitionCacheDao;

    @Reference
    WfProcessParamsRelationService wfProcessParamsRelationService;


    @Override
    public WfProcessDefinitionBO getWfProcessDefinitionById(String id) {
        WfProcessDefinitionDO wfProcessDefinitionDO = wfProcessDefinitionMapper.selectById(id);
        WfProcessDefinitionBO wfProcessDefinitionBO = WfProcessDefinitionConvert.INSTANCE.convertDOToBO(wfProcessDefinitionDO);
        WfProcessTemplateDO wfProcessTemplateDO = wfProcessTemplateMapper.selectById(wfProcessDefinitionDO.getPtId());
        wfProcessDefinitionBO.setPtContent(wfProcessTemplateDO.getPtContent());
        wfProcessDefinitionBO.setBpmnModel(BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessDefinitionBO.getPtContent()));
        return wfProcessDefinitionBO;
    }

    @Override
    public WfProcessDefinitionBO generatePDFromTemplateFile(WfProcessTemplateDTO wfProcessTemplateDTO) {
        BpmnModel bpmnModel = BpmnXMLConvertUtil.ConvertToBpmnModel(wfProcessTemplateDTO.getPtContent());
        WfProcessDefinitionDO wfProcessDefinitionDO = new WfProcessDefinitionDO()
                .setPtId(wfProcessTemplateDTO.getId())
                .setPdName(bpmnModel.getName())
                .setPdNo(bpmnModel.getNo());
        wfProcessDefinitionDO.setCreatetime(new Date());
        wfProcessDefinitionDO.setUpdatetime(wfProcessDefinitionDO.getCreatetime());
        wfProcessDefinitionMapper.insert(wfProcessDefinitionDO);
        /**
         * 构造参数映射关系
         */
        List<UserTask> userTaskList =  bpmnModel.getProcess().getUserTaskList();
        for(UserTask userTask : userTaskList){
            if(userTask.getParamList() == null)
                continue;
            for(DataParam dataParam : userTask.getParamList()){
                WfProcessParamsRelationDTO wfProcessParamsRelationDTO = new WfProcessParamsRelationDTO()
                        .setPpName(dataParam.getPpName())
                        .setPpLevel("02")
                        .setPpType(dataParam.getPpType())
                        .setPdId(wfProcessDefinitionDO.getId())
                        .setTaskNo(dataParam.getTaskNo())
                        .setEnginePpName(dataParam.getEnginePpName());
                wfProcessParamsRelationDTO.setCreatetime(new Date());
                wfProcessParamsRelationDTO.setUpdatetime(wfProcessParamsRelationDTO.getCreatetime());
                wfProcessParamsRelationService.save(wfProcessParamsRelationDTO);
            }
        }
        return WfProcessDefinitionConvert.INSTANCE.convertDOToBO(wfProcessDefinitionDO).setBpmnModel(bpmnModel);
    }

    @Override
    public WfProcessDefinitionBO getFromCache(String id) {
        return wfProcessDefinitionCacheDao.get(id);
    }

    @Override
    public void setToCache(WfProcessDefinitionBO wfProcessDefinitionBO) {
        wfProcessDefinitionCacheDao.set(wfProcessDefinitionBO.getId(),wfProcessDefinitionBO);
    }
}
