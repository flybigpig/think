# View 旋转动画

```
private void createAnim(View view, boolean isStop) {
    if (view == null) {
        return;
    } else {
        if (mAnimator != null && isStop) {
            mAnimator.pause();
        } else if (mAnimator != null && !isStop) {
            mAnimator.resume();
        } else {
            mAnimator = ObjectAnimator.ofFloat(view, View.ROTATION.getName(), 0, 360);
            view.setRotation(0);
            mAnimator.setDuration(10000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(-1);
            mAnimator.start();
        }
    }
    return;
}
```



# View动画





------

> View动画一个非常大的缺陷突显，其不具有交互性。当某个元素发生View动画后，其响应事件的位置依然在动画进行前的地方，所以View动画只能做普通的动画效果，要避免涉及交互操作。但是它的优点也非常明显：效率比较高，使用也方便

```

/**旋转动画
 *

 * 6个参数的方法：
    float fromDegrees：旋转的开始角度。
    float toDegrees：旋转的结束角度。
    int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
    float pivotXValue：X坐标的伸缩值。
    int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
    float pivotYValue：Y坐标的伸缩值。
    4个参数的方法：
   旋转起始角度
   旋转结束角度
   后两个为旋转中心
     */
   public RotateAnimation getRotateAnimation(){
      RotateAnimation animation= new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,
               Animation.RELATIVE_TO_SELF,0.5f);
       animation.setFillAfter(true);//设置动画结束后保留当前状态
       animation.setDuration(2000);//动画持续时间
   //        animation.setRepeatMode(ValueAnimation.RESTART);//重复类型有两个值，取值为ValueAnimation.RESTART时,表示正序重新开始，当取值为ValueAnimation.REVERSE表示倒序重新开始。
   //        animation.setStartOffset(2000);//调用start函数之后等待开始运行的时间，单位为毫秒
   //        animation.setZAdjustment(300);//表示被设置动画的内容运行时在Z轴上的位置（top/bottom/normal），默认为normal
       animation.startNow();
       animation.setRepeatCount(3);//设置重复次数  这里的次数是从0开始计数的  即设置为2时执行了3次 设置为INFINITE表示无限循环
       return animation;
   }
   /**位移动画

 * 参数说明：

 * formXDelta: 表示X的起始值

 * toXDelta：X得结束值

 * fromYDelta：Y的起始值

 * toYDelta：Y得结束值
   */
   public TranslateAnimation getTranslateAnimation(){
       TranslateAnimation animation=new TranslateAnimation(-200,300,0,400);
       animation.setDuration(2000);
       animation.setFillAfter(true);
       animation.setRepeatCount(2);
       animation.startNow();
       return animation;
   }

   /**缩放动画

    * float fromX 动画起始时 X坐标上的伸缩尺寸
      float toX 动画结束时 X坐标上的伸缩尺寸
       float fromY 动画起始时Y坐标上的伸缩尺寸
       float toY 动画结束时Y坐标上的伸缩尺寸
       int pivotXType 动画在X轴相对于物件位置类型
       float pivotXValue 动画相对于物件的X坐标的开始位置
       int pivotYType 动画在Y轴相对于物件位置类型
       float pivotYValue 动画相对于物件的Y坐标的开始位置
       *
    * @return
      */
      public ScaleAnimation getScaleAnimation(){
      ScaleAnimation animation=new ScaleAnimation(0,2,0,2);
      animation.setDuration(2000);
      animation.setFillAfter(true);
      animation.setRepeatCount(2);
      animation.startNow();
    return animation;
}
```

 

    /**透明度动画  使用在淡入淡出
     * 参数说明：
     *   透明度的起始值 比如0.1
     *   透明度的结束值 比如1  1表示不透明
     *
     * @return
     */
    public AlphaAnimation getAlphaAnimation(){
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        animation.setRepeatCount(2);
        animation.startNow();
        return animation;
    }
     
    /**动画集合 可以将以上各种动画结合到一起实现
     * 在这里可以设置以上动画的不同次数来达到其他动画结束后还执行某次动画
     * 例如：以上其他动画结束后到达的位置还要多执行一次旋转动画
     * @return
     */
    public AnimationSet getAnimationSet(){
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(getRotateAnimation());
        animationSet.addAnimation(getTranslateAnimation());
        animationSet.addAnimation(getScaleAnimation());
        animationSet.addAnimation(getAlphaAnimation());
        animationSet.setFillAfter(true);
        animationSet.startNow();
        return animationSet;
    }


```
  另外，动画事件也提供了相应的监听回调：
  animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            
            }
 
        @Override
        public void onAnimationEnd(Animation animation) {
 
        }
 
        @Override
        public void onAnimationRepeat(Animation animation) {
 
        }
    });
```





​                

以上是一种动态的视图动画的写法，在Android中更提倡的是在res/anim/下设置一个xml文件来创建动画，如：

```

<?xml version="1.0" encoding="utf-8"?>  
<setxmlns:androidsetxmlns:android="http://schemas.android.com/apk/res/android"  
    android:interpolator="@[package:]anim/interpolator_resource"  
    android:shareInterpolator=["true" | "false"] ]]>  
    <alpha  
        android:fromAlpha="float"  
        android:toAlpha="float"/>  
    <scale  
        android:fromXScale="float"  
        android:toXScale="float"  
        android:fromYScale="float"  
        android:toYScale="float"  
        android:pivotX="float"  
        android:pivotY="float"/>  
    <translate  
        android:fromXDelta="float"  
        android:toXDelta="float"  
        android:fromYDelta="float"  
        android:toYDelta="float"/>  
    <rotate  
        android:fromDegrees="float"  
        android:toDegrees="float"  
        android:pivotX="float"  
        android:pivotY="float"/>  
    <set]]>  
        ...  
    </set>  
</set>  
```





# 属性动画



```
// 垂直移动
float curTranslationY = 100;
ObjectAnimator translationY
        = ObjectAnimator.ofFloat(line1, "translationY",
        curTranslationY, curTranslationY + 500f);

ObjectAnimator scaleY = ObjectAnimator.ofFloat(line1, "scaleY", 0.8f, 0.5f, 1f);
ObjectAnimator scaleX = ObjectAnimator.ofFloat(line1, "scaleX", 0.8f, 0.5f, 1f);

AnimatorSet animSet = new AnimatorSet();
animSet.play(scaleY).with(scaleX).after(translationY);
animSet.setDuration(2000);
animSet.start();
```

```cpp
AnimatorSet animSet = new AnimatorSet();
animSet.playTogether(translationY, scaleX, scaleY);
animSet.setDuration(2000);
animSet.start();
```



```xml
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:ordering="sequentially">

    <objectAnimator
        android:duration="1000"
        android:propertyName="translationY"
        android:valueFrom="0"
        android:valueTo="500"
        android:valueType="floatType" />

    <set android:ordering="sequentially">
        <objectAnimator
            android:duration="1000"
            android:propertyName="rotation"
            android:valueFrom="0"
            android:valueTo="360"
            android:valueType="floatType" />

        <set android:ordering="together">
            <objectAnimator
                android:duration="1000"
                android:propertyName="scaleX"
                android:valueFrom="1"
                android:valueTo="5"
                android:valueType="floatType" />
            <objectAnimator
                android:duration="1000"
                android:propertyName="scaleY"
                android:valueFrom="1"
                android:valueTo="5"
                android:valueType="floatType" />
        </set>
    </set>
</set>
```



# ViewPropertyAnimator

```java
view.animate()// 获取ViewPropertyAnimator对象
        // 动画持续时间
        .setDuration(5000)

        // 透明度
        .alpha(0)
        .alphaBy(0)

        // 旋转
        .rotation(360)
        .rotationBy(360)
        .rotationX(360)
        .rotationXBy(360)
        .rotationY(360)
        .rotationYBy(360)

        // 缩放
        .scaleX(1)
        .scaleXBy(1)
        .scaleY(1)
        .scaleYBy(1)

        // 平移
        .translationX(100)
        .translationXBy(100)
        .translationY(100)
        .translationYBy(100)
        .translationZ(100)
        .translationZBy(100)

        // 更改在屏幕上的坐标
        .x(10)
        .xBy(10)
        .y(10)
        .yBy(10)
        .z(10)
        .zBy(10)

        // 插值器
        .setInterpolator(new BounceInterpolator())//回弹
        .setInterpolator(new AccelerateDecelerateInterpolator())//加速再减速
        .setInterpolator(new AccelerateInterpolator())//加速
        .setInterpolator(new DecelerateInterpolator())//减速
        .setInterpolator(new LinearInterpolator())//线性

        // 动画延迟
        .setStartDelay(1000)

        //是否开启硬件加速
        .withLayer()

        // 监听
        .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        })

        .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        })

        .withEndAction(new Runnable() {
            @Override
            public void run() {
            }
        })
        .withStartAction(new Runnable() {
            @Override
            public void run() {
            }
        });
```

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\ViewPropertyAnimator.png)



------









# 插值器 估值器















































