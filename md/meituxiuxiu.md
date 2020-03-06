# meituxiuxiu

```java
package com.fairy.thickness.open.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fairy.thickness.open.R;
import com.fairy.thickness.open.view.DragScaleView;
import com.fairy.thickness.open.view.Paints;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends BaseActivity {

    private DragScaleView mDragScaleView;
    private Canvas canvas;
    private Paint paint;
    private ImageView photo;
    private int x, y;
    private Bitmap bm;
    private Bitmap copy;
    private Paints paints;
    private ImageView mCanva;
    private LinearLayout linearLayout;
    private String sdCardDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void initView() {

//        mDragScaleView = (DragScaleView) findViewById(R.id.dragScaleView);
//
//        mDragScaleView.setImageResource(MainActivity.this, R.drawable.account);
//
////        mDragScaleView.setBackground(getResources().getDrawable(R.drawable.account));
//
//        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switchButton);
//
//        switchCompat.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mDragScaleView.setDraw(true);
//                    Toast.makeText(MainActivity.this, "on", Toast.LENGTH_SHORT).show();
//                } else {
//                    mDragScaleView.setDraw(false);
//                    Toast.makeText(MainActivity.this, "off", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        mCanva = (ImageView) findViewById(R.id.canvas);

        linearLayout = (LinearLayout) findViewById(R.id.bottom);

        paints = (Paints) findViewById(R.id.paints);

        paints.setBackground(getResources().getDrawable(R.drawable.account));

        DisplayMetrics outMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        int widthPixels = outMetrics.widthPixels;

        int heightPixels = outMetrics.heightPixels;

        // 加载原始图片
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.account);
        // 需要创建一个和原始的图片大小一样的空白图片(一张纸,上面没有任何数据)
        copy = bm.createBitmap(widthPixels, heightPixels - linearLayout.getHeight(), bm.getConfig());
        // 需要一个画板,画板上铺上白纸
        canvas = new Canvas(copy);
        paint = new Paint();
        paint.setStrokeWidth(10); //设置画笔宽度
        paint.setColor(Color.BLACK);

        // 创建画笔

        mCanva.setOnTouchListener(new View.OnTouchListener() {
            int startx = 0;
            int starty = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 拿到动作
                int type = event.getAction();
                switch (type) {
                    case MotionEvent.ACTION_DOWN:
                        startx = (int) event.getX();
                        starty = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endx = (int) event.getX();
                        int endy = (int) event.getY();

                        paints.x = endx;
                        paints.y = endy;
                        paints.invalidate();

                        canvas.drawLine(startx, starty, endx, endy, paint);
                        startx = (int) event.getX();
                        starty = (int) event.getY();
                        mCanva.setImageBitmap(copy);
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            save(copy);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }
        });


    }

    public void save(Bitmap bitmap) throws IOException {
        sdCardDir = Environment.getExternalStorageDirectory() + "/image/";
        //BitmapUtil.createScaledBitmapByHeight(srcBitmap, 300);//  压缩图片
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(sdCardDir);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }


}
```