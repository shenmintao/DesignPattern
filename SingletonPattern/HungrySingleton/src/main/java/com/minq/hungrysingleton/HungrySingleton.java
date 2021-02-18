package com.minq.hungrysingleton;

public class HungrySingleton {

    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton() {};

    public static HungrySingleton getInstance() {
        return  hungrySingleton;
    }
}
