package com.oilpeddler.wfengine.schedulecomponent.convert;

import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import com.oilpeddler.wfengine.common.dto.WfActivtityInstanceDTO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfActivityHistoryInstanceDO;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfActivtityInstanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WfActivtityInstanceConvert {
    WfActivtityInstanceConvert INSTANCE = Mappers.getMapper(WfActivtityInstanceConvert.class);

    @Mappings({})
    WfActivtityInstanceDTO convertDOToDTO(WfActivtityInstanceDO wfActivtityInstanceDO);

    @Mappings({})
    WfActivtityInstanceBO convertDOToBO(WfActivtityInstanceDO wfActivtityInstanceDO);

    @Mappings({})
    WfActivtityInstanceDTO convertBOToDTO(WfActivtityInstanceBO wfActivtityInstanceBO);

    @Mappings({})
    WfActivtityInstanceDO convertDTOToDO(WfActivtityInstanceDTO wfActivtityInstanceDTO);

    @Mappings({
            @Mapping(source = "id", target = "actId"),
            @Mapping(source = "createtime", target = "aiCreatetime"),
            @Mapping(source = "updatetime", target = "aiUpdatetime")
    })
    WfActivityHistoryInstanceDO convertRunToHistoryDO(WfActivtityInstanceDTO wfActivtityInstanceDTO);

    @Mappings({
            @Mapping(source = "id", target = "actId"),
            @Mapping(source = "createtime", target = "aiCreatetime"),
            @Mapping(source = "updatetime", target = "aiUpdatetime")
    })
    WfActivityHistoryInstanceDO convertRunDOToHistoryDO(WfActivtityInstanceDO wfActivtityInstanceDO);
}
