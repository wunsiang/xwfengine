package com.oilpeddler.wfengine.schedulecomponent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenMapper extends BaseMapper<Token> {
    void decrementChildNum(String id);
}
