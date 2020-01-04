package com.oilpeddler.wfengine.common.api.formservice;

import java.util.Map;

public interface ClientTaskService {
    boolean completeTask(String tiId, String pdId,String taskNo,Map<String, Object> requiredData);
}
