package com.oilpeddler.wfengine.formcomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.oilpeddler.wfengine.common.api.formservice.WfProcessParamsRelationService;
import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;
import com.oilpeddler.wfengine.formcomponent.convert.WfProcessParamsRelationConvert;
import com.oilpeddler.wfengine.formcomponent.dao.WfProcessParamsRelationMapper;
import com.oilpeddler.wfengine.formcomponent.dataobject.WfProcessParamsRelationDO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@org.springframework.stereotype.Service
public class WfProcessParamsRelationServiceImpl implements WfProcessParamsRelationService {

    @Autowired
    WfProcessParamsRelationMapper wfProcessParamsRelationMapper;

    @Override
    public WfProcessParamsRelationDTO save(WfProcessParamsRelationDTO wfProcessParamsRelationDTO) {
        WfProcessParamsRelationDO wfProcessParamsRelationDO = WfProcessParamsRelationConvert.INSTANCE.convertDTOToDO(wfProcessParamsRelationDTO);
        wfProcessParamsRelationMapper.insert(wfProcessParamsRelationDO);
        return WfProcessParamsRelationConvert.INSTANCE.convertDOToDTO(wfProcessParamsRelationDO);
    }

    @Override
    public WfProcessParamsRelationDTO getEnginePpName(String pdId, String businessName, String taskNo) {
        QueryWrapper<WfProcessParamsRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("business_name",businessName)
                .eq("pd_id",pdId)
                .eq("task_no",taskNo);
        WfProcessParamsRelationDO wfProcessParamsRelationDO = wfProcessParamsRelationMapper.selectOne(queryWrapper);
        return WfProcessParamsRelationConvert.INSTANCE.convertDOToDTO(wfProcessParamsRelationDO);
    }
}
