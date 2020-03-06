# Mopub

| Format                 | Size    | Ad unit ID                       |
| ---------------------- | ------- | -------------------------------- |
| Banner                 | 320x50  | b195f8dd8ded45fe847ad89ed1d016da |
| Medium Rectangle       | 300x250 | 252412d5e9364a05ab77d9396346d73d |
| Interstitial           | 320x480 | 24534e1901884e398f1253216226017e |
| Rewarded Video         | N/A     | 920b6145fb1546cf8b5cf2ac34638bb7 |
| Rewarded Video (MRAID) | 320x480 | 15173ac6d3e54c9389b9a5ddca69b34b |
| Rewarded Playable      | N/A     | 15173ac6d3e54c9389b9a5ddca69b34b |
| Native                 | N/A     | 11a17b188668469fb0412708c3d16813 |

```java

private MoPubView mBanner;
    private MoPubAdAdapter mAdAdapter;
    private RequestParameters mRequestParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

//        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("11a17b188668469fb0412708c3d16813")
//                .withLogLevel(MoPubLog.LogLevel.DEBUG)
//                .withLegitimateInterestAllowed(false)
//                .build();
//
//        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
//            @Override
//            public void onInitializationFinished() {
//                Log.d("Mopub", "SDK initialized");
//
//            }
//        });

    }

    private double nLenStart = 0;//监听 WebView所用手势


    private void setNative() {
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_ad_main_image)
                .iconImageId(R.id.native_ad_icon_image)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_text)
                .privacyInformationIconImageId(R.id.native_ad_privacy_information_icon_image)
                .sponsoredTextId(R.id.native_sponsored_text_view)
                .build();

        MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNativeAdPositioning.MoPubClientPositioning adPositioning = MoPubNativeAdPositioning.clientPositioning();
        adPositioning.addFixedPosition(1);
        adPositioning.addFixedPosition(3);
        adPositioning.addFixedPosition(7);

        mAdAdapter = new MoPubAdAdapter(this, null, adPositioning);
        mAdAdapter.registerAdRenderer(adRenderer);

        final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);

        mRequestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();

        mAdAdapter.loadAds("11a17b188668469fb0412708c3d16813", mRequestParameters);
    }

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

    @Override
    protected void onDestroy() {
        mBanner.destroy();
        super.onDestroy();
    }

    /*


    int nCnt = event.getPointerCount();
        Log.d("TAG", nCnt + "as");
        int n = event.getAction();
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt) {
            for (int i = 0; i < nCnt; i++) {
                float x = event.getX(i);
                float y = event.getY(i);
                Point pt = new Point((int) x, (int) y);
            }
            int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
            int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));
            nLenStart = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);
        } else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP && 2 == nCnt) {
            for (int i = 0; i < nCnt; i++) {
                float x = event.getX(i);
                float y = event.getY(i);
                Point pt = new Point((int) x, (int) y);
            }
            int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
            int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));

            double nLenEnd = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);

            if (nLenEnd > nLenStart)//通过两个手指开始距离和结束距离，来判断放大缩小
            {
                Toast.makeText(getApplicationContext(), "放大", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "缩小", Toast.LENGTH_SHORT).show();

            }
        }
        return false;

     */
```

