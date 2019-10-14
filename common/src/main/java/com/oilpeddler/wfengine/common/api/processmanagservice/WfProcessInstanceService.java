package com.oilpeddler.wfengine.common.api.processmanagservice;

import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfProcessInstanceStartDTO;

import java.util.List;

public interface WfProcessInstanceService {
    /**
     * 开启一个新流程
     * @param wfProcessInstanceStartDTO
     * @return
     */
    boolean startProcess(WfProcessInstanceStartDTO wfProcessInstanceStartDTO);

    /**
     * 结束流程并收尾
     */
    void endProcess(String piId);

    void haha();

    WfProcessInstanceBO getById(String id);


}