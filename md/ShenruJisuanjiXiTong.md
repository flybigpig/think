# 深入 JVM

##### 基础知识

java虚拟机

类的加载机制不在java虚拟机内

java 文件被编译成class 文件后 被类加载器加载到java虚拟机 再到从内存中卸载--类的生命周期

包括：**加载**、**链接**、**初始化**、**使用**、**卸载**;

其中链接又分为:**验证**，**准备**，**解析**

```
接下来大致介绍一下各个阶段的工作：

1）加载：查找并加载Class文件。

2）链接：包括验证、准备、和解析。

验证：确保被导入类型的正确性

准备：为类的静态字段分配字段，并使用默认值初始化这个字段

解析：虚拟机将常量池内的符号引用替换为直接引用

3）初始化：将类变量初始化为正确的初始值

其他的就是使用和卸载，不必多说


类实例化：
为新的对象分配内存
为实例变量赋予默认值
为实例变量赋正确的初始值
java编译器为他编译的每一个类至少生成一个实例初始化方法<init>
静态实例初始化<Cinit>





```



## Dalvik虚拟机

Dalvik虚拟机（Dalvik Virtual Machine），简称Dalvik VM或者DVM。

为什么开篇说DVM不属于JVM的范畴，下面给出答案。

> ### DVM与JVM的区别

1）**基于的架构不同**

JVM基于栈，意味着需要去栈中读写数据，所需要的指令会更多，这样会导致速度变慢，对于性能有限的移动设备显然不合适。DVM是基于寄存器的，它没有基于栈的虚拟机在复制数据时而使用大量的出入栈指令，同事指令更紧凑、简洁。但是由于显式的制定了操作数，所以基于寄存器的指令会比基于栈的指令要大。

2）**执行字节码不同**

JVM中，java类被编译成一个或多个.class文件，并打包成.jar文件，而后JVM会通过相应的.class文件和.jar文件获取相应的字节码。
 而DVM会用dx工具把所有的class文件打包成一个.dex文件，然后DVM会从该.dex文件中读取指令和数据。这个.dex文件将所有的.class文件里面所包含的信息全部整合到了一起，这样加载就加快了速度。

3）**DVM允许在有限的内存中同时运行多个进程**

DVM经过优化，允许在有限的内存中同时运行多个进程。在Android中每一个应用都运行在一个DVM实例中，每一个DVM实例都运行在一个独立的进程空间，独立的进程可以防止虚拟机崩溃时所有程序都被关闭。

4）**DVM由Zygote创建并初始化**

在[《Android系统启动流程》](https://www.jianshu.com/p/2c1318b0f527)中提到过，Zygote是一个DVM进程，同时也用来创建和初始化其他DVM进程。每当系统需要一个应用程序进程的时候，Zygote就会fork自身，快速地创建和初始化一个DVM实例，用于程序运行。对于一些只读的系统库，所有的DVM实例都会和Zygote共享一块内存区域，节省内存开销。

5）**DVM有共享机制**

DVM拥有预加载-共享机制，不同应用之间运行时可以共享相同的类，拥有更高的效率。而JVM机制不存在这种共享机制。不同的程序，打包以后程序都是彼此独立的，即便是他们使用了相同的类，运行时也都是单独加载和运行的。

6）**DVM早期没有JIT编译器**

JVM使用了JIT（Just In Time Compiler），而DVM早期没有使用JIT编译器。早期DVM执行代码，都需要解释器将dex代码编译成机器码，然后交给系统处理，效率不是很高。Android 2.2之后开始为DVM使用了JIT编译器，它会对多次运行的代码（热点代码）进行编译，生成相当精简的本地机器码（Native Code），这样在下次执行到相同的逻辑时，直接使用编译好的机器码即可。需要注意的是，应用程序每次重新运行的时候，都需要重做这个编译工作。

## ART虚拟机

ART（Android Runtime）虚拟机是Android 4.4发布的，用来替换Dalvik虚拟机。Android 4.4默认还是DVM，但是可以在开发者选项中切换成ART。在Android 5.0之后默认采用了ART，从此DVM退出了历史舞台。

> ### ART与DVM的区别

1）前文了解到，DVM中的应用每次运行时，字节码都需要通过JIT编译器编译为机器码，这样会使应用程序的运行效率降低。而在ART中，系统安装应用程序时会进行一次AOT（ahead of time compilation），将字节码预编译成机器码并存储在本地，这样应用程序每次运行时就不需要执行编译了，会大大增加效率。但是AOT不是完美的，它的缺点主要有两个：第一个是AOT会使安装应用的时间变长，尤其是复杂的应用。第二个是字节码预先编译成机器码，机器码需要存储空间会多一些。为了解决这两个问题，Android 7.0版本中的ART加入了JIT即时编译器，作为AOT的一个补充。应用程序安装时并不会将字节码全部编译成机器码，而是在系统运行中将热点代码编译成机器码，从而缩短应用程序安装时间，并且节省内存。

2）DVM是为32位CPU设计的，而ART是支持64位并且兼容32位CPU，这也是DVM被淘汰的主要原因之一。

3）ART对垃圾回收机制进行了改进，比如更频繁的执行并行垃圾收集，将GC暂停由2次减少为1次等等。

4）ART运行时堆空间划分和DVM不同。

下面主要讲一下ART对垃圾回收机制的改进：

> ### ART垃圾回收

DVM的垃圾回收算法采用的是标记-清除算法（Mark-Sweep），ART改进了该算法，并且使用了多种垃圾收集器。

1.Concurrent Mark Sweep（CMS）：CMS收集器是一种获取最短收集暂停时间为目标的收集器，采用了标记-清除算法实现。它是完整的堆垃圾收集器。

2.Concurrent Partial Mark Sweep：部分完整的堆垃圾收集器

3.Concurrent Sticky Mark Sweep：粘性收集器，基于分代垃圾收集思想，它只能释放上次GC以来分配的对象。这个垃圾收集器比一个完整或者部分完整的垃圾收集器扫描的更加频繁，因为它更快而且暂停时间更短。

4.Marksweep + Semispace：肺病发的GC，复制GC用于堆转换以及齐性空间压缩（碎片整理）。

## 总结

DVM和ART知识体系完全可以写一本书，如果想要更多的了解它请阅读专业的书籍和博客，比如老罗的博客。



------

------



- JDK  Java Development

  - java程序设计语言
  - java 虚拟机
  - JavaApi

- Class

-  ![image-20200311111852732](C:\Users\czdxn\AppData\Roaming\Typora\typora-user-images\image-20200311111852732.png)

- 程序计数器

  - 一块较小的内存空间
  - 每个线程都会有一个独立的程序计数器。因此，程序计数器是**线程私有**的
  - 可以看成是当前线程执行字节码的行号指示器
  - 字节码指示器工作时就是通过改变这个计数器的值来取下一条执行的字节码指令（分支、循环、跳转、异常处理、线程恢复等）
  -  java function---计数器记录的是正在执行的虚拟机字节码指令地址
  - native function-- 计数器值为空（Undefined）
  - 此内存区域是唯一一个在java虚拟机规范中没有规定任何OutOfMemoryError情况的区域

- Java 虚拟机栈

  - 与程序计数器一样，java虚拟栈（JavaStacks）也是线程私有的
  - 生命周期和线程相同
  - 虚拟机栈描述的是java 方法执行的内存模型：每个方法都在执行的同时都会创建一个栈帧 用于存储局部变量、操作数栈、动态链接、方法出口等信息。
  - Java内存区分为堆内存（Heap）和栈内存（Stack），这种分法比较粗糙
  - 栈就是讲的虚拟机栈，或者说虚拟机栈中的局部变量表部分，局部变量表存放了编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用（reference类型，它不等同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）和returnAddress类型（指向了一条字节码指令的地址）。
  - 64位长度的long和double 类型的数据会占用两个局部变量控件（slot）,其余只占一个。所以进入一个方法时，桢在分配多大是确定的，在运行期间不会改变局部变量表的大小
  - 异常1：如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError
  - 异常2：如果虚拟机栈可以动态扩展（当前大部分的java虚拟机哦都可以动态扩展，只不过java虚拟机规范中也允许固定长度的虚拟机栈）如果扩展时无法申请到足够的内存，就会抛出OutOfMemoryError
  - 栈帧是方法运行时的基础数据结构

- 本地方法栈

  - 本地方法栈位虚拟机使用到的native方法服务。
  - 本地方法栈区域也会抛出StackOverflowError和OutOfMemoryError异常。

- java堆

  - java heap 算是java虚拟机管理的内存中最大的一块了
  - 被所有线程共享的一块内存区域
  - 在创建时，此内存区域的唯一目的是存放对象实例，几乎所有的对象实例都在这里分配。
  - 但是随着JIT编译器的发展与逃逸分析技术逐渐成熟，栈上分配、标量替换[[2\]](https://www.neat-reader.cn/webapp#filepos160836)优化技术将会导致一些微妙的变化发生，所有的对象都分配在堆上也渐渐变得不是那么“绝对”了。
  - Java堆是垃圾收集器管理的主要区域，因此很多时候也被称做“GC堆”（Garbage Collected Heap，幸好国内没翻译成“垃圾堆”）
  - 现在收集器的基本都采用的是分代收集算法，所以还可以分位新生代 和 老年代；再细致还有Eden空间 From Survivor空间，To Survivor空间等
  - 从内存分配的角度来看，线程共享的java堆可能划分出多个线程私有的分配缓冲区，但无论哪个区域们都存放的是对象实例。进一步划分只是为了更好回收，或者更快分配内存
  - 根据java虚拟机规范规定，java堆可以处于物理上不连续的内存空间，只要逻辑上是连续的即可。
  - 如果堆中没有内存完成实例分配，会抛出OutOfMemoryError。
  - [[1\]](https://www.neat-reader.cn/webapp#filepos158689)Java虚拟机规范中的原文：The heap is the runtime data area from which memory for all class instances and arrays is allocated。

- 方法区

  - 各个线程共享的内存区域
  - 用于存储被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。
  - java虚拟机规范把方法区描述为堆的一个逻辑部分，但有个别名Non_heap，目的是与堆区分开
  - 现在也有放弃永久代并逐步改为采用Native Memory来实现方法区的规划了[[1\]](https://www.neat-reader.cn/webapp#filepos163984)，在目前已经发布的JDK 1.7的HotSpot中，已经把原本放在永久代的字符串常量池移出。
  - HotSpot 在 JDK 1.7 以前使用“永久区”（或者叫 Perm 区）来实现方法区，在 JDK 1.8 之后“永久区”就已经被移除了，取而代之的是一个叫作“元空间（metaspace）”的实现方式
  - 不需要连续的内存、可以固定 亦可以扩展、还可选择不实现垃圾收集。
  - 这区域的内存回收目标主要是针对常量池的回收和对类型的卸载。
  - 不回收 也会存在内存泄漏问题
  - 根据Java虚拟机规范的规定，当方法区无法满足内存分配需求时，将抛出OutOfMemoryError异常。

- 运行时常量池

  - 是方法区的一部分
  - class  文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译器生成的各种字面量和符号引用，这部分将在类加载后进入方法区的运行时常量池中存放。
  - java虚拟机堆Class文件每一部分（自然也包括常量池）的格式都有严格规定、每一个字节用于存储哪种数据都必须符合规范上的要求才会被虚拟机认可，装载和执行、但对于运行时常量池，java虚拟机规范没有做任何细节描写。
  - 一般来说除了保存Class文件中描述的符号引用外，还会把翻译出来的直接引用也存储再运行时常量池
  - 并非预置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被开发人员利用得比较多的便是String类的intern（）方法。
  - OutOfMemoryError异常。

- 直接内存

  - 可能OutOfMemoryError异常。
  - 在JDK 1.4中新加入了NIO（New Input/Output）类，引入了一种基于通道（Channel）与缓冲区（Buffer）的I/O方式，它可以使用Native函数库直接分配堆外内存，然后通过一个存储在Java堆中的DirectByteBuffer对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在Java堆和Native堆中来回复制数据。
  - 不会受到java堆大小的限制。但作为内存，还是会受到本机总内存（包括RAM以及SWAP区或者分页文件）大小以及处理器寻址空间的限制。
  - 务器管理员在配置虚拟机参数时，会根据实际内存设置-Xmx等参数信息，但经常忽略直接内存，使得各个内存区域总和大于物理内存限制（包括物理的和操作系统级的限制），从而导致动态扩展时出现OutOfMemoryError异常。

- HotSpot虚拟机对象探秘

  - 基于实用优先的原则

- 对象创建

  - 通常一个关键字new

  - 虚拟机遇到一条new指令时，首先将去检查这个指令的参数是否能够再常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否已被加载、解析和初始化，如果没有，那就先必须执行相应的类加载过程

  - 检查完成后，接下来虚拟机将为新生对象分配内存，对象所需内存大小在类加载完成后就确定了

  - 所分配内存就仅仅是把那个指针向空闲空间那边挪动一段与对象大小相等的距离，这种分配方式称为“指针碰撞”（Bump the Pointer）

  - 内存防止重复分配方法：

    - 一种是对分配内存空间的动作进行同步处理——实际上虚拟机采用CAS配上失败重试的方式保证更新操作的原子性；
    - 另一种是把内存分配的动作按照线程划分在不同的空间之中进行，即每个线程在Java堆中预先分配一小块内存，称为本地线程分配缓冲（Thread Local Allocation Buffer,TLAB）

  - 内存分配完成后，都会初始化为零值，不包括对象头。

  - 接着设置对象（实例，元数据，hashcode、gc）这些信息都在对象头

  - 上面是创建，接下来init真正初始化，赋值操作

    ```
    //确保常量池中存放的是已解释的类
    
    if（！constants-＞tag_at（index）.is_unresolved_klass（））{
    
    //断言确保是klassOop和instanceKlassOop（这部分下一节介绍）
    
    oop entry=（klassOop）*constants-＞obj_at_addr（index）；
    
    assert（entry-＞is_klass（），"Should be resolved klass"）；
    
    klassOop k_entry=（klassOop）entry；
    
    assert（k_entry-＞klass_part（）-＞oop_is_instance（），"Should be instanceKlass"）；
    
    instanceKlass * ik=（instanceKlass*）k_entry-＞klass_part（）；
    
    //确保对象所属类型已经经过初始化阶段
    
    if（ik-＞is_initialized（）＆＆ik-＞can_be_fastpath_allocated（））
    
    {
    
    //取对象长度
    
    size_t obj_size=ik-＞size_helper（）；
    
    oop result=NULL；
    
    //记录是否需要将对象所有字段置零值
    
    bool need_zero=！ZeroTLAB；
    
    //是否在TLAB中分配对象
    
    if（UseTLAB）{
    
    result=（oop）THREAD-＞tlab（）.allocate（obj_size）；
    
    }
    
    if（result==NULL）{
    
    need_zero=true；
    
    //直接在eden中分配对象
    
    retry：
    
    HeapWord * compare_to=*Universe：heap（）-＞top_addr（）；
    
    HeapWord * new_top=compare_to+obj_size；
    
    /*cmpxchg是x86中的CAS指令，这里是一个C++方法，通过CAS方式分配空间，如果并发失败，
    
    转到retry中重试，直至成功分配为止*/
    
    if（new_top＜=*Universe：heap（）-＞end_addr（））{
    
    if（Atomic：cmpxchg_ptr（new_top,Universe：heap（）-＞top_addr（），compare_to）！=compare_to）{
    
    goto retry；
    
    }
    
    result=（oop）compare_to；
    
    }
    
    }
    
    if（result！=NULL）{
    
    //如果需要，则为对象初始化零值
    
    if（need_zero）{
    
    HeapWord * to_zero=（HeapWord*）result+sizeof（oopDesc）/oopSize；
    
    obj_size-=sizeof（oopDesc）/oopSize；
    
    if（obj_size＞0）{
    
    memset（to_zero，0，obj_size * HeapWordSize）；
    
    }
    
    }
    
    //根据是否启用偏向锁来设置对象头信息
    
    if（UseBiasedLocking）{
    
    result-＞set_mark（ik-＞prototype_header（））；
    
    }else{
    
    result-＞set_mark（markOopDesc：prototype（））；
    
    }
    
    result-＞set_klass_gap（0）；
    
    result-＞set_klass（k_entry）；
    
    //将对象引用入栈，继续执行下一条指令
    
    SET_STACK_OBJECT（result，0）；
    
    UPDATE_PC_AND_TOS_AND_CONTINUE（3，1）；
    
    }
    
    }
    
    }
    ```

- 对象内存布局

  - 对象头、
    - ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\duixiang.png)
  - 示例数据、
  - 对齐填充
    - 就是对象的大小必须是8字节的整数倍。而对象头部分正好是8字节的倍数（1倍或者2倍）

- 对象访问定位

  - 指针

  - 句柄

  - 句柄来访问的最大好处就是reference中存储的是稳定的句柄地址，在对象被移动（垃圾收集时移动对象是非常普遍的行为）时只会改变句柄中的实例数据指针，而reference本身不需要修改。

    使用直接指针访问方式的最大好处就是速度更快，它节省了一次指针定位的时间开销，由于对象的访问在Java中非常频繁，因此这类开销积少成多后也是一项非常可观的执行成本。就本书讨论的主要虚拟机Sun HotSpot而言，它是使用第二种方式进行对象访问的

- **实战：OutOfMemoryError异常**

  - Java堆溢出

    - ```
      /**
       * VM Args：-Xms20m
       *  -Xmx20m-XX：+HeapDumpOnOutOfMemoryError
       *
       * @author zzm
       */
      
      public class HeapOOM {
      
          static class OOMObject {
      
          }
      
          public static void main(String[] args) {
      
              List<OOMObject> list = new ArrayList<OOMObject>();
      
              while (true) {
      
                  list.add(new OOMObject());
      
              }
      
          }
      
      }
      ```

      ```
      java.lang.OutOfMemoryError：Java heap space
      
      Dumping heap to java_pid3404.hprof……
      
      Heap dump file created[22045981 bytes in 0.663 secs]
      ```

      

    - Eclipse Memory Analyzer分析

    - 如果是内存泄露，可进一步通过工具查看泄露对象到GC Roots的引用链。于是就能找到泄露对象是通过怎样的路径与GC Roots相关联并导致垃圾收集器无法自动回收它们的。掌握了泄露对象的类型信息及GC Roots引用链的信息，就可以比较准确地定位出泄露代码的位置。

    - 如果不存在泄露，换句话说，就是内存中的对象确实都还必须存活着，那就应当检查虚拟机的堆参数（-Xmx与-Xms），与机器物理内存对比看是否还可以调大，从代码上检查是否存在某些对象生命周期过长、持有状态时间过长的情况，尝试减少程序运行期的内存消耗。

  - 虚拟机栈和本地方法栈溢出

    - 由于在HotSpot虚拟机中并不区分虚拟机栈和本地方法栈，因此，对于HotSpot来说，虽然-Xoss参数（设置本地方法栈大小）存在，但实际上是无效的，栈容量只由-Xss参数设定。关于虚拟机栈和本地方法栈，在Java虚拟机规范中描述了两种异常：
      - 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常
      - 如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常

> thread.join的含义是当前线程需要等待previousThread线程终止之后才从thread.join返回。简单来说，就是线程没有执行完之前，会一直阻塞在join方法处。

------

------

# GC

清理需求：

> - Allocation Failure：在堆内存中分配时，如果因为可用剩余空间不足导致对象内存分配失败，这时系统会触发一次 GC。
>
> - System.gc()：在应用层，Java 开发工程师可以主动调用此 API 来请求一次 GC。

被清理的对象：

> 在 Java 中，有以下几种对象可以作为 GC Root：
>
> 1. Java 虚拟机栈（局部变量表）中的引用的对象。
> 2. 方法区中静态引用指向的对象。
> 3. 仍处于存活状态中的线程对象。
> 4. Native 方法中 JNI 引用的对象。

1. Java 虚拟机栈（局部变量表）中的引用的对象。
2. 方法区中静态引用指向的对象。
3. 仍处于存活状态中的线程对象。
4. Native 方法中 JNI 引用的对象。

# GC回收

# JVM分代回收策略

Java 虚拟机根据对象存活的周期不同，把堆内存划分为几块，一般分为新生代、老年代，这就是 JVM 的内存分代策略。注意: 在 HotSpot 中除了新生代和老年代，还有永久代。



分代回收的中心思想就是：对于新创建的对象会在新生代中分配内存，此区域的对象生命周期一般较短。如果经过多次回收仍然存活下来，则将它们转移到老年代中。

### 年轻代（Young Generation）

新生成的对象优先存放在新生代中，新生代对象朝生夕死，存活率很低，在新生代中，常规应用进行一次垃圾收集一般可以回收 70%~95% 的空间，回收效率很高。新生代中因为要进行一些复制操作，所以一般采用的 GC 回收算法是复制算法。



新生代又可以继续细分为 3 部分：Eden、Survivor0（简称 S0）、Survivor1（简称S1）。这 3 部分按照 8:1:1 的比例来划分新生代。这 3 块区域的内存分配过程如下：



绝大多数刚刚被创建的对象会存放在 Eden区。如图所示：





![img](https://app.yinxiang.com/shard/s28/nl/23628363/9d252eff-7003-4a09-9c50-ed27320ec1ad/res/5c2a813b-ad90-4849-9c29-8b97c5344dff/Ciqah158kSCACECoAABYMXWFYtY758.png?resizeSmall&width=832)





当 Eden 区第一次满的时候，会进行垃圾回收。首先将 Eden 区的垃圾对象回收清除，并将存活的对象复制到 S0，此时 S1 是空的。如图所示：





![img](https://app.yinxiang.com/shard/s28/nl/23628363/9d252eff-7003-4a09-9c50-ed27320ec1ad/res/f463cfa1-156e-4865-870d-9d69d9187384/Cgq2xl58kSGACeimAABTArM3xYk676.png?resizeSmall&width=832)





下一次 Eden 区满时，再执行一次垃圾回收。此次会将 Eden 和 S0 区中所有垃圾对象清除，并将存活对象复制到 S1，此时 S0 变为空。如图所示：





![img](https://app.yinxiang.com/shard/s28/nl/23628363/9d252eff-7003-4a09-9c50-ed27320ec1ad/res/5c3fd3d3-518c-46fb-95b1-ed3ef1f53cf2/Ciqah158kSGAXW6uAABTZbbBBQU172.png?resizeSmall&width=832)





如此反复在 S0 和 S1之间切换几次（默认 15 次）之后，如果还有存活对象。说明这些对象的生命周期较长，则将它们转移到老年代中。如图所示：





![img](https://app.yinxiang.com/shard/s28/nl/23628363/9d252eff-7003-4a09-9c50-ed27320ec1ad/res/12154243-95c0-4a00-bdf5-c5f0f76b50f4/Cgq2xl58kSGAIFsJAABiXFUZ3JU251.png?resizeSmall&width=832)

------



### 年老代（Old Generation）

一个对象如果在新生代存活了足够长的时间而没有被清理掉，则会被复制到老年代。老年代的内存大小一般比新生代大，能存放更多的对象。如果对象比较大（比如长字符串或者大数组），并且新生代的剩余空间不足，则这个大对象会直接被分配到老年代上。



我们可以使用 -XX:PretenureSizeThreshold 来控制直接升入老年代的对象大小，大于这个值的对象会直接分配在老年代上。老年代因为对象的生明周期较长，不需要过多的复制操作，所以一般采用标记压缩的回收算法。

> 注意：对于老年代可能存在这么一种情况，老年代中的对象有时候会引用到新生代对象。这时如果要执行新生代 GC，则可能需要查询整个老年代上可能存在引用新生代的情况，这显然是低效的。所以，老年代中维护了一个 512 byte 的 card table，所有老年代对象引用新生代对象的信息都记录在这里。每当新生代发生 GC 时，只需要检查这个 card table 即可，大大提高了性能。 

###### GC Log 分析

为了让上层应用开发人员更加方便的调试 Java 程序，JVM 提供了相应的 GC 日志。在 GC 执行垃圾回收事件的过程中，会有各种相应的 log 被打印出来。其中新生代和老年代所打印的日志是有区别的。

- 新生代 GC：这一区域的 GC 叫作 Minor GC。因为 Java 对象大多都具备朝生夕灭的特性，所以 Minor GC 非常频繁，一般回收速度也比较快。
- 老年代 GC：发生在这一区域的 GC 也叫作 Major GC 或者 Full GC。当出现了 Major GC，经常会伴随至少一次的 Minor GC。

> 注意：在有些虚拟机实现中，Major GC 和 Full GC 还是有一些区别的。Major GC 只是代表回收老年代的内存，而 Full GC 则代表回收整个堆中的内存，也就是新生代 + 老年代。 



###### 再谈引用

上文中已经介绍过，判断对象是否存活我们是通过GC Roots的引用可达性来判断的。但是JVM中的引用关系并不止一种，而是有四种，根据引用强度的由强到弱，他们分别是:强引用(Strong Reference)、软引用(Soft Reference)、弱引用(Weak Reference)、虚引用(Phantom Reference)。



任何一本Java面试书籍都会对这四种引用做简单对比，我用一张表格来表示如下：

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\refference.png)



#### SoftReference

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\GCoverHead.jpg)

- 优化

  ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\GCoverHeadBetter.jpg)



# class loader

> 常量在编译阶段会存入到调用`这个常量的方法所在类的常量池`，本质上调用类并没有直接引用到
>
> 定义常量的类，因此不会触发定义常量的类的初始化
>
> ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\finalStatic.png)
>
> ldc 表示将int string 类型的常量从常量池推送到栈顶。
>
> bipush 将单字节（-128~127）常量值推送至栈顶。
>
> sipush 将短整新常量（-32768~32767）推送至栈顶 
>
> iconst表示将int 类型1推送至栈顶
>
> 不确定值会影响类的初始化
>
> 对于数组实例来说 其类型是由jvm在运行期间动态生成 表示为[L+class]
>
> 这种形式 动态生成的类型，父类型是object
>
> anewarray 引用的数组
>
> newarray  原始类型的数组并压入栈顶
>
> **`当一个接口在初始化时 并不要求其父接口都完成初始化，但是运行期确定值时，需要对其父类初始化`**
>
> 一个接口在初始化时，会要求其父接口完成初始化
>
> 在初始化一个类时，并不会先初始化他实现的接口
>
> 在初始化一个接口时，并不会初始化他的父接口
>
> 只有程序首次使用特定接口的静态变量时才会导致该接口初始化。



















