package com.oilpeddler.wfengine.taskmanagecomponent.convert;

import com.oilpeddler.wfengine.common.dto.WfTaskHistoryInstanceDTO;
import com.oilpeddler.wfengine.taskmanagecomponent.dataobject.WfTaskHistoryInstanceDO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

public interface WfTaskHistoryInstanceConvert {
    WfTaskHistoryInstanceConvert INSTANCE = Mappers.getMapper(WfTaskHistoryInstanceConvert.class);
    @Mappings({})
    WfTaskHistoryInstanceDTO convertDOToDTO(WfTaskHistoryInstanceDO wfTaskHistoryInstanceDO);

    @Mappings({})
    WfTaskHistoryInstanceDO convertDTOToDO(WfTaskHistoryInstanceDTO wfTaskHistoryInstanceDTO);

}
