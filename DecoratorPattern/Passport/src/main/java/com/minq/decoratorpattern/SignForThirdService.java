package com.minq.decoratorpattern;

public class SignForThirdService implements ISignForThirdService{

    private ISignService sign;

    public SignForThirdService(ISignService sign) {
        this.sign = sign;
    }

    @Override
    public ResultMsg loginForQQ(String id) {
        return null;
    }

    @Override
    public ResultMsg loginForWechat(String id) {
        return null;
    }

    @Override
    public ResultMsg loginForToken(String token) {
        return null;
    }

    @Override
    public ResultMsg loginForTelphone(String telphone, String code) {
        return null;
    }

    @Override
    public ResultMsg loginForRegist(String username, String passport) {
        return null;
    }

    @Override
    public ResultMsg regist(String username, String password) {
        return sign.regist(username, password);
    }

    @Override
    public ResultMsg login(String username, String password) {
        return sign.login(username, password);
    }
}
