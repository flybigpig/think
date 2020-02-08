# zygote

中文名为`孵化器`

在 Android 系统中, DVM (Dalvik 虚拟机)和 ART 、 应用程序进程以及运行系统的关
键服务的 SystemServer 进程都是由 Zygote 进程来创建的,我们也将它称为孵化器。它通过
fock ( 复 制进程)的形式来创建应用程序进程和 SystemServer 进程,由于 Zygote 进程在启
动时会创建 DVM 或者 ART ,因此通过 fock 而创建的应用程序进程和 System Server 进程可
以在 内部获取一个 DVM 或者 ART 的 实例副本。



```
AMS 在启动应用程序时会检查这个应用程序需要的应用程序进程是否存在,不存在就会请
求 Zygote 进程启动需要的应用程序进程。在 2.2 节中,我们知道在 Zygote 的 Java 框架层
中会创建一个 Server 端的 Socket ,这个 Socket 用来等待 AMS 请求 Zygote 来创建新的应用
程序进程。 Zygote 进程通过 fock 自身创建应用程序进程,这样应用程序进程就会获得 Zygote
进程在启动时创建的虚拟机实例。当然,在应用程序进程创建过程中除了获取虚拟机实例
外,还创建了 Binder 线程地和 消息循环
```

![image-20200208140028193](/home/fly/.config/Typora/typora-user-images/image-20200208140028193.png)