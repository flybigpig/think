# Thread

- 进程

  - 进程是操作系统结构的基础，是程序在一个数据集合上运行的过程，是系统进行资源分配和调度的基本单位

- 线程

  - 是操作系统调度的最小单元，也叫作轻量级进程。在一个进程中可以创建多个线程，这些线程都拥有各自的计数器、堆栈和局部变量等属性，并且能够访问共享的内存变量。

- 优点

  - • 使用多线程可以减少程序的响应时间。如果某个操作很耗时，或者陷入长时间的等待，此时程序将不会响应鼠标和键盘等的操作，使用多线程后可以把这个耗时的线程分配到一个单独的线程中去执行，从而使程序具备了更好的交互性。

    • 与进程相比，线程创建和切换开销更小，同时多线程在数据共享方面效率非常高。

    • 多CPU或者多核计算机本身就具备执行多线程的能力。如果使用单个进程，将无法重复利用计算机资源，这会造成资源的巨大浪费。在多CPU计算机中使用多线程能提高CPU的利用率。

    • 使用多线程能简化程序的结构，使程序便于理解和维护。

- ### 状态

  - • **New**：**新创建状态。线程被创建，还没有调用** start **方法，在线程运行之前还有一些基础工作要做。**

    • **Runnable**：可运行状态。一旦调用start方法，线程就处于Runnable状态。一个可运行的线程可能正在运行也可能没有运行，这取决于操作系统给线程提供运行的时间。

    • **Blocked**：阻塞状态。表示线程被锁阻塞，它暂时不活动。

    • **Waiting**：等待状态。线程暂时不活动，并且不运行任何代码，这消耗最少的资源，直到线程调度器重新激活它。

    • **Timed waiting**：超时等待状态。和等待状态不同的是，它是可以在指定的时间自行返回的。

    • **Terminated**：终止状态。表示当前线程已经执行完毕。导致线程终止有两种情况：第一种就是run方法执行完毕正常退出；第二种就是因为一个没有捕获的异常而终止了run方法，导致线程进入终止状态。

- ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\Thread.png)

- 创建线程

  - **继承Thread类，重写run（）方法**

  - Runnable

  - **.实现Callable接口，重写call（）方法**

    - ```
      public class Callable {
          /**
           *
           * 实现callable 接口
           * 线程池执行
           * future 回调
           *
           */
          public static class TestCallable implements java.util.concurrent.Callable {
      
              @Override
              public String call() throws Exception {
                  return "hellow orld";
              }
          }
      
          public static void main(String[] args) {
      
              TestCallable testCallable = new TestCallable();
              ExecutorService executorService = Executors.newSingleThreadExecutor();
              Future mFuture = executorService.submit(testCallable);
              try {
                  System.out.print(mFuture.get().toString());
              } catch (Exception e) {
                  e.printStackTrace();
              }
      
          }
      }
      ```

  
  
  #### 中断
  
  > 早期有线程有一个stop的方法，其他线程可以调用终止线程，但是现在已经被弃用了，
  >
  > 取而代之的是interrupt方法。
  >
  > > 当一个线程调用interrupt方法时，线程的中断标识为将被置位true，线程不时检测这个中断标识位，以判断线程是否应该被中断。要想知道线程是否被置位，可以调用Thread.currentThread().isInterrupted()
  >
  > 但是 弱国一个线程被阻塞，就无法监测终端状态。
  >
  > 如果一个线程处于阻塞状态，线程再检查中断标识位时如果发现中断标识位位true,则会在阻塞方法调用处抛出InterruptException异常，并且会再抛出异常前将线程的中断标识位复位，（false）
  >
  > 中断线程是为了引起线程的注意，被终端的线程可以决定如何去相应中断，如果是比较重要的线程则不会理会中断，大部分情况时线程会将中断作为一个终止的请求。
  >
  > > 不要再底层代码里获取InterruptException异常后不做处理。
  >
  > 两种处理方式：
  >
  > ```
  > 1 再catch子句中，调用Thread.currentThread.interrupt();来设置中断状态（因为抛出异常后，中断标识会复位），让外界通过判断Thread.currentThread().isInterrupted()来决定是否终止线程还是继续下去。
  > 2 更好的做法就是，不使用try来捕获这样的异常让方法直接抛出，这样调用者可以捕获这个异常
  > ```



#### 同步

###### 锁

Synchronized关键字自动提供了锁以及相关的条件。

> 重入锁ReentrantLock 标识该锁能够支持一个线程对资源的重复枷锁。5.0引入
>
> ```
> Lock mLock =new ReentrantLock();
> mLock.lock();
> try{
> 
> }finally{
> mLock.unlock();
> }
> ```

一旦一个线程封锁了锁对象，其他线程都无法进去Lock语句，把解锁的操作放在finally中是十分必要的。

如果再临界区发生了异常，锁是必须要释放的。否则其他线程将会永远被阻塞

在进入临界区时，却发现在某一个条件满足后，他才能执行。

**这时可以使用一个条件对象管理那些已经获取一个锁但是却不能做有用工作的线程**。条件对象有被称作条件变量。



##### 同步方法

Lock 和Condiction接口为程序设计人员提供了高度的锁定控制，然而大多数情况并不需要那样的控制，并且可以使用以重嵌入到java语言内部的机制

一般实现同步最好用java.util.concurrent包下提供的类

synchronized(lock){

}



##### volatile

volatile为实例域的同步访问提供了免锁的机制

> **volatile不保证原子性**
>
> **volatile保证有序性**



当一个共享变量被volatile修饰之后，其就具备了两个含义，一个是线程修改了变量的值时，变量的新值对其他线程是立即可见的。换句话说，就是不同线程对这个变量进行操作时具有可见性。另一个含义是禁止使用指令重排序。



并发编程中的3个特性：原子性、可见性和有序性。

> 1  对基本数据类型变量的读取和赋值操作是原子性操作，即这些操作是不可被中断的，要么执行完毕，要么就不执行。
>
> 2  是指线程之间的可见性，一个线程修改的状态对另一个线程是可见的
>
> 3  Java内存模型中允许编译器和处理器对指令进行重排序



> **synchronized**关键字可防止多个线程同时执行一段代码，那么这就会很影响程序执行效率。而**volatile**关键字在某些情况下的性能要优于**synchronized**。但是要注意**volatile**关键字是无法替代**synchronized**关键字的，因为**volatile**关键字无法保证操作的原子性。通常来说，使用**volatile**必须具备以下两个条件：
>
> （1）对变量的写操作不会依赖于当前值。
>
> （2）该变量没有包含在具有其他变量的不变式中。



> volatile变量是一种非常简单但同时又非常脆弱的同步机制，它在某些情况下将提供优于锁的性能和伸缩性。如果严格遵循volatile的使用条件，即变量真正独立于其他变量和自己以前的值，在某些情况下可以使用volatile代替synchronized来简化代码。然而，使用volatile的代码往往比使用锁的代码更加容易出错。在前面的第 4 小节中介绍了可以使用 volatile 代替synchronized的最常见的两种用例，在其他情况下我们最好还是使用synchronized。





#### java内存模型

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\memberCache.png)



> Java内存模型规定了所有的变量都存储在主内存（Main Memory）中（此处的主内存与介绍物理硬件时的主内存名字一样，两者也可以互相类比，但此处仅是虚拟机内存的一部分）。每条线程还有自己的工作内存（Working Memory，可与前面讲的处理器高速缓存类比），线程的工作内存中保存了被该线程使用到的变量的主内存副本拷贝[[4\]](https://www.neat-reader.cn/webapp#filepos1042676)，线程对变量的所有操作（读取、赋值等）都必须在工作内存中进行，而不能直接读写主内存中的变量[[5\]](https://www.neat-reader.cn/webapp#filepos1043154)。不同的线程之间也无法直接访问对方工作内存中的变量，线程间变量值的传递均需要通过主内存来完成，线程、主内存、工作内存三者的交互关系如图12-2所示。



### 阻塞



1  当队列中没有数据时，消费者端的所有线程都会自动挂起，直到有数据放入队列

2  当队列中填满数据的情况下，生产者端的所有线程都会被自动挂起，知道队列中有空的位置，线程会自动唤醒



### BlockingQueue

在Java中提供了7个阻塞队列，它们分别如下所示。

• ArrayBlockingQueue：由数组结构组成的有界阻塞队列。

• LinkedBlockingQueue：由链表结构组成的有界阻塞队列。

• PriorityBlockingQueue：支持优先级排序的无界阻塞队列。

• DelayQueue：使用优先级队列实现的无界阻塞队列。

• SynchronousQueue：不存储元素的阻塞队列。

• LinkedTransferQueue：由链表结构组成的无界阻塞队列。

• LinkedBlockingDeque：由链表结构组成的双向阻塞队列。





#### 线程池








