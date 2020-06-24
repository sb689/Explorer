package com.example.explorer.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.explorer.R;
import com.example.explorer.databinding.ActivitySearchBinding;

import com.example.explorer.widget.WidgetUpdateWorker;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();



    public static int mPosition;
    private AdView mAdView;;
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

            //schedule widget updater
            PeriodicWorkRequest dailyUpdateRequest =
                    new PeriodicWorkRequest.Builder(WidgetUpdateWorker.class, 3, TimeUnit.MINUTES)
                            .build();
            WorkManager.getInstance(getApplicationContext())
                    .enqueueUniquePeriodicWork(WidgetUpdateWorker.class.getName(),
                            ExistingPeriodicWorkPolicy.REPLACE,
                            dailyUpdateRequest);

            SearchFragment fragment = new SearchFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        }


    }


    public void showSearchResponseList(){

        ListFragment listFragment = new ListFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, listFragment, null)
                .addToBackStack(listFragment.getClass().getSimpleName())
                .commit();
    }


    public void showDetail(int position){

        mPosition = position;
        DetailFragment detailFragment = DetailFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        detailFragment, detailFragment.TAG)
                .addToBackStack(detailFragment.getClass().getSimpleName())
                .commit();
    }


    public void nextClicked(){
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.TAG);
        fragment.showNextItem();
    }


    public void backClicked(){
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.TAG);
      fragment.showPrevItem();
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
