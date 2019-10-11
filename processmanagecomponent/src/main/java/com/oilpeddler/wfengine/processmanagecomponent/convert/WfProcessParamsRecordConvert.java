package com.oilpeddler.wfengine.processmanagecomponent.convert;

import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.processmanagecomponent.dataobject.WfProcessParamsRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessParamsRecordConvert {
    WfProcessParamsRecordConvert INSTANCE = Mappers.getMapper(WfProcessParamsRecordConvert.class);

    @Mappings({})
    WfProcessParamsRecordBO convertDOToBO(WfProcessParamsRecordDO wfProcessParamsRecordDO);
}
