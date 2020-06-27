package com.oilpeddler.wfengine.schedulecomponent.dao.redis;

import com.oilpeddler.wfengine.schedulecomponent.element.BpmnModel;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BpmnModelCacheDao {
    private static final String KEY_PATTERN = "BpmnModel:%s"; // wProcessDefinition:流程定义主键

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, Object> operations;

    private static String buildKey(String id) {
        return String.format(KEY_PATTERN, id);
    }
    public BpmnModel get(String id) {
        String key = buildKey(id);
        return (BpmnModel)operations.get(key);
        //return JSONUtil.parseObject(value, WfProcessDefinitionBO.class);
    }

    public void set(String id, BpmnModel object) {
        String key = buildKey(id);
        //String value = JSONUtil.toJSONString(object);
        operations.set(key, object);
    }
}
