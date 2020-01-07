package com.oilpeddler.wfengine.schedulecomponent;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBTests {

    @Autowired
    TokenMapper tokenMapper;

    @Test
    @Transactional
    public void bf(){
        int r = tokenMapper.deleteById("1213400206196912130");
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("parent_id","1213398753059319810");
        int count = tokenMapper.selectCount(queryWrapper);
        System.out.println(count);
    }
}