package com.oilpeddler.wfengine.formcomponent.service;

import com.oilpeddler.wfengine.formcomponent.dto.WfBusinessFormDTO;

public interface WfBusinessFormService {
    WfBusinessFormDTO selectById(String id);
}
