package com.oilpeddler.wfengine.taskmanagecomponent.service.impl;

import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskHistoryInstanceService;
import com.oilpeddler.wfengine.common.bo.WfTaskHistoryInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfTaskHistoryInstanceDTO;
import com.oilpeddler.wfengine.taskmanagecomponent.convert.WfTaskHistoryInstanceConvert;
import com.oilpeddler.wfengine.taskmanagecomponent.dao.WfTaskHistoryInstanceMapper;
import com.oilpeddler.wfengine.taskmanagecomponent.dataobject.WfTaskHistoryInstanceDO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
@Service
public class WfTaskHistoryInstanceServiceImpl implements WfTaskHistoryInstanceService {

    @Autowired
    WfTaskHistoryInstanceMapper wfTaskHistoryInstanceMapper;

    @Override
    public void save(WfTaskHistoryInstanceDTO wfTaskHistoryInstanceDTO) {
        WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO = WfTaskHistoryInstanceConvert.INSTANCE.convertDTOToDO(wfTaskHistoryInstanceDTO);
        wfTaskHistoryInstanceMapper.insert(wfTaskHistoryInstanceDO);
    }
}
