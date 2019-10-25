package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessTemplateConvert {
    WfProcessTemplateConvert INSTANCE = Mappers.getMapper(WfProcessTemplateConvert.class);

    @Mappings({})
    WfProcessTemplateDTO convertDOToDTO(WfProcessTemplateDO wfProcessTemplateDO);
}
