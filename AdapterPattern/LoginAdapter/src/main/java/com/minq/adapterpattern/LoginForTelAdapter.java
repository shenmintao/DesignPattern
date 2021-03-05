package com.minq.adapterpattern;

public class LoginForTelAdapter implements LoginAdapter{
    public boolean support(Object adapter) {
        return adapter instanceof LoginForTelAdapter;
    }
    public ResultMsg login(String id, Object adapter) {
        return null;
    }
}
