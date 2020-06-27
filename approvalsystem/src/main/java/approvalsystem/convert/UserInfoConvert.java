package approvalsystem.convert;

import approvalsystem.dataobject.UserInfoDO;
import approvalsystem.dto.UserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInfoConvert {
    UserInfoConvert INSTANCE = Mappers.getMapper(UserInfoConvert.class);

    @Mappings({})
    UserInfoDTO convertDOToDTO(UserInfoDO userInfoDO);

    @Mappings({})
    UserInfoDO convertDTOToDO(UserInfoDTO userInfoDTO);
}
