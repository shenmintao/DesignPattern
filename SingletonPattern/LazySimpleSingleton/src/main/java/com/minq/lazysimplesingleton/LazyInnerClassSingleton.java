package com.minq.lazysimplesingleton;

public class LazyInnerClassSingleton {
    //使用LazyInnerClassSingleton的时候，默认会先初始化内部类
    //如果没有使用，则内部类不会加载
    private LazyInnerClassSingleton(){
        if(LazyHolder.LAZY_INNER_CLASS_SINGLETON != null) {
            throw new RuntimeException("不允许创建多个实例");
        }
    };

    private static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.LAZY_INNER_CLASS_SINGLETON;
    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleton LAZY_INNER_CLASS_SINGLETON = new LazyInnerClassSingleton();
    }
}

