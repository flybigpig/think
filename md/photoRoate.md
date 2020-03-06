三星旋转问题

```

private void setImageView(Uri uri, AutographView mAutographView) {
        String path = RealPathFromUriUtils.getRealPathFromUri(EditPhotoActivity.this, uri);
        if (path == null) {
            path = uri.getPath();
        }
        int angle = DiyCommonUtil.readPictureDegree(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            int height = options.outHeight;
            int width = options.outWidth;
            int sampleSize = 1;
            int max = Math.max(height, width);
            if (max > MAX_SIZE) {
                int nw = width / 2;
                int nh = height / 2;
                while ((nw / sampleSize) > MAX_SIZE || (nh / sampleSize) > MAX_SIZE) {
                    sampleSize *= 2;
                }
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
            Bitmap selectdBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            selectdBitmap = DiyCommonUtil.rotaingImageView(angle, selectdBitmap);
            mAutographView.setBackground(BitmapUtils.BitmapToDrawable(selectdBitmap, this));
        } catch (IOException ioe) {

        }
    }
```



```
Utils_1

package com.fairy.thickness.open.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class DiyCommonUtil {


    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }



    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/quickwater/";



    public static void saveBitmap2file(Bitmap bmp, Context context, Activity activity) {


        if (bmp == null) {


            return;
        }
        String savePath;


        String fileName = generateFileName() + ".JPEG";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {


            savePath = SD_PATH;


        } else {
            Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();


            return;
        }


        File filePic = new File(savePath + fileName);
        try {
            if (!filePic.exists()) {


                filePic.getParentFile().mkdirs();


                filePic.createNewFile();


            }
            FileOutputStream fos = new FileOutputStream(filePic);


            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            fos.close();
            activity.finish();
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        }
        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    filePic.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath + fileName)));
    }

    public static Bitmap returnRotatePhoto(String originpath) {


        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);


        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = getCompressPhoto(originpath);


        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);


        // 保存修复后的图片并返回保存后的图片路径
        return bitmap;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i("111", "readPictureDegree: orientation-------->" + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:

                degree = 90;


                break;
                case ExifInterface.ORIENTATION_ROTATE_180:

                degree = 180;


                break;
                case ExifInterface.ORIENTATION_ROTATE_270:

                degree = 270;


                break;
            }
        } catch (IOException e) {
            e.printStackTrace();


        }


        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {

        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();


        matrix.postRotate(angle);
        try {


            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            if (bitmap != null) {
                returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;

        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();


        options.inJustDecodeBounds = false;
        options.inSampleSize = 1; // 图片的大小设置为原来的十分之一


        Bitmap bmp = BitmapFactory.decodeFile(path, options);


        options = null;
        return bmp;
    }
}

```

```
package com.fairy.thickness.open.utils;

/**
 * @author
 * @date 2020/1/9.
 * GitHub：
 * email：
 * description：
 */

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.util.Arrays;
import java.util.Calendar;


public class RealPathFromUriUtils {


    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {

        int sdkVersion = Build.VERSION.SDK_INT;

        if (sdkVersion >= 19) { // api >= 19

            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19

            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }



    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {

        return getDataColumn(context, uri, null, null);
    }



    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {

        String filePath = null;

        if (DocumentsContract.isDocumentUri(context, uri)) {

            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);

            if (isMediaDocument(uri)) { // MediaProvider

                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";

                String[] selectionArgs = {id};

                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);

            } else if (isDownloadsDocument(uri)) { // DownloadsProvider

                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));

                filePath = getDataColumn(context, contentUri, null, null);

            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);

        } else if ("file".equals(uri.getScheme())) {

            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();

        }

        return filePath;
    }



    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};

        Cursor cursor = null;

        try {

            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {

                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);

                path = cursor.getString(columnIndex);

            }

        } catch (Exception e) {

            if (cursor != null) {

                cursor.close();

            }

        }

        return path;
    }



    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {

        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {

        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


}



```

