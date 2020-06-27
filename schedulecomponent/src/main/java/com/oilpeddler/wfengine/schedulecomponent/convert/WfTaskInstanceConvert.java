package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.common.bo.WfTaskHistoryInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import com.oilpeddler.wfengine.common.message.WfTaskInstanceMessage;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskInstanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfTaskInstanceConvert {
    WfTaskInstanceConvert INSTANCE = Mappers.getMapper(WfTaskInstanceConvert.class);

    @Mappings({})
    WfTaskInstanceMessage convertDOToMessage(WfTaskInstanceDO wfTaskInstanceDO);

    @Mappings({})
    WfTaskInstanceDTO convertDOToDTO(WfTaskInstanceDO wfTaskInstanceDO);

    @Mappings({})
    WfTaskInstanceBO convertDOToBO(WfTaskInstanceDO wfTaskInstanceDO);

    @Mappings({})
    WfTaskInstanceDO convertDTOToDO(WfTaskInstanceDTO wfTaskInstanceDTO);

    @Mappings({
            @Mapping(source = "id", target = "tiId"),
            @Mapping(source = "endtime", target = "tiEndtime"),
            @Mapping(source = "createtime", target = "tiCreatetime"),
            @Mapping(source = "updatetime", target = "tiUpdatetime"),
            @Mapping(target = "id",  ignore = true)
    })
    WfTaskHistoryInstanceDO convertRunToHistory(WfTaskInstanceDO wfTaskInstanceDO);

    @Mappings({
            @Mapping(source = "id", target = "tiId"),
            @Mapping(source = "endtime", target = "tiEndtime"),
            @Mapping(source = "createtime", target = "tiCreatetime"),
            @Mapping(source = "updatetime", target = "tiUpdatetime"),
            @Mapping(target = "id",  ignore = true)
    })
    WfTaskHistoryInstanceBO convertBOToHistory(WfTaskInstanceBO wfTaskInstanceBO);
}
