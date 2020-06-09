package com.example.explorer.widget;


import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;



public class WidgetUpdateWorker extends Worker {

    private static final String TAG =  WidgetUpdateWorker.class.getSimpleName();

    private Context mContext;
    public WidgetUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "::::::::::called updateWidgetWorker doWork");
        WidgetUpdateService.startActionUpdateWidget(mContext);
        return Result.success();

    }




}
