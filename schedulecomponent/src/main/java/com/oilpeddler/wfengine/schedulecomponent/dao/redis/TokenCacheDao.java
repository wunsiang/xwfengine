package com.oilpeddler.wfengine.schedulecomponent.dao.redis;

import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.tools.JSONUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class TokenCacheDao {
    private static final String KEY_PATTERN = "token:%s"; // wProcessDefinition:流程定义主键

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, String> operations;
    private static String buildKey(String id) {
        return String.format(KEY_PATTERN, id);
    }

    public Token get(String id) {
        String key = buildKey(id);
        String value = operations.get(key);
        return JSONUtil.parseObject(value, Token.class);
    }

    public void set(String id, Token object) {
        String key = buildKey(id);
        String value = JSONUtil.toJSONString(object);
        operations.set(key, value);
    }
}
