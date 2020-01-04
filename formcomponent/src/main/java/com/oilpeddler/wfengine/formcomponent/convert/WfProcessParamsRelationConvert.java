package com.oilpeddler.wfengine.formcomponent.convert;

import com.oilpeddler.wfengine.common.dto.WfProcessParamsRelationDTO;
import com.oilpeddler.wfengine.formcomponent.dataobject.WfProcessParamsRelationDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessParamsRelationConvert {
    WfProcessParamsRelationConvert INSTANCE = Mappers.getMapper(WfProcessParamsRelationConvert.class);

    @Mappings({})
    WfProcessParamsRelationDTO convertDOToDTO(WfProcessParamsRelationDO wfProcessParamsRelationDO);

    @Mappings({})
    WfProcessParamsRelationDO convertDTOToDO(WfProcessParamsRelationDTO wfProcessParamsRelationDTO);
}
