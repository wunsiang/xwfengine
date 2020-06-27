package approvalsystem.service.impl;

import approvalsystem.convert.UserInfoConvert;
import approvalsystem.dao.UserInfoMapper;
import approvalsystem.dataobject.UserInfoDO;
import approvalsystem.dto.UserInfoDTO;
import approvalsystem.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfoDTO validateUser(String uiName) {
        QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ui_name",uiName);
        UserInfoDO userInfoDO = userInfoMapper.selectOne(queryWrapper);
        return UserInfoConvert.INSTANCE.convertDOToDTO(userInfoDO);
    }

    @Override
    public UserInfoDTO queryUser(String id) {
        UserInfoDO userInfoDO = userInfoMapper.selectById(id);
        return UserInfoConvert.INSTANCE.convertDOToDTO(userInfoDO);
    }
}
