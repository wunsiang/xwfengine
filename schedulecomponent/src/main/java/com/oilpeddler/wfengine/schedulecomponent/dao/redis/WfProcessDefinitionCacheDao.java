package com.oilpeddler.wfengine.schedulecomponent.dao.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.schedulecomponent.tools.JSONUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class WfProcessDefinitionCacheDao {
    private static final String KEY_PATTERN = "wProcessDefinition:%s"; // wProcessDefinition:流程定义主键

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, Object> operations;

    private static String buildKey(String id) {
        return String.format(KEY_PATTERN, id);
    }
    public WfProcessDefinitionBO get(String id) {
        String key = buildKey(id);
        return (WfProcessDefinitionBO)operations.get(key);
        //return JSONUtil.parseObject(value, WfProcessDefinitionBO.class);
    }

    public void set(String id, WfProcessDefinitionBO object) {
        String key = buildKey(id);
        //String value = JSONUtil.toJSONString(object);
        operations.set(key, object);
    }

}
