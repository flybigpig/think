# ViewPager Transformer

- android:clipChildren="false"

  - 父布局 不管子布局遮挡裁切

- ViewPager.PageTransformer

  - ```
    // 界面展示其他界面动效
    public class ViewPagerTransformer implements ViewPager.PageTransformer {
    
        @Override
        public void transformPage(View page, float position) {
            //最重要的就是这里了
            float v = Math.abs(position);
            float v1 = (float) (0.2 * (v * v));
            page.setScaleY(1 - v1);
        }
    }
    ```





