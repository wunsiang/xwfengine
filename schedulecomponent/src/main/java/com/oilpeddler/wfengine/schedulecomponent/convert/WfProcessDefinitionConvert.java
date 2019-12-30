package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessDefinitionDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessDefinitionConvert {
    WfProcessDefinitionConvert INSTANCE = Mappers.getMapper(WfProcessDefinitionConvert.class);

    @Mappings({})
    WfProcessDefinitionBO convertDOToBO(WfProcessDefinitionDO wfProcessDefinitionDO);
}
