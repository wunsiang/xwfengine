package com.oilpeddler.wfengine.processmanagecomponent.convert;

import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessDefinitionDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessDefinitionConvert {
    WfProcessDefinitionConvert INSTANCE = Mappers.getMapper(WfProcessDefinitionConvert.class);

    @Mappings({})
    WfProcessDefinitionBO convertDOToBO(WfProcessDefinitionDO wfProcessDefinitionDO);
}
