package com.oilpeddler.wfengine.formcomponent.service.impl;

import com.oilpeddler.wfengine.formcomponent.convert.WfBusinessFormConvert;
import com.oilpeddler.wfengine.formcomponent.dao.WfBusinessFormMapper;
import com.oilpeddler.wfengine.formcomponent.dataobject.WfBusinessFormDO;
import com.oilpeddler.wfengine.formcomponent.dto.WfBusinessFormDTO;
import com.oilpeddler.wfengine.formcomponent.service.WfBusinessFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WfBusinessFormServiceImpl implements WfBusinessFormService {

    @Autowired
    WfBusinessFormMapper wfBusinessFormMapper;

    @Override
    public WfBusinessFormDTO selectById(String id) {
        WfBusinessFormDO wfBusinessFormDO = wfBusinessFormMapper.selectById(id);
        return WfBusinessFormConvert.INSTANCE.convertDOToDTO(wfBusinessFormDO);
    }
}
