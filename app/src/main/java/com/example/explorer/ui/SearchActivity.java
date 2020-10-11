package com.example.explorer.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.explorer.R;
import com.example.explorer.databinding.ActivitySearchBinding;
import com.example.explorer.widget.WidgetUpdateWorker;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String SAVED_INSTANCE_ASSET_ID_KEY = "asset_id_key";

    public static int mAdViewHeight;
    public static FirebaseAnalytics mFirebaseAnalytics;
    private ActivitySearchBinding mDataBinding;
    private String mAssetId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mDataBinding.adView.loadAd(adRequest);
        Log.d(TAG, "banner added of size, "+ getSmartBannerHeightDp());
        mAdViewHeight = getSmartBannerHeightDp();


        if(getIntent().getAction().equals(getString(R.string.widget_action_detail_view)) && savedInstanceState == null )
        {
            //Trace.beginSection("detail from widget loading");
            Log.d(TAG, "::::::::::::::  intent received");
            mAssetId = getIntent().getStringExtra(getString(R.string.widget_intent_asset_id_key));
            showDetailForAsset(mAssetId);
            //Trace.endSection();

        }
        else if(savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_ASSET_ID_KEY)){
            Log.d(TAG, ":::::::::::::;  recreating widget image detail, no need to add fragment again");

        }
        else if (savedInstanceState == null) {
            Log.d(TAG, ":::::::::::::::::: intent action for SearchActivity is null, loading SearchFragment");
             SearchFragment fragment = SearchFragment.newInstance();
             getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        }
        //schedule work manager to update widget
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(WidgetUpdateWorker.class,
                        24, TimeUnit.HOURS,
                        1, TimeUnit.HOURS)
                        .setConstraints(constraints)

                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                WidgetUpdateWorker.class.getSimpleName(),
                ExistingPeriodicWorkPolicy.REPLACE,
                saveRequest);
    }


    private int getSmartBannerHeightDp() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float screenHeightDp = dm.heightPixels / dm.density;

        return screenHeightDp > 720 ? 90 : screenHeightDp > 400 ? 50 : 32;
    }


    private void showDetailForAsset(String assetId){
        DetailFragment detailFragment = DetailFragment.newInstance(assetId);

        SearchFragment fragment = SearchFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if( mAssetId != null && !mAssetId.isEmpty()){
            outState.putString(SAVED_INSTANCE_ASSET_ID_KEY, mAssetId);
        }
        super.onSaveInstanceState(outState);
    }


    public void resultFound() {
        ListFragment listFragment = ListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, listFragment, null)
                .addToBackStack(listFragment.getClass().getSimpleName())
                .commit();
    }


    public void itemSelectedForDetailView(int position) {
        DetailFragment detailFragment = DetailFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        detailFragment, DetailFragment.TAG)
                .addToBackStack(DetailFragment.TAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding = null;

    }
}
