package com.minq.lazysimplesingleton;

public class LazySimpleSingleton {
    private LazySimpleSingleton(){};

    private volatile static LazySimpleSingleton lazySimpleSingleton = null;
    public synchronized static LazySimpleSingleton getInstance() {
        if(lazySimpleSingleton == null) {
            lazySimpleSingleton = new LazySimpleSingleton();
        }
        return lazySimpleSingleton;
    }
}
