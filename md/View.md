# View

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\window.png)

[https://blog.csdn.net/qiaoyl113/article/details/84662688](https://blog.csdn.net/qiaoyl113/article/details/84662688)

![](C:\Users\czdxn\Desktop\md\think\think\md\pic\view.png)

- ViewGroup也继承自View

- offsetLeftAndRight

- offsetTopAndBottom

- LayoutParams

  ```
  
   LinearLayout.LayoutParams latoutParams =(LinearLayout.LayoutParams).getLayoutParams();
                  latoutParams.bottomMargin=getLeft()+getRight();
                  setLayoutParams(latoutParams);
  ```

  



  - 

    ```
    
     LinearLayout.LayoutParams latoutParams =(LinearLayout.LayoutParams).getLayoutParams();
                    latoutParams.bottomMargin=getLeft()+getRight();
                    setLayoutParams(latoutParams);
    
    ```

  - View动画

    - ```
      mCustomView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
      // View动画并不能改变View的位置参数,原位置点击有效
      ```

  - ObjectAnimator

    - ```
      
      ```

  - ScrollTo / ScrollBy

  - Scroller

    ```
    //重写computeScroll(),系统会在绘制view的时候 在draw()方法调用该方法。
      @Override
        public void computeScroll() {
            super.computeScroll();
            if(mScroller.computeScrollOffset()){
              ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
                invalidate();
            }
      }
        
    ```
    
  - 解析Scoller

    - 3个构造方法通常使用第一个，传入上下文即可

    - startScoll()方法只是保存各个参数，为滑动做准备

    - 关键是我们在startScroll（）方法后调用了 invalidate（）方法，这个方法会导致View的重绘，而View的重绘会调用View的draw（）方法，draw（）方法又会调用View的computeScroll（）方法。

    - somputeScroll()通过获取当前scrollX,scrollY,然后调用scrollTo() 方法进行View滑动，接着调用invalidate方法让view进行重绘。重绘调用computeScroll() 实现view 滑动。

    - 还有 Scroller获取当前位置的scroll X，scrollY.

      - 在调用scrollTo() 方法前会调用Scroller的computeScrollOffset()

      - ```
        /**
             * Call this when you want to know the new location.  If it returns true,
             * the animation is not yet finished.
             */ 
            public boolean computeScrollOffset() {
                if (mFinished) {
                    return false;
                }
        		//计算动画持续时间
                int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
            
                if (timePassed < mDuration) {
                    switch (mMode) {
                    case SCROLL_MODE:
                        final float x = mInterpolator.getInterpolation(timePassed * mDurationReciprocal);
                        mCurrX = mStartX + Math.round(x * mDeltaX);
                        mCurrY = mStartY + Math.round(x * mDeltaY);
                        break;
                    case FLING_MODE:
                        final float t = (float) timePassed / mDuration;
                        final int index = (int) (NB_SAMPLES * t);
                        float distanceCoef = 1.f;
                        float velocityCoef = 0.f;
                        if (index < NB_SAMPLES) {
                            final float t_inf = (float) index / NB_SAMPLES;
                            final float t_sup = (float) (index + 1) / NB_SAMPLES;
                            final float d_inf = SPLINE_POSITION[index];
                            final float d_sup = SPLINE_POSITION[index + 1];
                            velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
                            distanceCoef = d_inf + (t - t_inf) * velocityCoef;
                        }
        
                        mCurrVelocity = velocityCoef * mDistance / mDuration * 1000.0f;
                        
                        mCurrX = mStartX + Math.round(distanceCoef * (mFinalX - mStartX));
                        // Pin to mMinX <= mCurrX <= mMaxX
                        mCurrX = Math.min(mCurrX, mMaxX);
                        mCurrX = Math.max(mCurrX, mMinX);
                        
                        mCurrY = mStartY + Math.round(distanceCoef * (mFinalY - mStartY));
                        // Pin to mMinY <= mCurrY <= mMaxY
                        mCurrY = Math.min(mCurrY, mMaxY);
                        mCurrY = Math.max(mCurrY, mMinY);
        
                        if (mCurrX == mFinalX && mCurrY == mFinalY) {
                            mFinished = true;
                        }
        
                        break;
                    }
                }
                else {
                    mCurrX = mFinalX;
                    mCurrY = mFinalY;
                    mFinished = true;
                }
                return true;
            }
        ```

      - 

    - scroller原理

        Scroller的原理：Scroller并不能直接实现View的滑动，它需要配合View的computeScroll（）方法。在computeScroll（）中不断让View进行重绘，每次重绘都会计算滑动持续的时间，根据这个持续时间就能算出这次View滑动的位置，我们根据每次滑动的位置调用scrollTo（）方法进行滑动，这样不断地重复上述过程就形成了弹性滑动。
    
    - View事件分发机制
    
        - setContent()
        
          getWindow().setContentView();
        
          getWindow() ----->Activity.attach() --> mWindow =new PhoneWindow(this);
        
          ```
          # phoneWindow.setContentView();
          
          @Override
              public void setContentView(int layoutResID) {
                  // Note: FEATURE_CONTENT_TRANSITIONS may be set in the process of installing the window
                  // decor, when theme attributes and the like are crystalized. Do not check the feature
                  // before this happens.
                  if (mContentParent == null) {
                      installDecor();
                  } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
                      mContentParent.removeAllViews();
                  }
          
                  if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
                      final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                              getContext());
                      transitionTo(newScene);
                  } else {
                      mLayoutInflater.inflate(layoutResID, mContentParent);
                  }
                  mContentParent.requestApplyInsets();
                  final Callback cb = getCallback();
                  if (cb != null && !isDestroyed()) {
                      cb.onContentChanged();
                  }
                  mContentParentExplicitlySet = true;
              }
          ```
        
    
      
    
- ###### 伪代码事件处理

  ```
   @Override
      public boolean dispatchTouchEvent(MotionEvent ev) {
          boolean result = false;
          if (onInterceptTouchEvent(ev)) {
              result = childView.onTouchEvent(ev);
          } else {
              result = childView.dispatchTouchEvent(ev);
          }
          return result;
      }
  ```

- view工作流程

  - **DecorView被加载到Window中**

    startActivity -->ActivityThread.handleLunchActivity()--->创建Activity

    ```
    private void handleLauncherActivity(ActivityRecord r,Intent customIntent){
    ```
    	Activity a = performLaunchActivity(r,customIntent);
    	if(a!=null){
    		r.createdConfig = new Configuration(mConfiguration);
    		Bundle oldState = r.state;
    		handleResumeActivity(r.token,false,r.isForward,
    	!r.activity.mFinished 	&&  !r.startsNotResumed);
    	}
    	```
    }
    -----------------------------
    performLaunchActivity 创建Activity 在这里会调用Activity的onCreate(),从而完成DecorView的创建。
    
    ```
find void handleResumeActivity(IBinder token,boolean clearHide,boolean isForward,boolean reallyResume){
    	unsheduleGcIdler();
    	mSomeActivitiesChanged= true;
    	ActivityClientRecord r = performResumeActivity(token,clearHide);
    	if(r!=null){
    		final Activity a = r.activity;
    ```
    		if(r.window==null && !a.mFinished && willBeVisible){
    			r.window=r.activity.getwindow;
    			view decor = r.window.getDecorView();
    			decor.setVisiblity(Vier.INVISIABLE);
    			ViewManager vm =a.getWindowManager();
    			WindowManager.LayoutParams l =r.window.getAttributes();
    			a,mDecor = decor;
    			l.type =WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
    			l.softInputMode |=forwardBit;
    			if(a.mVisibleFromClient){
    				a.mWindowAdded = true ;
    				wm.addView(decor,l);
    			}
    		}
    		```
    	}
    }
    ```
    此处会调用onResume方法
    得到WindowManager，一个接口并继承实现ViewManager.
    实际调用WindowManagerImpl的addView()方法
    ```
    
  - ```
     // measureSpec
    public static int getChildMeasureSpec(int spec,int padding,int child-Dimension) {
            int specMode = MeasureSpec.getMode(spec);
            int specSize = MeasureSpec.getSize(spec);
            int size = Math.max(0,specSize -padding);
            int resultSize = 0;
            int resultMode = 0;
            switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                    if (childDimension => 0) {
                            resultSize = childDimension;
                            resultMode = MeasureSpec.EXACTLY;
                    } else if (childDimension == LayoutParams.MATCH_PARENT) {
                            // Child wants to be our size. So be it.
                            resultSize = size;
                            resultMode = MeasureSpec.EXACTLY;
                    } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                            // Child wants to determine its own size. It can't be
                            // bigger than us.
                            resultSize = size;
                            resultMode = MeasureSpec.AT_MOST;
                    }
                    break;
            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                    if (childDimension => 0) {
                            // Child wants a specific size... so be it
                            resultSize = childDimension;
                            resultMode = MeasureSpec.EXACTLY;
                    } else if (childDimension == LayoutParams.MATCH_PARENT) {
                            // Child wants to be our size,but our size is not fixed.
                            // Constrain child to not be bigger than us.
                            resultSize = size;
                            resultMode = MeasureSpec.AT_MOST;
                    } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                            // Child wants to determine its own size. It can't be
                            // bigger than us.
                            resultSize = size;
                            resultMode = MeasureSpec.AT_MOST;
                    }
                    break;
            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                    if (childDimension => 0) {
                            // Child wants a specific size... let him have it
                            resultSize = childDimension;
                            resultMode = MeasureSpec.EXACTLY;
                    } else if (childDimension == LayoutParams.MATCH_PARENT) {
                            // Child wants to be our size... find out how big it should
                            // be
                            resultSize = 0;
                            resultMode = MeasureSpec.UNSPECIFIED;
                    } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                            // Child wants to determine its own size.... find out how
                            // big it should be
                            resultSize = 0;
                            resultMode = MeasureSpec.UNSPECIFIED;
                    }
                    break;
            }
            return MeasureSpec.makeMeasureSpec(resultSize,resultMode);
        }
    
    ```
  
  
  
  - ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\measureSpec1.jpg)
  - ![](C:\Users\czdxn\Desktop\md\think\think\md\pic\MeasureSpec2.jpg)





### 获取activity根布局

***View view = getWindow().getDecorView();***

```
View view = getWindow().getDecorView();
RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout);
relativeLayout.setBackgroundColor(Color.BLACK);
```











# 以下为自定义view实战

------



- Paint.setStyle()
  - Paint.Style.Fill  :仅仅填充内部
  - Paint.Style.Fill_AND_STROKE: 填充内部和描边
  - Paint.Style.STROKE:仅仅描边
- Paint.setStrokeWidth()
  - 设置画笔宽度
- Canvas
- Path ： 默认情况下路径都是链接好的，除了
  - 调用AddXXX系列函数，将直接添加固定形状路径
  - 调用moveTo()函数改变回值起始位置
- **Region(区域)**
  - 本意不是用来绘图
  - 间接构造
    - 无论是调用set系列函数的Region是不是有区域值，当调用set系列函数后，原来的区域值就会被替换成set系列函数的区域值
    - setPath(Path path,Region clip)
- Canvas变换
  - clip
  - save
  - restore











