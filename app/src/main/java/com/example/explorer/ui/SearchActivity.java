package com.example.explorer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import com.example.explorer.R;
import com.example.explorer.databinding.ActivitySearchBinding;
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
    private ActivitySearchBinding mDataBinding;

    public static int getmPosition() {
        return mPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

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

        mDataBinding.ivFrontArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextClicked();
            }
        });

        mDataBinding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClicked();
            }
        });

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

    public void nextClicked(){

        int position = DetailFragment.mPosition;

        if(position + 1 > DetailFragment.mItemList.size()-1){
            position = 0;
        }else{
            position = position + 1;
        }

        Log.d(TAG, "loading detail ui in nextClicked for position = " + mPosition);

        DetailFragment.loadDetailUI(position);
    }

    public void backClicked(){

        int position = DetailFragment.mPosition;

        if(position - 1 < 0){
            position = DetailFragment.mItemList.size() -1;
        }
        else{
            position = position -1;
        }
        Log.d(TAG, "loading detail ui in backClicked for position = " + mPosition);
        DetailFragment.loadDetailUI(position);
    }

    public void hideNavigationButtons(){
        mDataBinding.ivFrontArrow.setVisibility(View.GONE);
        mDataBinding.ivBackArrow.setVisibility(View.GONE);
    }

    public void showNavigationButtons(){
        mDataBinding.ivFrontArrow.setVisibility(View.VISIBLE);
        mDataBinding.ivBackArrow.setVisibility(View.VISIBLE);
        mDataBinding.ivFrontArrow.requestFocus();

    }

}
