package com.example.explorer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import com.example.explorer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    public static final String ANALYTICS_SEARCH_KEY = "search_key";
    public static final String ANALYTICS_SELECTED_RESPONSE = "search_item_selected";


    public static int mPosition;
    private AdView mAdView;;
    private FrameLayout adContainerView;
    public static FirebaseAnalytics mFirebaseAnalytics;

    public static int getmPosition() {
        return mPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState == null) {
            SearchFragment fragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, SearchFragment.TAG).commit();
        }

    }


    public void showSearchResponseList(){

        ListFragment listFragment = new ListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("listFragment")
                .replace(R.id.fragment_container,
                        listFragment, null).commit();
    }

    public void showDetail(int position){
        Log.d(TAG, "item clicked received in showDetail" );
        mPosition = position;
        DetailFragment detailFragment = new DetailFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("detailFragment")
                .replace(R.id.fragment_container,
                        detailFragment).commit();

    }

//    private void loadBanner() {
//        // Create an ad request.
//        adView = (AdView) findViewById(R.id.adView);
//        adView.setAdUnitId(getString(R.string.test_banner_unit_id));
//        //adContainerView.removeAllViews();
////        adContainerView.addView(adView);
//
//        AdSize adSize = getAdSize();
//        adView.setAdSize(adSize);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Start loading the ad in the background.
//        adView.loadAd(adRequest);
//    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            return AdSize.getPortraitInlineAdaptiveBannerAdSize(this, adWidth);
        }else{
            return AdSize.getLandscapeInlineAdaptiveBannerAdSize(this, adWidth);
        }

    }

}
