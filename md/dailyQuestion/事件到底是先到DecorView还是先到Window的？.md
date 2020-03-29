# 事件到底是先到DecorView还是先到Window的？

> # 事件先到DecorView
>
> ## Input系统
>
> 当用户触摸屏幕或者按键操作，首次触发的是硬件驱动，驱动收到事件后，将该相应事件写入到输入设备节点，这便产生了最原生态的内核事件。接着，输入系统取出原生态的事件，经过层层封装后成为KeyEvent或者MotionEvent ；最后，交付给相应的目标窗口(Window)来消费该输入事件。
>
> 1、当屏幕被触摸，Linux内核会将硬件产生的触摸事件包装为Event存到/dev/input/event[x]目录下。
>
> 2、Input系统—InputReader线程：loop起来让EventHub调用getEvent()不断的从/dev/input/文件夹下读取输入事件。然后转换成EventEntry事件加入到InputDispatcher的mInboundQueue。
>
> 3、Input系统—InputDispatcher线程：从mInboundQueue队列取出事件，转换成DispatchEntry事件加入到connection的outboundQueue队列。再然后开始处理分发事件 **（比如分发到ViewRootImpl的WindowInputEventReceiver中）**，取出outbound队列，放入waitQueue.
>
> 4、Input系统—UI线程：创建socket pair，分别位于”InputDispatcher”线程和focused窗口所在进程的UI主线程，可相互通信。
>
> 这里只说大概，详情请看gityuan的这篇文章[Input系统—事件处理全过程](http://gityuan.com/2016/12/31/input-ipc/)，文章3.3.3小节讲的是input系统事件从Native层分发Framework层的InputEventReceiver.dispachInputEvent()。
>
> ## Framework层
>
> ```
> //InputEventReceiver.dispachInputEvent()
> private void dispatchInputEvent(int seq, InputEvent event) {
>     mSeqMap.put(event.getSequenceNumber(), seq);
>     onInputEvent(event); 
> }
> ```
>
> ### ViewRootImpl.WindowInputEventReceiver
>
> Native层通过JNI执行Framework层的InputEventReceiver.dispachInputEvent()，而真正调用的是继承了InputEventReceiver的ViewRootImpl.WindowInputEventReceiver。所以这里执行的WindowInputEventReceiver的dispachInputEvent()：
>
> ```
> final class WindowInputEventReceiver extends InputEventReceiver {
>     public void onInputEvent(InputEvent event) {
>        enqueueInputEvent(event, this, 0, true);
>     }
>     ...
> }
> ```
>
> ### ViewRootImpl
>
> ```
>     void enqueueInputEvent(InputEvent event,
>             InputEventReceiver receiver, int flags, boolean processImmediately) {
>         ...
>         if (processImmediately) {
>             //关键点：执行Input事件
>             doProcessInputEvents();
>         } else {
>             //走一遍Handler延迟处理事件
>             scheduleProcessInputEvents();
>         }
>     }
> 
>     void doProcessInputEvents() {
>         while (mPendingInputEventHead != null) {
>             QueuedInputEvent q = mPendingInputEventHead;
>             mPendingInputEventHead = q.mNext;
>             if (mPendingInputEventHead == null) {
>                 mPendingInputEventTail = null;
>             }
>             q.mNext = null;
> 
>             mPendingInputEventCount -= 1;
>             Trace.traceCounter(Trace.TRACE_TAG_INPUT, mPendingInputEventQueueLengthCounterName,
>                     mPendingInputEventCount);
> 
>             long eventTime = q.mEvent.getEventTimeNano();
>             long oldestEventTime = eventTime;
>             if (q.mEvent instanceof MotionEvent) {
>                 MotionEvent me = (MotionEvent)q.mEvent;
>                 if (me.getHistorySize() > 0) {
>                     oldestEventTime = me.getHistoricalEventTimeNano(0);
>                 }
>             }
>             mChoreographer.mFrameInfo.updateInputEventTime(eventTime, oldestEventTime);
>             //关键点：进一步派发事件处理
>             deliverInputEvent(q);
>         }
>         ...
>     }
> 
>     private void deliverInputEvent(QueuedInputEvent q) {
>         Trace.asyncTraceBegin(Trace.TRACE_TAG_VIEW, "deliverInputEvent",
>                 q.mEvent.getSequenceNumber());
>         if (mInputEventConsistencyVerifier != null) {
>             mInputEventConsistencyVerifier.onInputEvent(q.mEvent, 0);
>         }
> 
>         InputStage stage;
>         if (q.shouldSendToSynthesizer()) {
>             stage = mSyntheticInputStage;
>         } else {
>             stage = q.shouldSkipIme() ? mFirstPostImeInputStage : mFirstInputStage;
>         }
> 
>         if (stage != null) {
>             //关键点:上面决定将事件派发到那个InputStage中处理
>             stage.deliver(q);
>         } else {
>             finishInputEvent(q);
>         }
>     }
> ```
>
> ### ViewRootImpl.ViewPostImeInputStage
>
> 前面事件会派发到ViewRootImpl.ViewPostImeInputStage中处理，它的父类InputStage.deliver()方法会调用apply()来处理Touch事件：
>
> ```
>         @Override
>         protected int onProcess(QueuedInputEvent q) {
>             if (q.mEvent instanceof KeyEvent) {
>                 return processKeyEvent(q);
>             } else {
>                 final int source = q.mEvent.getSource();
>                 if ((source & InputDevice.SOURCE_CLASS_POINTER) != 0) {
>                     //关键点：执行分发touch事件
>                     return processPointerEvent(q);
>                 } else if ((source & InputDevice.SOURCE_CLASS_TRACKBALL) != 0) {
>                     return processTrackballEvent(q);
>                 } else {
>                     return processGenericMotionEvent(q);
>                 }
>             }
>         }
> 
>         private int processPointerEvent(QueuedInputEvent q) {
>             final MotionEvent event = (MotionEvent)q.mEvent;
>             ...
>             //关键点：mView分发Touch事件，mView就是DecorView
>             boolean handled = mView.dispatchPointerEvent(event);
>             maybeUpdatePointerIcon(event);
>             maybeUpdateTooltip(event);
>             ...
>         }
> ```
>
> ### DecorView
>
> 如果你熟悉安卓的Window，Activity和Dialog对应的ViewRootImpl成员mView就是DecorView，View的dispatchPointerEvent()代码如下：
>
> ```
>     //View.java
>     public final boolean dispatchPointerEvent(MotionEvent event) {
>         if (event.isTouchEvent()) {
>             //分发Touch事件
>             return dispatchTouchEvent(event);
>         } else {
>             return dispatchGenericMotionEvent(event);
>         }
>     }
> ```
>
> 因为DecorView继承FrameLayout，上面所以会调用DecorView的dispatchTouchEvent()：
>
> ```
>    @Override
>     public boolean dispatchTouchEvent(MotionEvent ev) {
>         final Window.Callback cb = mWindow.getCallback();
>         return cb != null && !mWindow.isDestroyed() && mFeatureId < 0
>                 ? cb.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
>     }
> ```
>
> 上面Window.Callback都被Activity和Dialog实现，所以变量cb可能就是Activity和Dialog。
>
> ### Activity
>
> 当上面cb是Activity时，执行Activity的dispatchTouchEvent():
>
> ```
>     public boolean dispatchTouchEvent(MotionEvent ev) {
>         if (ev.getAction() == MotionEvent.ACTION_DOWN) {
>             onUserInteraction();
>         }
>         if (getWindow().superDispatchTouchEvent(ev)) {//关键点：getWindow().superDispatchTouchEvent(ev)
>             return true;
>         }
>         return onTouchEvent(ev);
>     }
> ```
>
> 如果你熟悉安卓的Window，Activity的getWindow()拿到的就是PhoneWindow，下面是PhoneWindow的代码：
>
> ```
>     //PhoneWindow.java
>     @Override
>     public boolean superDispatchTouchEvent(MotionEvent event) {
>         //调用DecorView的superDispatchTouchEvent
>         return mDecor.superDispatchTouchEvent(event);
>     }
> ```
>
> 下面是DecorView.superDispatchTouchEvent()代码:
>
> ```
>     //DecorView.java
>     public boolean superDispatchTouchEvent(MotionEvent event) {
>         //调用ViewGroup的dispatchTouchEvent()开始我们常见的分发Touch事件
>         return super.dispatchTouchEvent(event);
>     }
> ```
>
> ## 流程图
>
> ![从Native层到Framework层Touch事件分发](https://gitee.com/pgm250/blog_img_bed/raw/master/wanan/%E4%BB%8ENative%E5%B1%82%E5%88%B0Framework%E5%B1%82Touch%E4%BA%8B%E4%BB%B6%E5%88%86%E5%8F%91.png)
>
> # 为什么要DecorView -> Activity -> PhoneWindow -> DecorView传递事件
>
> **解耦!** ViewRootImpl并不知道有Activity这种东西存在！它只是持有了DecorView。所以，不能直接把触摸事件送到Activity.dispatchTouchEvent()；那么，既然触摸事件已经到了Activity.dispatchTouchEvent()中了，为什么不直接分发给DecorView，而是要通过PhoneWindow来间接发送呢？因为Activity不知道有DecorView！但是，Activity持有PhoneWindow ，而PhoneWindow当然知道自己的窗口里有些什么了，所以能够把事件派发给DecorView。在Android中，Activity并不知道自己的Window中有些什么，这样耦合性就很低了。我们换一个Window试试？不管Window里面的内容如何，只要Window任然符合Activity制定的标准，那么它就能在Activity中很好的工作。当然，这就是解耦所带来的扩展性的好处。
>
> 回复
>
> Yuan咕咚 : @蔡徐坤打篮球 
>
> 请问一下，为啥先是DecorView，后面又回到DecorView了，此处为啥要这样设计。
>
> 2020-03-01 12:15回复
>
> 蔡徐坤打篮球 : @Yuan咕咚 
>
> 上面已经说了“为什么要DecorView -> Activity -> PhoneWindow -> DecorView传递事件”
>
> 2020-03-01 18:45回复
>
> 若邪 : @蔡徐坤打篮球 
>
> 楼主您好，这样看，Activity持有一个Window对象，ViewRootImpl中持有的是DecorView，DecorView中可以拿到Activity中的window回调； 当事件由硬件传递到 ...查看更多
>
> 2020-03-06 01:05回复
>
> 蔡徐坤打篮球 : @若邪 
>
> 是的
>
> 2020-03-06 08:56回复
>
> 若邪 : @蔡徐坤打篮球 
>
> 还有个疑问，Window中是应该持有一个DecorView，但是在通过WindowManager添加的时候，会生成一个ViewRootImpl，将DecorView放入其中，这个时候Window还算直 ...查看更多
>
> 2020-03-06 09:25回复
>
> 蔡徐坤打篮球 : @若邪 
>
> 每一个Window都对应着一个View和一个ViewRootImpl，Window和View通过ViewRootImpl来建立联系，所以Window还不算直接持有DecorView，建议您看一下《an ...查看更多
>
> 2020-03-07 11:42回复
>
> 250900678@qq.com : @蔡徐坤打篮球 
>
> ViewRootImpl 是知道DecorView 存在的，也就是ViewRootImpl 中的mView，是可以调用super.dispatchTouchEvent的，为什么要再转这么一圈非要传给A ...查看更多