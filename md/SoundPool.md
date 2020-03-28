# soundPool

```
if (Build.VERSION.SDK_INT > 21) {
    SoundPool.Builder builder = new SoundPool.Builder();

    builder.setMaxStreams(5);

    AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();

    attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//STREAM_MUSIC

    builder.setAudioAttributes(attrBuilder.build());
    soundpool = builder.build();
} else {
    soundpool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
}

soundmap.put(1, soundpool.load(this, R.raw.samp1, 1));
soundmap.put(2, soundpool.load(this, R.raw.samp2, 2));
soundmap.put(3, soundpool.load(this, R.raw.samp3, 3));
soundmap.put(4, soundpool.load(this, R.raw.samp4, 4));

soundmap.put(5, soundpool.load(this, R.raw.heiha, 5));
soundmap.put(6, soundpool.load(this, R.raw.xiha, 6));
soundmap.put(7, soundpool.load(this, R.raw.dala, 7));

soundpool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        soundPool.autoPause();
        load = true;
    }
});

mMAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
currentVolume = mMAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);


                    streadId = soundpool.play(soundmap.get(isPlay), mMAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), mMAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), 0, -1, 1);

```