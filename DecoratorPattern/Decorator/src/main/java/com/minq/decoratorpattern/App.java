package com.minq.decoratorpattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Battercake battercake;
        battercake = new BaseBattercake();
        battercake = new EggDecorator(battercake);
        battercake = new SausageDecorator(battercake);

        //跟静态代理最大的区别就是职责不同
        //静态代理不一定满足is-a的关系
        //静态代理会做功能增强，使同一个职责变得不一样

        //装饰者更多考虑扩展
        System.out.println(battercake.getMsg() + ",总价：" + battercake.getPrice());
    }
}
