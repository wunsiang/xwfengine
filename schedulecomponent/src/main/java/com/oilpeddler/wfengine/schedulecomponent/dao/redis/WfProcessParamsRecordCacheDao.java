package com.oilpeddler.wfengine.schedulecomponent.dao.redis;

import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRecordDO;
import com.oilpeddler.wfengine.schedulecomponent.tools.JSONUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class WfProcessParamsRecordCacheDao {
    private static final String KEY_PATTERN = "enginePpName:%s;aiId:%s"; // wProcessDefinition:流程定义主键

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, String> operations;
    private static String buildKey(String enginePpName,String aiId) {
        return String.format(KEY_PATTERN, enginePpName,aiId);
    }
    public WfProcessParamsRecordDO get(String enginePpName, String aiId) {
        String key = buildKey(enginePpName,aiId);
        String value = operations.get(key);
        return JSONUtil.parseObject(value, WfProcessParamsRecordDO.class);
    }

    public void set(String enginePpName,String aiId, WfProcessParamsRecordDO object) {
        String key = buildKey(enginePpName,aiId);
        String value = JSONUtil.toJSONString(object);
        operations.set(key, value);
    }
}
