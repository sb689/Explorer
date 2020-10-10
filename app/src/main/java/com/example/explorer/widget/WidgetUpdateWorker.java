package com.example.explorer.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.explorer.R;
import com.example.explorer.ui.SearchActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;


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
        String assetId = helper.getAssetIdOfTheDay(getApplicationContext());
        String url = helper.getImageUrlOfTheDay(getApplicationContext());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), ExplorerWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.iv_widget);
        ExplorerWidgetProvider.updateExplorerWidget( getApplicationContext(), appWidgetManager, appWidgetIds, url, assetId);

        return Result.success();

    }


}
