package approvalsystem.service.impl;

import approvalsystem.convert.LeaveInfoConvert;
import approvalsystem.dao.LeaveInfoMapper;
import approvalsystem.dao.UserInfoMapper;
import approvalsystem.dataobject.LeaveInfoDO;
import approvalsystem.dataobject.UserInfoDO;
import approvalsystem.dto.LeaveInfoDTO;
import approvalsystem.service.LeaveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveInfoServiceImpl implements LeaveInfoService {

    @Autowired
    LeaveInfoMapper leaveInfoMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public LeaveInfoDTO addLeave(LeaveInfoDTO leaveInfoDTO) {
        LeaveInfoDO leaveInfoDO = LeaveInfoConvert.INSTANCE.convertDTOToDO(leaveInfoDTO);
        leaveInfoMapper.insert(leaveInfoDO);
        return LeaveInfoConvert.INSTANCE.convertDOToDTO(leaveInfoDO);
    }

    @Override
    public LeaveInfoDTO selectById(String id) {
        LeaveInfoDO leaveInfoDO = leaveInfoMapper.selectById(id);
        LeaveInfoDTO leaveInfoDTO = LeaveInfoConvert.INSTANCE.convertDOToDTO(leaveInfoDO);
        UserInfoDO userInfoDO = userInfoMapper.selectById(leaveInfoDO.getUiId());
        leaveInfoDTO.setUiName(userInfoDO.getUiName());
        return leaveInfoDTO;
    }
}
