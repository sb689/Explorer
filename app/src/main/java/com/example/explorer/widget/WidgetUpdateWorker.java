package com.example.explorer.widget;



import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.explorer.R;



public class WidgetUpdateWorker extends Worker {

    private static final String TAG =  WidgetUpdateWorker.class.getSimpleName();



    public WidgetUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "::::::::::called updateWidgetWorker doWork");

        WidgetUpdateHelper helper = new WidgetUpdateHelper(getApplicationContext());
        String assetId = helper.getAssetIdOfTheDay();
        String url = helper.getImageUrlOfTheDay();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), ExplorerWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.iv_widget);
        ExplorerWidgetProvider.updateExplorerWidget( getApplicationContext(), appWidgetManager, appWidgetIds, url, assetId);

        return Result.success();

    }


}
