package approvalsystem.convert;

import approvalsystem.dataobject.LeaveInfoDO;
import approvalsystem.dataobject.UserInfoDO;
import approvalsystem.dto.LeaveInfoDTO;
import approvalsystem.dto.UserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveInfoConvert {
    LeaveInfoConvert INSTANCE = Mappers.getMapper(LeaveInfoConvert.class);

    @Mappings({})
    LeaveInfoDTO convertDOToDTO(LeaveInfoDO leaveInfoDO);

    @Mappings({})
    LeaveInfoDO convertDTOToDO(LeaveInfoDTO leaveInfoDTO);
}
