package com.minq.adapterpattern;

public class LoginForTokenAdapter implements LoginAdapter{
    public boolean support(Object adapter) {
        return adapter instanceof LoginForTokenAdapter;
    }
    public ResultMsg login(String id, Object adapter) {
        return null;
    }
}
