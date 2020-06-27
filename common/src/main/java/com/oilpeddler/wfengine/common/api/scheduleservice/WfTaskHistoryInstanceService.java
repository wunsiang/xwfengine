package com.oilpeddler.wfengine.common.api.scheduleservice;

import com.oilpeddler.wfengine.common.dto.WfTaskHistoryInstanceDTO;

public interface WfTaskHistoryInstanceService {
    void save(WfTaskHistoryInstanceDTO wfTaskHistoryInstanceDTO);

    void delete(String id);
}
