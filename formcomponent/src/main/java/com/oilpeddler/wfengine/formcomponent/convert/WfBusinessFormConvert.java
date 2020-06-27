package com.oilpeddler.wfengine.formcomponent.convert;

import com.oilpeddler.wfengine.formcomponent.dataobject.WfBusinessFormDO;
import com.oilpeddler.wfengine.formcomponent.dto.WfBusinessFormDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfBusinessFormConvert {
    WfBusinessFormConvert INSTANCE = Mappers.getMapper(WfBusinessFormConvert.class);

    @Mappings({})
    WfBusinessFormDTO convertDOToDTO(WfBusinessFormDO wfBusinessFormDO);

    @Mappings({})
    WfBusinessFormDO convertDTOToDO(WfBusinessFormDTO wfBusinessFormDTO);
}
