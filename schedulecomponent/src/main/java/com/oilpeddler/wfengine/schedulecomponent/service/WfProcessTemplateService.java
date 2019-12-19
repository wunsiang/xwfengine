package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;

public interface WfProcessTemplateService {
    WfProcessTemplateDTO selectByPtFilename(String ptFilename);
}
