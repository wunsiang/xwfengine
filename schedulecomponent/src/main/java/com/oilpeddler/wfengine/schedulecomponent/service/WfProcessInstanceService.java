package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfProcessInstanceStartDTO;

public interface WfProcessInstanceService {
    /**
     * 开启一个新流程
     * @param wfProcessInstanceStartDTO
     * @return
     */
    WfProcessInstanceBO startProcess(WfProcessInstanceStartDTO wfProcessInstanceStartDTO);

    /**
     * 结束流程并收尾
     */
    void endProcess(String piId);

    void haha();

    WfProcessInstanceBO getById(String id);


}