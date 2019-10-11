package com.oilpeddler.wfengine.common.api.processmanagservice;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

public interface WfProcessTemplateService {
    /**
     * 测试用，不属于项目用
     * @param ptFilename
     * @return
     */
    WfProcessTemplateDTO selectByPtFilename(String ptFilename);
}
