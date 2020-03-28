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


