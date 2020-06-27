package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfProcessInstanceStartDTO;
import com.oilpeddler.wfengine.common.message.WfProcessInstanceMessage;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessInstanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfProcessInstanceConvert {

    WfProcessInstanceConvert INSTANCE = Mappers.getMapper(WfProcessInstanceConvert.class);

    @Mappings({})
    WfProcessInstanceDO convertStartDTOToDO(WfProcessInstanceStartDTO wfProcessInstanceStartDTO);

    @Mappings({})
    WfProcessInstanceStartDTO convertMessageToStartDTO(WfProcessInstanceMessage wfProcessInstanceMessage);

    @Mappings({})
    WfProcessInstanceMessage convertDOToMessage(WfProcessInstanceDO wfProcessInstanceDO);

    @Mappings({
            @Mapping(source = "id", target = "piId"),
            @Mapping(source = "endtime", target = "piEndtime"),
            @Mapping(source = "createtime", target = "piCreatetime"),
            @Mapping(source = "updatetime", target = "piUpdatetime"),
            @Mapping(target = "id",  ignore = true)
    })
    WfProcessHistoryInstanceDO convertRunToHistory(WfProcessInstanceDO wfProcessInstanceDO);

    @Mappings({})
    WfProcessInstanceBO convertDOToBO(WfProcessInstanceDO wfProcessInstanceDO);


}
