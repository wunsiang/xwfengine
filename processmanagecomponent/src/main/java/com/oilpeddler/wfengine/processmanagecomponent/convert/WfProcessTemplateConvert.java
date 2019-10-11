package com.oilpeddler.wfengine.processmanagecomponent.convert;

import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessTemplateConvert {
    WfProcessTemplateConvert INSTANCE = Mappers.getMapper(WfProcessTemplateConvert.class);

    @Mappings({})
    WfProcessTemplateDTO convertDOToDTO(WfProcessTemplateDO wfProcessTemplateDO);
}
