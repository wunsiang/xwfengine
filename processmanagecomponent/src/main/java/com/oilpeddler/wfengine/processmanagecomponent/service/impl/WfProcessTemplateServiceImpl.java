package com.oilpeddler.wfengine.processmanagecomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.api.processmanagservice.WfProcessTemplateService;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.processmanagecomponent.convert.WfProcessTemplateConvert;
import com.oilpeddler.wfengine.processmanagecomponent.dao.WfProcessTemplateMapper;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessTemplateDO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
@Service
public class WfProcessTemplateServiceImpl implements WfProcessTemplateService {

    @Autowired
    WfProcessTemplateMapper wfProcessTemplateMapper;

    @Override
    public WfProcessTemplateDTO selectByPtFilename(String ptFilename) {
        QueryWrapper<WfProcessTemplateDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pt_filename",ptFilename);
        WfProcessTemplateDO wfProcessTemplateDO = wfProcessTemplateMapper.selectOne(queryWrapper);
        return WfProcessTemplateConvert.INSTANCE.convertDOToDTO(wfProcessTemplateDO);
    }
}
