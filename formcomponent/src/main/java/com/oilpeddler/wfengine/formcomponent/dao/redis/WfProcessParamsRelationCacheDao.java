package com.oilpeddler.wfengine.formcomponent.dao.redis;

import com.oilpeddler.wfengine.formcomponent.dataobject.WfProcessParamsRelationDO;
import com.oilpeddler.wfengine.formcomponent.tools.JSONUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class WfProcessParamsRelationCacheDao {
    private static final String KEY_PATTERN = "businessName:%s;pdId:%s"; // wProcessDefinition:流程定义主键

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, String> operations;
    private static String buildKey(String businessName,String pdId) {
        return String.format(KEY_PATTERN, businessName,pdId);
    }
    public WfProcessParamsRelationDO get(String businessName, String pdId) {
        String key = buildKey(businessName,pdId);
        String value = operations.get(key);
        return JSONUtil.parseObject(value, WfProcessParamsRelationDO.class);
    }

    public void set(String businessName,String pdId, WfProcessParamsRelationDO object) {
        String key = buildKey(businessName,pdId);
        String value = JSONUtil.toJSONString(object);
        operations.set(key, value);
    }
}
