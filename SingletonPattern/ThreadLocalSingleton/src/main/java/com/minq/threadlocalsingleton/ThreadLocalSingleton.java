package com.minq.threadlocalsingleton;

public class ThreadLocalSingleton {
    private static final ThreadLocal<ThreadLocalSingleton> threadlocalInstance =
            new ThreadLocal<ThreadLocalSingleton>() {
                @Override
                protected ThreadLocalSingleton initialValue(){
                    return new ThreadLocalSingleton();
            }
    };

    private ThreadLocalSingleton(){};

    public static ThreadLocalSingleton getInstance() {
        return threadlocalInstance.get();
    }
}
