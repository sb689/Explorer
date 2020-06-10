package com.example.explorer.widget;

import android.app.IntentService;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET = "com.example.explorer.widget.action.update";
    private static final String TAG =  WidgetUpdateWorker.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetUpdateService() {

        super(WidgetUpdateService.class.getSimpleName());
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_WIDGET.equals(action)){
                WidgetUpdateHelper helper = new WidgetUpdateHelper(this);
                helper.getImageForTheDay();


            }
        }

    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }





}
