package com.oilpeddler.wfengine.common.api.formservice;

public interface ClientProcessService {
    boolean startProcess(String pdId,String piName,String piStarter,String piBusinesskey);
}
