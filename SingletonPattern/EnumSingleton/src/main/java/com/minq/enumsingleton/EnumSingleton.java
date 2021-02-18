package com.minq.enumsingleton;

//C#里就没这破玩意，简直脑洞清奇
public enum EnumSingleton {
    INSTANCE;

    private Object data;
    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}
