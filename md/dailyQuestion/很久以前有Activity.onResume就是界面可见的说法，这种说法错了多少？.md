# 很久以前有Activity.onResume就是界面可见的说法，这种说法错了多少？

>  
>
> 问题是：
>
> 1. Activity.onCreate 和 Activity.onResume，在调用时间上有差别么？可以从Message调度去考虑。
> 2. 有没有一个合理的时机，让我们认为Activity 界面可见了？

-----

>仅从activity角度来看，oncreate oostart oonresume 都是message里面的逻辑，具体可以看ActivityThread里面的 HandleLaunchActivity 方法
>
>```
>private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent, String reason) {
> ...
> Activity a = performLaunchActivity(r, customIntent); // 这里最终回调到Activity的performCreate、performStart
> if (a != null) {
>         r.createdConfig = new Configuration(mConfiguration);
>         reportSizeConfigurations(r);
>         Bundle oldState = r.state;
>         // 这里最终回调到Activity的performResume
>         handleResumeActivity(r.token, false, r.isForward,
>                 !r.activity.mFinished && !r.startsNotResumed, r.lastProcessedSeq, reason);
> ...
>}
>```
>
>但是仅从应用开发角度来看，选择 `onWindowfocusChanged` 的首次回调是一个比较精准的可见时机
>
>整个activity启动大致回调流程为
>
>onCreate - onStart - onResume - measure - layout -measure - layout - draw - onWindowFocusChanged
>
>详细的说
>
>1. 在onCreate阶段通过setContentView，布局被包装为PhoneWindow内部的DecorView对象
>2. 在ActivityThread.handleResumeActivity阶段，执行完performResume后通过WindowManagerImpl的addView，DecorView被 setView 到 ViewRootImpl
>3. ViewRootImpl.setView 方法里通过 requestLayout - scheduleTraversals 向 Choreographer 请求安排绘制任务
>4. Choreographer收到VSYNC信号回调到ViewRootImpl的performTraversals对DecorView进行measure、layout、draw，其中由于首次performTraversals会涉及到初始化EGL，所以最终会执行两次该方法，因此decorView会经历：measure - layout - measure - layout draw
>
>至于 onWindowFocusChanged，得从第3步说起，在setView方法中，紧接在 requestLayout 之后的逻辑就是向WMS通过binder发起添加window的过程，WMS完成操作后会把windowFocusChanged的事件回调给应用进程，ViewRootImpl在把该事件分发给DecorView，而DecorView重载了View的 onWindowFocusChanged 方法，内部最终将消息通过接口回传给了Activity的onWindowFocusChanged。

 

