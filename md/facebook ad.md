# Facebook ad



[TOC]



------



### prepare

```
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id" />
```



```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.MANAGE_ACCOUNTS" />
```



```
android:networkSecurityConfig="@xml/network_security_config"
​```

<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- Must be included for proper work of the cache. -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">127.0.0.1</domain>
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </domain-config>

    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">example.com</domain>
        <domain includeSubdomains="true">cdn.example2.com</domain>
    </domain-config>
    <!-- For internal use only. -->
    <domain-config>
        <domain includeSubdomains="true">facebook.com</domain>
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </domain-config>
</network-security-config>

```



```
native_ad_unit.xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/ad_unit"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/white"
    android:orientation="vertical"
    android:paddingLeft="10dp"
    android:paddingRight="10dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:paddingBottom="10dp"
        android:paddingTop="10dp">

        <com.facebook.ads.AdIconView
            android:id="@+id/native_ad_icon"
            android:layout_width="35dp"
            android:layout_height="35dp" />

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingLeft="5dp"
            android:paddingRight="5dp">

            <TextView
                android:id="@+id/native_ad_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:ellipsize="end"
                android:lines="1"
                android:textColor="@android:color/black"
                android:textSize="15sp" />

            <TextView
                android:id="@+id/native_ad_sponsored_label"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:ellipsize="end"
                android:lines="1"
                android:textColor="@android:color/darker_gray"
                android:textSize="12sp" />

        </LinearLayout>

        <LinearLayout
            android:id="@+id/ad_choices_container"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="end"
            android:orientation="horizontal" />

    </LinearLayout>

    <com.facebook.ads.MediaView
        android:id="@+id/native_ad_media"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="5dp">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="3"
            android:orientation="vertical">

            <TextView
                android:id="@+id/native_ad_social_context"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ellipsize="end"
                android:lines="1"
                android:textColor="@android:color/darker_gray"
                android:textSize="12sp" />

            <TextView
                android:id="@+id/native_ad_body"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ellipsize="end"
                android:gravity="center_vertical"
                android:lines="2"
                android:textColor="@android:color/black"
                android:textSize="12sp" />

        </LinearLayout>

        <Button
            android:id="@+id/native_ad_call_to_action"
            android:layout_width="100dp"
            android:layout_height="30dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:background="#4286F4"
            android:paddingLeft="3dp"
            android:paddingRight="3dp"
            android:textColor="@android:color/white"
            android:textSize="12sp"
            android:visibility="gone" />

    </LinearLayout>

</LinearLayout>
```



```
AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("45361e6907473b329c4e35b21516276e");
```



```
<string name="fb_test_id">IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID</string>
```



```
// 获取设备id
public class DeviceUuidFactory {
    
    protected static final String PREFS_FILE = "device_id";

    protected static final String PREFS_DEVICE_ID = "device_id";

    protected static UUID uuid;

    private static String deviceType = "0";

    private static final String TYPE_ANDROID_ID = "1";

    private static final String TYPE_DEVICE_ID = "2";

    private static final String TYPE_RANDOM_UUID = "3";

    public DeviceUuidFactory(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                deviceType = TYPE_ANDROID_ID;
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    Activity#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for Activity#requestPermissions for more details.

                                    }
                                }
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                if (deviceId != null
                                        && !"0123456789abcdef".equals(deviceId.toLowerCase())
                                        && !"000000000000000".equals(deviceId.toLowerCase())) {
                                    deviceType = TYPE_DEVICE_ID;
                                    uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
                                } else {
                                    deviceType = TYPE_RANDOM_UUID;
                                    uuid = UUID.randomUUID();
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            deviceType = TYPE_RANDOM_UUID;
                            uuid = UUID.randomUUID();
                        } finally {
                            uuid = UUID.fromString(deviceType + uuid.toString());
                        }

                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
    }

    public UUID getDeviceUuid() {
        Log.d("DeviceUuidFactory", "------>获取的设备ID号为：" + uuid.toString());
        return uuid;
    }
}

```



------



### Banner

```
private void setBanner() {
    // Banner test "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    adView = new AdView(this, getResources().getString(R.string.fb_test_id), AdSize.BANNER_HEIGHT_50);

    // Find the Ad Container
    LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

    // Add the ad view to your activity layout
    adContainer.addView(adView);
    adView.setAdListener(new AdListener() {
        @Override
        public void onError(Ad ad, AdError adError) {
            Log.d("fbAD", adError.getErrorMessage());
            // Ad error callback
            Toast.makeText(MainActivity.this, "Error: " + adError.getErrorMessage(),
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onAdLoaded(Ad ad) {
            Log.d("fbAD", ad.getPlacementId());
        }

        @Override
        public void onAdClicked(Ad ad) {
            Log.d("fbAD", ad.getPlacementId());
        }

        @Override
        public void onLoggingImpression(Ad ad) {
            Log.d("fbAD", "FSd");
        }
    });
    // Request an ad
    adView.loadAd();
}
```

### native

```
private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).

        TAG = "Native";

        nativeAd = new NativeAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }


    /**
     * 填充原生广告
     *
     * @param nativeAd
     */
    private void inflateAd(NativeAd nativeAd) {
        // 取消默认的view
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_unit, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }
```

### InterstitialAd

```
private void loadInterstitialAd() {

        interstitialAd = new InterstitialAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();

    }
```







------



| Test Ad Type                      | Description                                              | Supported Ad Format                  |
| --------------------------------- | -------------------------------------------------------- | ------------------------------------ |
| `CAROUSEL_IMG_SQUARE_APP_INSTALL` | carousel ad with square image and app install CTA option | Interstitial, Native                 |
| `CAROUSEL_IMG_SQUARE_LINK`        | carousel ad with square image and link CTA option        | Interstitial, Native                 |
| `IMG_16_9_APP_INSTALL`            | 16x9 image ad with app install CTA option                | Banner, Interstitial, Native         |
| `IMG_16_9_LINK`                   | 16x9 image ad with link CTA option                       | Banner, Interstitial, Native         |
| `PLAYABLE`                        | Playable ad with app install CTA option                  | Interstitial, Rewarded Video         |
| `VID_HD_9_16_39S_APP_INSTALL`     | 9x16 HD video 39 sec ad with app install CTA option      | Interstitial, Native, Rewarded Video |
| `VID_HD_9_16_39S_LINK`            | 9x16 HD video 39 sec ad with link CTA option             | Interstitial, Native, Rewarded Video |
| `VID_HD_16_9_15S_APP_INSTALL`     | 16x9 HD video 15 sec ad with app install CTA option      | Interstitial, Native, Rewarded Video |
| `VID_HD_16_9_15S_LINK`            | 16x9 HD video 15 sec ad with link CTA option             | Interstitial, Native, Rewarded Video |
| `VID_HD_16_9_46S_APP_INSTALL`     | 16x9 HD video 46 sec ad with app install CTA option      | Interstitial, Native, Rewarded Video |
| `VID_HD_16_9_46S_LINK`            | 16x9 HD video 46 sec ad with link CTA option             | Interstitial, Native, Rewarded Video |