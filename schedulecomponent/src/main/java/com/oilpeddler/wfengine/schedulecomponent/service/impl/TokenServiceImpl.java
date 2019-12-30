package com.oilpeddler.wfengine.schedulecomponent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.redis.TokenCacheDao;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.element.Process;
import com.oilpeddler.wfengine.schedulecomponent.element.UserTask;
import com.oilpeddler.wfengine.schedulecomponent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenMapper tokenMapper;

    @Autowired
    TokenCacheDao tokenCacheDao;

    @Override
    public Token recoverTokens(String piId, String pdId, String elementNo, Process process) {
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("pi_id",piId)
                .eq("pd_id",pdId)
                .eq("element_no",elementNo);
        //目前认为，一个活动（只限于UserTask，gateway啥的不算）上只可能有一个token
        Token currentToken = tokenMapper.selectOne(queryWrapper);
        Token parent = currentToken;
        while (!parent.getParentId().equals("0")){
            queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("parent_id",parent.getParentId())
                    .ne("id",parent.getId());
            List<Token> concurrentTokens = tokenMapper.selectList(queryWrapper);
            concurrentTokens.add(parent);
            parent = tokenMapper.selectById(parent.getParentId());
            parent.setChildren(concurrentTokens);
            for(Token cToken : concurrentTokens){
                cToken.setParent(parent);
                cToken.setCurrentNode(findUserTaskByNo(cToken.getElementNo(),process));
            }
        }
        parent.setCurrentNode(findUserTaskByNo(parent.getElementNo(),process));
        return currentToken;
    }

    @Override
    public Token getFromCache(String id) {
        return tokenCacheDao.get(id);
    }

    @Override
    public void setToCache(Token token) {
        tokenCacheDao.set(token.getId(),token);
    }

    public UserTask findUserTaskByNo(String no, Process process) {
        for(UserTask userTask : process.getUserTaskList()){
            if(userTask.getNo().equals(no))
                return userTask;
        }
        return null;
    }
}
