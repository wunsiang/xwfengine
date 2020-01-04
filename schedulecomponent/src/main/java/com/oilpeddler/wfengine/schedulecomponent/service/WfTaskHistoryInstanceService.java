package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.schedulecomponent.dto.WfTaskHistoryInstanceDTO;

public interface WfTaskHistoryInstanceService {
    void save(WfTaskHistoryInstanceDTO wfTaskHistoryInstanceDTO);

    void delete(String id);
}
