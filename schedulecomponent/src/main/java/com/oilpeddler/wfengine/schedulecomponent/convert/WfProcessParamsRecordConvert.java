package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessParamsRecordConvert {
    WfProcessParamsRecordConvert INSTANCE = Mappers.getMapper(WfProcessParamsRecordConvert.class);

    @Mappings({})
    WfProcessParamsRecordBO convertDOToBO(WfProcessParamsRecordDO wfProcessParamsRecordDO);
}
