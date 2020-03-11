# 深入 JVM



- JDK  Java Development

  - java程序设计语言
  - java 虚拟机
  - JavaApi

- Class

-  ![image-20200311111852732](C:\Users\czdxn\AppData\Roaming\Typora\typora-user-images\image-20200311111852732.png)

- 程序计数器

  - 一块较小的内存空间
  - 可以看成是当前线程执行字节码的行号指示器
  - 字节码指示器工作时就是通过改变这个计数器的值来取下一条执行的字节码指令（分支、循环、跳转、异常处理、线程恢复等）
  -  java function---计数器记录的是正在执行的虚拟机字节码指令地址
  - native function-- 计数器值为空（Undefined）
  - 此内存区域是唯一一个在java虚拟机规范中没有规定任何OutOfMemoryError情况的区域

- Java 虚拟机栈

  - 与程序计数器一样，java虚拟栈（JavaStacks）也是线程私有的
  - 生命周期和线程相同
  - 虚拟机栈描述的是java 方法执行的内存模型：每个方法都在执行的同时都会创建一个栈帧 用于存储局部变量、操作数栈、动态链接、方法出口等信息。
  - 粗略可以划分为 堆（Heap）内存和栈(Stack)内存
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