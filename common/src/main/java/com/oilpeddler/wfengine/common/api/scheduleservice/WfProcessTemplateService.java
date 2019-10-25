package com.oilpeddler.wfengine.common.api.scheduleservice;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

public interface WfProcessTemplateService {
    WfProcessTemplateDTO selectByPtFilename(String ptFilename);
}
