package approvalsystem.service;

import approvalsystem.dto.LeaveInfoDTO;

public interface LeaveInfoService {
    LeaveInfoDTO addLeave(LeaveInfoDTO leaveInfoDTO);

    LeaveInfoDTO selectById(String id);
}
