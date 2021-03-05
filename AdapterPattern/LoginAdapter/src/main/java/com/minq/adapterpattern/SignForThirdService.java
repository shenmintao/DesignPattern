package com.minq.adapterpattern;

public class SignForThirdService extends SignService{
    public ResultMsg loginForQQ(String openId) {
        //1. openId是全局唯一的，我们可以把它当作一个用户名（加长）
        //2. 密码默认为QQ_EMPTY
        //3. 注册（在原有系统里创建一个用户）
        //4. 调用原来的登录方式

        return loginForRegist(openId, null);
    }

    public ResultMsg loginForWechat(String openId) {
        return null;
    }

    public ResultMsg loginForToken (String token) {
        //通过token获取用户信息，然后重新登录一次
        return null;
    }

    public ResultMsg loginForTelphone(String telphone, String code) {
        return null;
    }

    public ResultMsg loginForRegist(String username, String password) {
        super.regist(username, password);
        return super.login(username, password);
    }
}
