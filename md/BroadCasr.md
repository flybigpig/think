# BroadCasr

```java
# 8.0发送广播被禁止

 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.mContext.sendBroadcast(mediaScanIntent);
        } else {
            mContext.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + path)));
        }

```

