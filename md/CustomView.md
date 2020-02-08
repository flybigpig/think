# 自定义View

#### 初始化自定义属性

+ 自定义属性

  ```java
  
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
          <declare-styleable name="HwLoadingView">
              <!--控件大小-->
              <attr name="hlv_size" format="dimension" />
              <!--控件颜色-->
              <attr name="hlv_color" format="color" />
              <!--转一圈花费时长-->
              <attr name="hlv_duration" format="integer"/>
          </declare-styleable>
      </resources>
      
  ```

+ 自定义属性初始化

  ```
  
          TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HwLoadingView);
  
          mSize = (int) ta.getDimension(R.styleable.HwLoadingView_hlv_size, dp2px(context, 100));
          setSize(mSize);
  
          mColor = ta.getColor(R.styleable.HwLoadingView_hlv_color, Color.parseColor("#333333"));
          setColor(mColor);
  
          mDuration = ta.getInt(R.styleable.HwLoadingView_hlv_duration, 1500);
  
          ta.recycle();
  ```

  