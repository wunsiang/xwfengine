package com.oilpeddler.wfengine.common.api.formservice;

import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;

public interface WfProcessParamsRelationService {
    WfProcessParamsRelationDTO save(WfProcessParamsRelationDTO wfProcessParamsRelationDTO);

    WfProcessParamsRelationDTO getEnginePpName(String pdId,String businessName,String taskNo);
}
