package approvalsystem.service;

import approvalsystem.dto.UserInfoDTO;

public interface UserInfoService {
    UserInfoDTO validateUser(String uiName);

    UserInfoDTO queryUser(String id);
}
