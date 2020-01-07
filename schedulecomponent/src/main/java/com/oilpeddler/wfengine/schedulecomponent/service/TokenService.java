package com.oilpeddler.wfengine.schedulecomponent.service;

import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.element.Process;

public interface TokenService {
    public Token recoverTokens(String piId,String pdId,String elementNo, Process process);

    Token getCurrentToken(String piId,String elementNo, Process process);

    Token getFromCache(String id);
    void setToCache(Token token);
}
