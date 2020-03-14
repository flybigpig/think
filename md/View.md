# View

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
    
        - 
    
      


