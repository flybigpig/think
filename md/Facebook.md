# Facebook



# Adding Interstitial Ads to your Android App

The Audience Network allows you to monetize your Android apps with Facebook ads. An interstitial ad is a full screen ad that you can show in your app. Typically interstitial ads are shown when there is a transition in your app. For example -- after finishing a level in a game or after loading a story in a news app.

Ensure you have completed the Audience Network [Getting Started](https://developers.facebook.com/docs/audience-network/getting-started) and [Android Getting Started](https://developers.facebook.com/docs/audience-network/android) guides before you proceed.

## Steps-by-Step

#### [Step 1: Initializing Interstitial Ads in your Activity](https://developers.facebook.com/docs/audience-network/guides/ad-formats/interstitial/android#implementation)

#### [Step 2: Showing Interstitial Ads in your Activity](https://developers.facebook.com/docs/audience-network/guides/ad-formats/interstitial/android#showing)

## Initialize the Audience Network SDK

This method was added in the Android Audience Network SDK version 5.1.

Explicit initialization of the Audience Network Android SDK is required for version `5.3.0` and greater. Please refer to [this document](https://developers.facebook.com/docs/audience-network/android-sdk-initialize/) about how to initialize the Audience Network Android SDK.

Before creating an ad object and loading ads, you should initialize the Audience Network SDK. It is recommended to do this at app launch.

```
public class YourApplication extends Application {
    ...
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);       
    }
    ...
}
```

## Step 1: Initializing Interstitial Ads in your Activity

Add the following code at the top of your Activity in order to import the Facebook Ads SDK:

```
import com.facebook.ads.*;
```

Initialize the `InterstitialAd`.

```
private InterstitialAd interstitialAd;

@Override
public void onCreate(Bundle savedInstanceState) {
...
  // Instantiate an InterstitialAd object. 
  // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
  // now, while you are testing and replace it later when you have signed up.
  // While you are using this temporary code you will only get test ads and if you release
  // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
  interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
...  
```

## Step 2: Showing Interstitial Ads

#### Scenario 1: Set an `InterstitialAdListener`, load the Ad and show the Ad immediately it is loaded successfully.

```
public class InterstitialAdActivity extends Activity {

    private final String TAG = InterstitialAdActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate an InterstitialAd object. 
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
        // Set listeners for the Interstitial Ad
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
}
```

Interstitial Ads have creatives that are larger in size so a good practice is calling `loadAd()` in advance and then calling `show()` at the appropriate time.

#### Scenario 2: Display the ad in a few seconds or minutes after it is successfully loaded. You should check whether the ad has been invalidated before displaying it.

In case of not showing the ad immediately after the ad has been **loaded**, the developer is responsible for checking whether or not the ad has been invalidated. Once the ad is successfully loaded, the ad will be valid for **60 mins**. You will not get **paid** if you are showing an **invalidated** ad.

You should follow the idea below, but please do not copy the code into your project since it is just an example:

```
public class InterstitialAdActivity extends Activity {

    private InterstitialAd  interstitialAd ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instantiate an InterstitialAd object. 
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
        interstitialAd.setAdListener(new InterstitialAdListener() {
            ...
        });
        // load the ad
        interstitialAd.loadAd();
    }

    private void showAdWithDelay() {
       /**
        * Here is an example for displaying the ad with delay;
        * Please do not copy the Handler into your project
       */
       // Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           public void run() {
                // Check if interstitialAd has been loaded successfully
               if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                   return;
               }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
               if(interstitialAd.isAdInvalidated()) {
                   return;
               }
               // Show the ad
                interstitialAd.show(); 
           }
       }, 1000 * 60 * 15); // Show the ad after 15 minutes
    }
}
```

Finally, add the following code to your Activity's `onDestroy()` function to release resources the `InterstitialAd` uses:

```
@Override
protected void onDestroy() {
  if (interstitialAd != null) {
    interstitialAd.destroy();
  }
  super.onDestroy();
}
```

If you are using the default Google Android emulator, you'll add the following line of code before loading a test ad:
`AdSettings.addTestDevice("HASHED ID");`.

Use the hashed ID that is printed to logcat when you first make a request to load an ad on a device.

Genymotion and physical devices do not need this step. If you would like to test with real ads, please consult our [Testing Guide](https://developers.facebook.com/docs/audience-network/testing).

Start your app and you should see an Interstitial Ad appear:

![img](https://app.yinxiang.com/shard/s28/u/0/res/944af480-f13b-4c03-b26b-25acc06c2efb/15164652_275228646212970_1756862411152818176_n.png)

# Hardware Acceleration for Video Ads

Videos ads in Audience Network requires the [hardware accelerated rendering](https://l.facebook.com/l.php?u=https%3A%2F%2Fdeveloper.android.com%2Fguide%2Ftopics%2Fgraphics%2Fhardware-accel.html%3Ffbclid%3DIwAR0Wqi0dgtqPi8AzgeCpDfItFHrC1KC25rZD8Dldkt8siJSKPff9cTbVka0&h=AT2Erpn2LsHMKNdFU7kfEFsXXfyVO-qk-Yp40d7jxnRJ59V7M1FDcZUzqLoxNyvQ-9ZHKsXLqbR1IZB2zrUphyOAQWpXnhlhEm9fAemx22zPIamuYLHEIkorY-OLnBJHIgCLGrxef_rtV1-qS7GkxA) to be enabled, otherwise you might experience a black screen in the video views. This applies to

- Videos creatives in Native Ads
- Videos creatives in Interstitials
- In-stream Video ads
- Rewarded Videos

Hardware acceleration is enabled by default if your Target API level is >=14 (Ice Cream Sandwich, Android 4.0.1), but you can also explicitly enable this feature at the application level or activity level.

## Application Level

In your Android manifest file, add the following attribute to the `` tag to enable hardware acceleration for your entire application:

```
<application android:hardwareAccelerated="true" ...>
```

## Activity Level

If you only want to enable the feature for specific activities in your application, in your Android manifest file, you can add the following feature to the `` tag. The following example will enable the hardware acceleration for the `AudienceNetworkActivity` which is used for rendering interstitial ads and rewarded videos:

```
<activity android:name="com.facebook.ads.AudienceNetworkActivity" android:hardwareAccelerated="true" .../>
```

## Next Steps

- Follow our guides for integrating different Ad Formats in your app:
  - [Native Ads](https://developers.facebook.com/docs/audience-network/android-native)
  - [Interstitial Ads](https://developers.facebook.com/docs/audience-network/android-interstitial)
  - [Banners](https://developers.facebook.com/docs/audience-network/android-banners)

- Explore our [Audience Network Android code samples](https://l.facebook.com/l.php?u=https%3A%2F%2Fgithub.com%2Ffbsamples%2Faudience-network%2Ftree%2Fmaster%2Fsamples%2Fandroid%3Ffbclid%3DIwAR0FXYAl1Xq71X4vyvr1tCRRxa8Modbw8LCsJHiSY8StP6MWBsRCa-W-Uq4&h=AT2gS-Fq_2hWgPriVI_bva1gPpj9BStEJQJHckZjUBeZRO_tpFQd67lu0juvj8E3qb_HUAXbfh8inMdXPRWH8nwqJWQH7ycZiK--fTMOe-e8RLwVInh8ypOuPiQICPfj7-Ya77NlArfbCzRlsQKhkw) on Github. Import the projects to your IDE and run it on either a device or the emulator.
- Once you're ready to go live with your app and monetize, [submit your app for review](https://developers.facebook.com/docs/audience-network/getting-started#onboarding) after ensuring it it complies with [Audience Network policies](https://developers.facebook.com/docs/audience-network/policy) and the [Facebook community standards](https://www.facebook.com/communitystandards).