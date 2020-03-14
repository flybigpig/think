# mopub

| Format                 | Size    | Ad unit ID                       |
| ---------------------- | ------- | -------------------------------- |
| Banner                 | 320x50  | b195f8dd8ded45fe847ad89ed1d016da |
| Medium Rectangle       | 300x250 | 252412d5e9364a05ab77d9396346d73d |
| Interstitial           | 320x480 | 24534e1901884e398f1253216226017e |
| Rewarded Video         | N/A     | 920b6145fb1546cf8b5cf2ac34638bb7 |
| Rewarded Video (MRAID) | 320x480 | 15173ac6d3e54c9389b9a5ddca69b34b |
| Rewarded Playable      | N/A     | 15173ac6d3e54c9389b9a5ddca69b34b |
| Native                 | N/A     | 11a17b188668469fb0412708c3d16813 |



------



### Banner

```
private void initView() {

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("11a17b188668469fb0412708c3d16813")
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .build();

        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d("Mopub", "SDK initialized");
				//banner
				//native
				//insertAD

            }
        });

    }


 

 
```



### Banner

```
   private void setBanner() {
        mBanner = (MoPubView) findViewById(R.id.banner);
        mBanner.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da"); // Enter your Ad Unit ID from www.mopub.com
        mBanner.loadAd();
        mBanner.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Log.d("Mopub", "yes");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Log.d("Mopub", errorCode + "");
            }

            @Override
            public void onBannerClicked(MoPubView banner) {

            }

            @Override
            public void onBannerExpanded(MoPubView banner) {

            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {

            }
        });
    }
```



### MoPubInterstitial

```

    private void setInterstitial() {
        mInterstitial = new MoPubInterstitial(this, "24534e1901884e398f1253216226017e");
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
//                Log.d("mInterstitial", interstitial.getUserDataKeywords());
                interstitial.show();
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                Log.d("mInterstitial", errorCode.toString() + " " + interstitial.getKeywords());
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
//                Log.d("mInterstitialShow", interstitial.getUserDataKeywords());

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                Log.d("mInterstitialClicked", interstitial.getUserDataKeywords());

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                Log.d("mInterstitialDismissed", interstitial.getUserDataKeywords());
            }
        });

        mInterstitial.load();
    }
```



### Native

```
  private void setNative() {
        moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(final NativeAd nativeAd) {
                Log.d("MoPub", "Native ad has loaded.");

                showAD(nativeAd);
                setBanner();

            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d("MoPub", "Native ad failed to load with error: " + errorCode.toString());
            }
        };

        moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {
                Log.d("MoPub", "Native ad recorded an impression.");
                // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
            }

            @Override
            public void onClick(View view) {
                Log.d("MoPub", "Native ad recorded a click.");
                // Click tracking.
            }
        };

        moPubNative = new MoPubNative(this, "11a17b188668469fb0412708c3d16813 ", moPubNativeNetworkListener);

        viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_ad_main_image)
                .iconImageId(R.id.native_ad_icon_image)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_text)
                .privacyInformationIconImageId(R.id.native_ad_privacy_information_icon_image)
                .build();

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.STAR_RATING
        );

        RequestParameters mRequestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();

        adapterHelper = new AdapterHelper(this, 0, 3); // When standalone, any range will be fine.

        moPubNative.makeRequest(mRequestParameters);


    }

    private void showAD(NativeAd nativeAd) {

        nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

        AdapterHelper adapterHelper = new AdapterHelper(MainActivity.this, 0, 2);
        adapterHelper.getAdView((View) null, (ViewGroup) null, nativeAd, viewBinder);

        // Retrieve the pre-built ad view that AdapterHelper prepared for us.
        View v = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());
        // Set the native event listeners (onImpression, and onClick).
        nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);
        // Add the ad view to our view hierarchy

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.native_ad);
        frameLayout.addView(v);


    }

```



```
native_ad_layout.xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/native_outer_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/white"
    android:textDirection="locale">


    <ImageView
        android:id="@+id/native_ad_icon_image"
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:layout_marginStart="10dp"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:background="@null"
        android:contentDescription="@null" />

    <TextView
        android:id="@+id/native_ad_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/native_ad_icon_image"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_marginStart="10dp"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:textColor="@android:color/darker_gray" />

    <TextView
        android:id="@+id/native_ad_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:layout_marginStart="84dp"
        android:layout_marginLeft="84dp"
        android:layout_marginTop="32dp"
        android:textColor="@android:color/darker_gray"
        android:textStyle="bold" />

    <ImageView
        android:id="@+id/native_ad_main_image"
        android:layout_width="match_parent"
        android:layout_height="@dimen/native_main_image_height"
        android:layout_below="@+id/native_ad_text"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:layout_marginRight="10dp"
        android:background="@null"
        android:scaleType="centerCrop" />


    <ImageView
        android:id="@+id/native_ad_privacy_information_icon_image"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_alignParentTop="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentRight="true"
        android:padding="10dp" />

    <TextView
        android:id="@+id/native_sponsored_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/native_ad_main_image"
        android:layout_alignStart="@id/native_ad_main_image"
        android:layout_alignLeft="@id/native_ad_main_image"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="10dp"
        android:layout_marginRight="10dp"
        android:layout_marginBottom="10dp"
        android:visibility="invisible" />

</RelativeLayout>
```

