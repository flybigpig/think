# [[Android   ] 谈一下自定义View的流程](https://www.cnblogs.com/merbn/p/11285721.html)




可以从：

1. 自定义`View`的步骤；
2. 2.自定义`View`的注意事项;
3. 3.自定义`ViewGroup`的步骤以及注意事项;
4. 4.一些特殊需要注意的地方;
   以上几方面进行。

##  绘制流程

要想充分理解自定义View的流程，就必须对`View`的绘制流程有深刻理解，下面说几点：

   - **DecorView被加载到Window中**
        - 从`Activity`的`startActivity`开始，最终调用到`ActivityThread`的`handleLaunchActivity`方法来创建`Activity`，首先，会调用`performLaunchActivity`方法,内部会执行`Activity`的`onCreate`方法，从而完成`DecorView`和`Activity`的创建。然后，会调用`handleResumeActivity`，里面首先会调用`performLaunchActivity`去执行`Activity`的`onResume()`方法，执行完成后悔得到一个`ActivityClientRecord`对象，然后通过`r.window.getDecorView()`的方式得到`DecorView`，然后会通过`a.getWIndowManager()`得到`WindowManager`，最终调用其`addView()`方法将`DecorView`加进去。
        - `WindowManager`的实现类是`WindowManagerImpl`，它内部会将`addView`的逻辑委托给`WindowManagerGlobal`，可见这里使用了接口隔离和委托模式将实现和抽象充分解耦。在`WindowManagerGlobal`的`addView()`方法中不仅会将`DecorView`添加到`Window`中，同时会创建`ViewRootImpl`对象，并将`ViewRootImpl`对象和`DecorView`通过`root.setView()`把`DecorView`加载到`Window`中，这里的`ViewRootImpl`是`ViewRoot`的实现类，是连接`WindowManager`和`DecorView`的纽带。`View`的三大流程均是通过`ViewRoot`来完成的。
   -  **了解绘制的整体流程**
        - 绘制会从根视图`ViewRoot`的`performTraversals()`方法开始，从上到下遍历整个视图树，每个View控件负责绘制自己，而`ViewGroup`还需要负责通知自己的子View进行绘制操作。
        - 前面讲到了将 DecorView 加载到 Window 中，是通过 ViewRootImpl 的 setView 方法。ViewRootImpl还有一个方法PerformTraveals，这里面主要执行了3个方法，分别是performMeasure、performLayout和performDraw，在其方法的内部又会分别调用View的measure、layout和draw方法。需要注意的是，performMeasure方法中需要传入两个参数，分别是 childWidthMeasureSpec 和 childHeightMeasureSpec。
   - **理解MeasureSpec**
        `MeasureSpec`表示的是一个32位的整形值，它的高2位表示测量模式`SpecMode`，低30位表示某种测量模式下的规格大小`SpecSize`。`MeasureSpec`是View类的一个静态内部类，它来说明应该如何测量这个`View`。它有三种测量模式，如下
        1.`EXACTLY`：精确测量模式，视图宽高指定为`match_parent`或具体数值时生效，表示父视图已经决定了子视图的精确大小，这种模式下`View`的测量值就是`SpecSize`的值。
        2.`AT_MOST`：最大值测量模式，当视图的宽高指定为`wrap_parent`时生效， 此时子视图的尺寸可以是不超过父视图允许的最大尺寸的任何尺寸。
        3.`UNSPECIFIED`：不指定测量模式，父视图没有限制子视图的大小，子视图可以是想要的任何尺寸，通常用于系统内部，应用开发中很少用到。
        `MeasureSpec`通过将`SpecMode`和`SpecSize`打包成一个int值来避免过多的对象内存分配，为了方便操作，其提供了打包和解包的方法， 打包方法为`makeMeasureSpec`，解包方法为`getMode和getSize`。
        普通`View`的`measureSpce`的创建规则如下：
        ![image](C:\Users\czdxn\Desktop\md\think\think\md\pic\measure.png)
        对于`DecorView`而言，它的measureSpec由`窗口尺寸和其自身的LayoutParams`共同决定；对于普通的View，它的MeasureSpec由`父视图的MeasureSpec和其自身的LayoutParams`共同决定。

   **View绘制流程之Measure**

   - 首先，在`ViewGroup`中的`measureChildren()`方法中会遍历测量`ViewGroup`中所有的`View`，当`View`的可见性处于`GONE`状态时，不对其进行测量。
   - 然后，测量某个指定的`View`时，根据父容器的`MeasureSpec`和子`View`的`LayoutParams`登信息计算子`View`的MeasureSpec。
   - 最后，将计算出的`MeasureSpec`传入View的`measure`方法，这里ViewGroup没有定义测量的具体过程，因为ViewGroup是一个抽象类，其测量过程中的`onMeasure`方法需要各个子类去实现，不同的VIewGroup子类有不同的布局特性，这导致它们的测量细节各不相同，如果需要自定义测量过程，则子类可以重写这个方法，（`setMeasureDimension`方法用于设置View的测量宽高，如果View没有重写`onMeasure`方法，则会默认调用`getDefaultSize`来获得View的宽高）
                **getSuggestMinimumWidth分析**
如果View没有设置背景，那么返回`android:minWidth`这个属性所指定的值，这个值可以为0；如果View设置了背景，则返回`android:minWidth`和背景的最小宽度这两者中的最大值。
                **自定义View时手动处理wrap_content时的情形**
直接继承View的控件需要重写`onMeasure`方法并设置`wrap_content`时的自身大小，否则在布局中使用`wrap_content`就相当于使用`match_parent`，此时，可以在wrap_content的情况下(对应MeasureSpec.AT_MOST)指定内部宽/高（mWidth和mHeight）。
                **LinearLayout的onMeasure方法实现解析(这里仅分析measureVertical核心源码)**
系统会遍历子元素并对每个子元素执行`measureChildBeforeLayout`方法，这个方法内部会调用子元素的`measure`方法，这样各个子元素就开始依次进入`measure`过程，并且系统会通过mTotalLength这个变量来存储LinearLayout在竖直方向的初步高度。每测量一个子元素，`mTotalLength`就会增加，增加的部分主要包括了子元素的高度以及子元素在竖直方向上的margin等。
   - **在Activity中获取某个View的宽高**
               由于View的measure过程和Activity的生命周期方法不是同步执行的，如果View还没有测量完毕，那么获得的宽/高就是0，所以在`onCreate`、`onStart`、`onResume`中均无法正确得到某个View的宽高信息，解决方式如下：
   - `Activity/View   onWindowFocusChanged`：此时View已经初始化完毕，当Activity的窗口得到焦点和失去焦点时均会被调用一次，如果频繁地进行`onResume和onPause`，那么`onWindowFocusChange`也会频繁地调用。
   - `view.post(runable)`：通过post可以将一个runable投递到消息队列的尾部，初始化好了然后等待`Looper`调用此`runable`的时候，View也已经初始化好了。
   - `ViewTreeObserver   addOnGlobalLayoutListener`：当View树的状态发生改变或者View树内部的View的可见性发生改变时，`onGlobalLayout`方法将被回调。
   - `View.measure(int widthMeasureSpec，int heightMeasureSpec)`：`match_parent`时不知道`parentSize`的大小，测不出具体数值时，直接`makeMeasureSpec`固定值，然后调用`view.measure`就可以了；`wrap_content`时，在最大化模式下，用View理论上能支持的最大值去构造`MeasureSpec`是合理的。
                **View的绘制流程之Layout**
首先，会通过setFrame方法来设定View的四个顶点的位置，即View在父容器中的位置，然后回执行onLayout空方法，子类如果是ViewGroup类型，则重写这个方法，实现VIewGroup中所有View控件布局流程。
                **LinearLayout的onLayout方法实现解析（layoutVertical核心代码）**
其中会遍历调用每个子View的`setChildFrame`方法为子元素确定对应的位置。其中的`childTop`会逐渐增大，意味着后面的子元素会被放置在靠下的位置。
注意：在View的默认实现中，View的测量宽/高和最终宽/高是相等的，只不过测量宽/高形成于View的`measure`过程，而最终宽/高形成于View的`layout`过程，即两者的赋值时机不同，测量宽/高的赋值时机稍早一些，在一些特殊情况下则两者不相等：
   - 重写View的`layout`方法，使最终宽度总比测量宽/高大100px。
   - View需要多次`measure`才能确定自己的测量宽/高，在前几次测量的过程中，其得出的测量宽/高可能和最终宽/高不一致，但最终来说，测量宽/高还是和最终宽/高相同。
                View的绘制流程之Draw
                   Draw的基本流程
绘制基本上可以分为六个步骤：
   - 实现绘制View的背景；
   - 如果需要的话，保存canvas的图层，为`fading`做准备；
   - 然后，绘制View的内容；
   - 接着，绘制View的子View；
   - 如果需要的话，绘制View的`fading`边缘并回复图层；
   - 最后，绘制View的装饰(例如滚动条等等)；
                   setWillNotDraw的作用
如果一个View不需要绘制任何内容，那么设置这个标记位位true以后，系统会进行相应的优化。
   - 默认情况下，View没有启用这个优化标记位，但是ViewGroup会默认启用这个优化标记位。
   - 当我们的自定义控件继承于ViewGroup并且本身不具备绘制功能时，就可以开启这个标记位从而便于系统进行后续的优化。
   - 当明确知道一个ViewGroup需要通过onDraw来绘制内容时，我们需要显示地关闭WILL_NOT_DRAW这个标记位。
                RequestLayout、onLayout、onDraw、onDrawChild区别与联系?
   + `requestLayout()`方法：会导致调用`measure()`过程和`layout()`过程，将会根据标记位判断是否`onDraw`
   + `onLayout()`方法：如果该View是`ViewGroup`对象，需要实现该方法，对每个子视图进行布局。
   + `onDraw()`方法：绘制视图本身（每个View都需要重载该方法，`ViewGroup`不需要实现该方法）
   + `drawChild()`：去重新回调每个子视图的draw()方法。
                invalidate()和postInvalidate()的区别?
`invalidate()`与`postInvalidate()`都用于刷新View，主要区别是`invalidate()`在主线程中调用，若在子线程中使用需要配合`handler`；而`postInvalidate()`可在子线程中直接调用。
[更详细内容查看这里](https://jsonchao.github.io/2018/10/28/Android%20View%E7%9A%84%E7%BB%98%E5%88%B6%E6%B5%81%E7%A8%8B/)
   > Answer2：
                大多数自定义View要么在`onDraw`方法中画点东西，和在`onTOuchEvent`中处理触摸事件。
                      自定义View的步骤：
   + `onMeasure`，可以不重写，不重写的话就要在外面指定宽高，建议重写；
   + `onDraw`，看情况重写，如果需要画东西就要重写；
   + `onTouchEvent`，也是看情况，如果要做能跟手指交互的View，就重写。
                      自定义View注意事项：
   + 如果有自定义布局属性的，在构造方法中取得属性后应及时调用recycle方法回收资源；
   + 某些比较重量级的资源，可以重写`onDetachedFormWindow`方法， 并在此方法中释放；
   + `onDraw`和`onTouchEvent`方法中都应尽量避免创建对象，过多操作可能会造成卡顿；
                如果现有ViewGroup的排版或者行为不满足当前需求，就可以自定义`ViewGroup`。
                      自定义ViewGroup的步骤：
   + `onMeasure`(必须)，在这里测量每一个子View，还有处理自己的尺寸；
   + `onLayout`(必须)，在这里对子View进行布局；
   + 如果有自己的触摸事件，需要重写`onInterceptTouchEvent`或`onTouchEvent`；
                自定义ViewGroup注意事项：
   + 如果想在`ViewGroup`中画点东西，又没有在布局中设置`background`的话，会画不出来，这时候需要调用`setWillNotDraw`方法，并设置为false；
   + 如果有自定义布局属性的，在构造方法中取得属性后应及时调用recycle方法回收资源；
   + 如果有自己的触摸事件，又不影响View的行为，需要重写`onInterceptTouchEvent`并在里面去判断哪些行为是自己需要的，哪些是不需要的。
   + 某些比较重量级的资源，可以重写`onDetachedFromWindow`方法，并在此方法中释放；