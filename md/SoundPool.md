# SoundPool

我们可以加载多个音频资源到内存，进行管理与播放

- 我们需要监听加载完毕后才能播放

SoundPool还可以调节左右声道的音量值，调整播放的语速，设置播放优先级，以及播放次数。

```
		/**
	     * 创建SoundPool ，注意 api 等级
	     */
	    private void createSoundPoolIfNeeded() {
	        if (mSoundPool == null) {
	            // 5.0 及 之后
	            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
	                AudioAttributes audioAttributes = null;
	                audioAttributes = new AudioAttributes.Builder()
	                        .setUsage(AudioAttributes.USAGE_MEDIA)
	                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
	                        .build();
	
	                mSoundPool = new SoundPool.Builder()
	                        .setMaxStreams(16)
	                        .setAudioAttributes(audioAttributes)
	                        .build();
	            } else { // 5.0 以前
	                mSoundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
	            }
	
	            mSoundPool.setOnLoadCompleteListener(this);  // 设置加载完成监听
	        }
	    }	

```

- setAudioAttributes用来设置audio 属性，此值要么不设，要么设置不为null的值，否则会导致异常产生

  ```
  public Builder setAudioAttributes(AudioAttributes attributes)
                  throws IllegalArgumentException {
              if (attributes == null) {
                  throw new IllegalArgumentException("Invalid null AudioAttributes");
              }
              mAudioAttributes = attributes;
              return this;
          }
  
          public SoundPool build() {
              if (mAudioAttributes == null) {
                  mAudioAttributes = new AudioAttributes.Builder()
                          .setUsage(AudioAttributes.USAGE_MEDIA).build();
              }
              return new SoundPool(mMaxStreams, mAudioAttributes);
          }
  ```

  

```
  // 返回值streamID ， 返回0则播放失败,否则成功
  
  public final int play(int soundID, float leftVolume, float rightVolume,
            int priority, int loop, float rate) {
        baseStart();
        return _play(soundID, leftVolume, rightVolume, priority, loop, rate);
    }
```

```

// 资源释放
/**
     * 释放资源
     */
    private void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
            mSoundPool.unload(mSoundId);
            mSoundId = DEFAULT_INVALID_SOUND_ID;
            mSoundPool.release();
            mSoundPool = null;
        }
    }
```



