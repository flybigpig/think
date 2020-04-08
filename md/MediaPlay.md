# MediaPlay

- odd

```
	mediaPlayer = new MediaPlayer();

	private void play(int resource) {
        try {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, resource);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
```



- MP3

  ```
  try {
              player = new MediaPlayer();
              assetManager = mContext.getAssets();
              player.stop();
              fileDescriptor = assetManager.openFd(fileString[index]);
              player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
              player.prepare();
              player.start();
          } catch (IOException e) {
              e.printStackTrace();
          }
  ```

  