# 问题合集

## 1. 为什么不使用JavaCourse course = new JavaCourse(); 而使用ICourse course = new JavaCourse();

https://www.jianshu.com/p/0f7b6a2b21fe

应该使用接口而不是类作为参数类型。

更通常地说，应该更喜欢使用接口而不是类来引用对象。如果存在适当的接口类型，那么应该使用接口类型声明参数、返回值、变量和属性。真正需要引用对象的类的惟一时机是使用构造方法创建它的时候。

例如

使用如下的方法创建一个LinkedHashset
```java
// Good - uses interface as type
Set<Son> sonSet = new LinkedHashSet<>();
```
而不是
```java
// Bad - uses class as type!
LinkedHashSet<Son> sonSet = new LinkedHashSet<>();
```
这样如果要切换实现的话，就可以采用如下的方式
```java
Set<Son> sonSet = new HashSet<>();
```
同时其它调用这个对象的代码并不知道已经发生了改变

但是

**如果不存在适当的接口，则通过类而不是接口引用对象是完全合适的**

例如，考虑值类，例如String和BigInteger。 值类很少用多个实现类来编写。 它们通常是final的，很少有相应的接口。 将这样的值类用作参数，变量，属性或返回类型是完全合适的。

## 2. synchronized关键字

https://blog.csdn.net/yhl_jxy/article/details/87012803

synchronized是Java中的关键字，是一种同步锁。它有以下几种使用方法、

1. 对象锁

普通同步方法，锁是当前实例对象。比如：
```java
public synchronized void doLongTimeTaskC() {}
```

2、类锁

静态同步方法，锁是当前类的Class对象。比如：

```java
public synchronized static void doLongTimeTaskA() {}
```

3、同步代码块上的对象锁或类锁

加在同步代码块上，锁是Synchonized括号里配置的对象，可以是实例对象，也可以是Class对象；

```java
public void doLongTimeTaskD() {
    // 对象锁
    synchronized (this) {
    }
}
```

或

```java
public static void doLongTimeTaskE() {
    // 类锁
    synchronized (Task.class) {
    }
}
```

对象锁和类锁是两个完全不一样的锁，下面通过实例看看他们的区别。

### 对象锁

对象锁分两种情况说明，分别是在实例方法上加锁或在实例方法的代码块上加锁。

下面讨论多个线程持有多个对象，每一个线程控制自己的对象锁，

线程之间异步执行。简单说就是一个线程一个对象，谁也不影响谁。

```java
public class Task {
 
    /**
     * 对象锁：普通同步方法，锁为当前实例对象。
     */
    public synchronized void doLongTimeTaskA() {
        System.out.println("name = " + Thread.currentThread().getName() + ", begain");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("name = " + Thread.currentThread().getName() + ", end");
 
    }
 
    /**
     * 对象锁：同步代码块，锁为代码块里面的实例对象。
     */
    public void doLongTimeTaskB() {
        synchronized (this) {
            System.out.println("name = " + Thread.currentThread().getName() + ", begain");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("name = " + Thread.currentThread().getName() + ", end");
        }
    }
}
```

ThreadA

```java
/**
 * @author yihonglei
 */
public class ThreadA extends Thread {
    private Task mTask;
 
    public ThreadA(Task tk) {
        mTask = tk;
    }
 
    @Override
    public void run() {
        mTask.doLongTimeTaskA();
    }
}
```

ThreadB

```java

/**
 * @author yihonglei
 */
public class ThreadB extends Thread {
    private Task mTask;
 
    public ThreadB(Task tk) {
        mTask = tk;
    }
 
    @Override
    public void run() {
        mTask.doLongTimeTaskB();
    }
}
```

RunObjectTest

```java

public class RunObjectTest {
    public static void main(String[] args) {
        Task mTaskA = new Task();
        Task mTaskB = new Task();
 
        ThreadA ta1 = new ThreadA(mTaskA);
        ThreadA ta2 = new ThreadA(mTaskB);
 
        ThreadB tb1 = new ThreadB(mTaskA);
        ThreadB tb2 = new ThreadB(mTaskB);
 
        ta1.setName("A1");
        ta2.setName("A2");
 
        tb1.setName("B1");
        tb2.setName("B2");
 
        ta1.start();
        ta2.start();
        tb1.start();
        tb2.start();
    }
}
```

运行结果

```java
name = A1, begin
name = A2, begin
name = A2, end
name = A1, end
name = B2, begin
name = B1, begin
name = B1, end
name = B2, end
```

从运行结果可以看到A1,begin开始后，并没有接着输出A1,end，而是输出了A2,begin，说明两个线程用的不是同一个锁

如果用的是同一把锁，A1,begin输出，必然需要等A1,end结束释放锁后，A2获取锁才会接着输出A2,begin。

所以A1，A2分别用的是自己持有对象的锁，线程自己管自己的锁，互不影响，线程异步执行，而不是排队等待执行。对于B1和B2也是同理。

总结：

多线程分别持有多个对象，每个线程异步执行对象的同步方法，因为JVM为每个对象创建了锁。

如果想让线程排队执行，让多个线程持有同一个对象，线程就会排队执行。

### 类锁

类锁，有在静态方法上加锁的，也有在静态方法代码块上加锁的。

Task

```java
public class Task {
 
    /** 静态对象 */
    private static Object object = new Object();
 
    /**
     * 类锁：静态同步方法，锁为当前Class对象。
     */
    public synchronized static void doLongTimeTaskC() {
        System.out.println("name = " + Thread.currentThread().getName() + ", begin");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("name = " + Thread.currentThread().getName() + ", end");
    }
 
    /**
     * 类锁：静态同步方法，锁为当前Class对象。
     */
    public synchronized static void doLongTimeTaskD() {
        System.out.println("name = " + Thread.currentThread().getName() + ", begin");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("name = " + Thread.currentThread().getName() + ", end");
    }
 
    /**
     * 同步代码块：里面的对象可以是Class对象，也可以是实例对象。
     */
    public static void doLongTimeTaskE() {
        synchronized (Task.class) {// Class对象
        //synchronized (object) {// 实例对象
            System.out.println("name = " + Thread.currentThread().getName() + ", begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("name = " + Thread.currentThread().getName() + ", end");
        }
    }
}
```

然后创建3个线程，分别调用TaskA中的3个方法。

ThreadC

```java
public class ThreadC extends Thread {
    private Task mTask;
 
    public ThreadC(Task tk) {
        mTask = tk;
    }
 
    @Override
    public void run() {
        mTask.doLongTimeTaskC();
    }
}
```

ThreadD

```java
public class ThreadD extends Thread {
    private Task mTask;
 
    public ThreadD(Task tk) {
        mTask = tk;
    }
 
    @Override
    public void run() {
        mTask.doLongTimeTaskD();
    }
}
```

ThreadE

```java
public class ThreadE extends Thread {
    private Task mTask;
 
    public ThreadE(Task tk) {
        mTask = tk;
    }
 
    @Override
    public void run() {
        mTask.doLongTimeTaskE();
    }
}
```

RunClassTest

```java
public class RunClassTest {
    public static void main(String[] args) {
        Task mTask = new Task();
        ThreadC tc = new ThreadC(mTask);
        ThreadD td = new ThreadD(mTask);
        ThreadE te = new ThreadE(mTask);
 
        tc.setName("C");// 静态同步方法
        td.setName("D");// 静态同步方法
        te.setName("E");// 静态方法同步代码块
 
        tc.start();
        td.start();
        te.start();
    }
}
```

运行结果

```java
name = C, begin
name = C, end
name = D, begin
name = D, end
name = E, begin
name = E, end
```

从程序运行结果可以看到，结果按照某个线程begin，然后接着输出end，说明线程按顺序执行同步方法。

因为，三个线程持有的是Task的Class类锁，是同一个锁，所以线程需要排队等待执行，直到获取锁才能执行，

这就是结果按顺序输出的原因，这也是类锁的特性，多个线程持有一个类锁，排队执行，持有就是王者，

否则就排队等待。

## 3. abstract关键字

如果一个类中没有包含足够的信息来描绘一个具体的对象，这样的类就是抽象类。

抽象类除了不能实例化对象之外，类的其它功能依然存在，成员变量、成员方法和构造方法的访问方式和普通类一样。

由于抽象类不能实例化对象，所以抽象类必须被继承，才能被使用。也是因为这个原因，通常在设计阶段决定要不要设计抽象类。

父类包含了子类集合的常见的方法，但是由于父类本身是抽象的，所以不能使用这些方法。

## 三种反射类的方式

```java
Class.forName("com.minq.DBConnectionPool.Pool").newInstance();
Pool.class.newInstance();
Pool.class.getConstructor().newInstance();
```

其中Class.forName("com.minq.DBConnectionPool.Pool").newInstance()与Pool.class.newInstance()等价

Class.newInstance()只能反射无参的构造器；

Constructor.newInstance()可以反任何构造器；

Class.newInstance()需要构造器可见(visible)；

例子：

假如你设置一个private Pool(){};

那么这个构造器就是不可见的，在反射的时候会报

can not access a member of class com.minq.simplefactorypattern.JavaCourse with modifiers "private"

而protected Pool(){}则被认为是可见的

Constructor.newInstance()可以反私有构造器；

Class.newInstance()对于捕获或者未捕获的异常均由构造器抛出;

Constructor.newInstance()通常会把抛出的异常封装成InvocationTargetException抛出；

**Class.forNam.newInstance()与XXX.class.newInstance()在Java 9中被标记为已过时**

## Spring单例模式为什么可以支持多线程并发？

spring单例模式是指，在内存中只实例化一个类的对象

类的变量有线程安全的问题，就是有get和set方法的类成员属性。

**执行单例对象的方法不会有线程安全的问题 因为方法是磁盘上的一段代码，每个线程在执行这段代码的时候，会自己去内存申请临时变量**

为什么局部变量不会受多线程影响？

对于那些会以多线程运行的单例类，例如Web应用中的Servlet，每个方法中对局部变量的操作都是在线程自己独立的内存区域内完成的，所以是线程安全的

局部变量不会受多线程影响

成员变量会受到多线程影响

**对于成员变量的操作，可以使用ThreadLocal来保证线程安全**

## JVM是如何实现线程的独立内存空间？

Java中的栈

每当启用一个线程时，JVM就为他分配一个Java栈，栈是以帧为单位保存当前线程的运行状态。

某个线程正在执行的方法称为当前方法，当前方法使用的栈帧称为当前帧，当前方法所属的类称为当前类，当前类的常量池称为当前常量池。

当线程执行一个方法时，它会跟踪当前常量池。

每当线程调用一个Java方法时，JVM就会在该线程对应的栈中压入一个帧，这个帧自然就成了当前帧。

当执行这个方法时，它使用这个帧来存储参数、局部变量、中间运算结果等等。

Java栈上的所有数据都是私有的。任何线程都不能访问另一个线程的栈数据。所以我们不用考虑多线程情况下栈数据访问同步的情况。

## Service为什么是线程安全的

**userService本身并不是线程安全的**

你在userController里修改userService吗？

**只是调用userService里的方法吧？**

方法都是线程安全的，多线程调用一个实例的方法，会在内存中复制变量，所以只要你不在userConstroller里修改userService这个实例就没问题。

## Vector与ArrayList的区别

1. Vector是线程安全的，ArrayList不是线程安全的。

2. ArrayList在底层数组不够用时在原来的基础上扩展0.5倍，Vector是扩展1倍。

只要是关键性的操作，Vector的方法前面都加了synchronized关键字，来保证线程的安全性。

当执行synchronized修饰的方法前，系统会对该方法加一把锁，方法执行完成后释放锁，加锁和释放锁的这个过程，在系统中是有开销的，因此，在单线程的环境中，Vector效率要差很多。

**（多线程环境不允许用ArrayList，需要做处理）**

**（Vector现在已经不常用了）**

**想让ArrayList变为线程安全的，请使用List<T> list = Collections.synchronizedList(ArrayList);**