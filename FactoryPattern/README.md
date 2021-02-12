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

## wait()与notify()

https://www.liaoxuefeng.com/wiki/1252599548343744/1306580911915042

synchronized并没有解决多线程协调的问题。

有如下程序

```java
class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
    }

    public synchronized String getTask() {
        while (queue.isEmpty()) {
        }
        return queue.remove();
    }
}
```

上述代码看上去没有问题：getTask()内部先判断队列是否为空，如果为空，就循环等待，直到另一个线程往队列中放入了一个任务，while()循环退出，就可以返回队列的元素了。

但实际上while()循环永远不会退出。因为线程在执行while()循环时，已经在getTask()入口获取了this锁，其他线程根本无法调用addTask()，因为addTask()执行条件也是获取this锁。

因此，执行上述代码，线程会在getTask()中因为死循环而100%占用CPU资源。

如果深入思考一下，我们想要的执行效果是：

线程1可以调用addTask()不断往队列中添加任务；

线程2可以调用getTask()从队列中获取任务。如果队列为空，则getTask()应该等待，直到队列中至少有一个任务时再返回。

因此，多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务。

对于上述TaskQueue，我们先改造getTask()方法，在条件不满足时，线程进入等待状态：

```java
public synchronized String getTask() {
    while (queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

当一个线程执行到getTask()方法内部的while循环时，它必定已经获取到了this锁，此时，线程执行while条件判断，如果条件成立（队列为空），线程将执行this.wait()，进入等待状态。

这里的关键是：wait()方法必须在当前获取的锁对象上调用，这里获取的是this锁，因此调用this.wait()。

调用wait()方法后，线程进入等待状态，wait()方法不会返回，直到将来某个时刻，线程从等待状态被其他线程唤醒后，wait()方法才会返回，然后，继续执行下一条语句。

有些仔细的童鞋会指出：即使线程在getTask()内部等待，其他线程如果拿不到this锁，照样无法执行addTask()，肿么办？

这个问题的关键就在于wait()方法的执行机制非常复杂。首先，它不是一个普通的Java方法，而是定义在Object类的一个native方法，也就是由JVM的C代码实现的。其次，必须在synchronized块中才能调用wait()方法，因为wait()方法调用时，会释放线程获得的锁，wait()方法返回后，线程又会重新试图获得锁。

因此，只能在锁对象上调用wait()方法。因为在getTask()中，我们获得了this锁，因此，只能在this对象上调用wait()方法：

```
public synchronized String getTask() {
    while (queue.isEmpty()) {
        // 释放this锁:
        this.wait();
        // 重新获取this锁
    }
    return queue.remove();
}
```

当一个线程在this.wait()等待时，它就会释放this锁，从而使得其他线程能够在addTask()方法获得this锁。

现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从wait()方法返回？答案是在相同的锁对象上调用notify()方法。我们修改addTask()如下：

```java
public synchronized void addTask(String s) {
    this.queue.add(s);
    this.notify(); // 唤醒在this锁等待的线程
}
```

这个例子中，我们重点关注addTask()方法，内部调用了this.notifyAll()而不是this.notify()，使用notifyAll()将唤醒所有当前正在this锁等待的线程，而notify()只会唤醒其中一个（具体哪个依赖操作系统，有一定的随机性）。这是因为可能有多个线程正在getTask()方法内部的wait()中等待，使用notifyAll()将一次性全部唤醒。通常来说，notifyAll()更安全。有些时候，如果我们的代码逻辑考虑不周，用notify()会导致只唤醒了一个线程，而其他线程可能永远等待下去醒不过来了。

但是，注意到wait()方法返回时需要重新获得this锁。假设当前有3个线程被唤醒，唤醒后，首先要等待执行addTask()的线程结束此方法后，才能释放this锁，随后，这3个线程中只能有一个获取到this锁，剩下两个将继续等待。

**wait()方法在线程被唤醒后会直接接着后面执行，因此如果有条件需要判断的话在wait()后还需要再处理一次**

## 其它几种锁

### ReentrantLock 

https://www.liaoxuefeng.com/wiki/1252599548343744/1306580960149538

```java
private final Lock lock = new ReentrantLock();
lock.lock();    //上锁
lock.unlock();  //解锁
lock.tryLock(1, TimeUnit.SECONDS);  //尝试获得锁，在超时后返回false
```

用于替代synchronized的wait和notify

### Condition

https://www.liaoxuefeng.com/wiki/1252599548343744/1306581033549858

```java
class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}
```

Condition提供的await()、signal()、signalAll()原理和synchronized锁对象的wait()、notify()、notifyAll()是一致的，并且其行为也是一样的：

await()会释放当前锁，进入等待状态；

signal()会唤醒某个等待线程；

signalAll()会唤醒所有等待线程；

唤醒线程从await()返回后需要重新获得锁。

此外，和tryLock()类似，await()可以在等待指定时间后，如果还没有被其他线程通过signal()或signalAll()唤醒，可以自己醒来：

### ReadWriteLock 读写锁

只有一个线程能获得写锁，所有线程都能获得读锁

```java
public class Counter {
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index) {
        wlock.lock(); // 加写锁
        try {
            counts[index] += 1;
        } finally {
            wlock.unlock(); // 释放写锁
        }
    }

    public int[] get() {
        rlock.lock(); // 加读锁
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            rlock.unlock(); // 释放读锁
        }
    }
}
```

ReadWriteLock只允许一个线程写入；

ReadWriteLock允许多个线程在没有写入时同时读取；

ReadWriteLock适合读多写少的场景。

### StampedLock 乐观锁

StampedLock和ReadWriteLock相比，改进之处在于：读的过程中也允许获取写锁后写入！这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。

```java
public class Point {
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
        // 注意下面两行代码不是原子操作
        // 假设x,y = (100,200)
        double currentX = x;
        // 此处已读取到x=100，但x,y可能被写线程修改为(300,400)
        double currentY = y;
        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = stampedLock.readLock(); // 获取一个悲观读锁
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

StampedLock把读锁细分为乐观读和悲观读，能进一步提升并发效率。但这也是有代价的：一是代码更加复杂，二是StampedLock是不可重入锁，不能在一个线程中反复获取同一个锁。

**可重入是某个线程已经获得某个锁，可以再次获取锁而不会出现死锁。**

## Concurrent集合

**线程安全的数据结构**

BlockingQueue的意思就是说，当一个线程调用这个TaskQueue的getTask()方法时，该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，getTask()方法才会返回。

 ，所以我们不必自己编写，可以直接使用Java标准库的java.util.concurrent包提供的线程安全的集合：ArrayBlockingQueue。

除了BlockingQueue外，针对List、Map、Set、Deque等，java.util.concurrent包也提供了对应的并发集合类。我们归纳一下：

| interface |       non-thread-safe |      thread-safe     |
|   ----    |          ----         |        ----          |
|   List    |        ArrayList      | CopyOnWriteArrayList |
|   Map     |        HashMap        |  ConcurrentHashMap   |
|   Queue   |ArrayDeque / LinkedList|ArrayBlockingQueue / LinkedBlockingQueue |
|   Deque   |ArrayDeque / LinkedList|  LinkedBlockingDeque |

