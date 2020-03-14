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

- 中断

  - 















