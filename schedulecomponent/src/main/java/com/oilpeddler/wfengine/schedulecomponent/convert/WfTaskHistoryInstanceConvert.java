package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.schedulecomponent.bo.WfTaskHistoryInstanceBO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dto.WfTaskHistoryInstanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfTaskHistoryInstanceConvert {
    WfTaskHistoryInstanceConvert INSTANCE = Mappers.getMapper(WfTaskHistoryInstanceConvert.class);
    @Mappings({})
    WfTaskHistoryInstanceDTO convertDOToDTO(WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO);

    @Mappings({})
    WfTaskHistoryInstanceDO convertDTOToDO(WfTaskHistoryInstanceDTO wfTaskHistoryInstanceDTO);

    @Mappings({})
    WfTaskHistoryInstanceDTO convertBOToDTO(WfTaskHistoryInstanceBO wfTaskHistoryInstanceBO);

}
