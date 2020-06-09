package com.example.explorer.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.explorer.R;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

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
              getImageForTheDay();
            }
        }

    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }


    private void getImageForTheDay() {

        Log.d(TAG, "getImageFOrTHeDayCalled");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String imageURlStr = "";

        switch (dayOfWeek){
            case 1: imageURlStr =  getString(R.string.image_sunday);

                break;
            case 2: imageURlStr = getString(R.string.image_monday);
                break;
            case 3: imageURlStr = getString(R.string.image_tuesday);
                break;
            case 4: imageURlStr =  getString(R.string.image_wednesday);
                break;
            case 5: imageURlStr =  getString(R.string.image_thursday);
                break;
            case 6: imageURlStr =  getString(R.string.image_friday);
                break;
            case 7: imageURlStr = getString(R.string.image_saturday);
                break;
            default: imageURlStr =  getString(R.string.image_default);
        }

        //now download the image from network
        Bitmap bitmap =  downloadImageBitmap(imageURlStr);

        Log.d(TAG, "imageURl chosen is = "+ imageURlStr);
        setImageToWidget(bitmap);
    }


    public void setImageToWidget(Bitmap bitmap){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ExplorerWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.iv_widget);
        ExplorerWidget.updateExplorerWidget(this, appWidgetManager, appWidgetIds, bitmap);

    }


    private Bitmap downloadImageBitmap(String sUrl) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
            bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
            inputStream.close();
        } catch (Exception e) {
            Log.d(TAG, "exception while downloading bitmap");
            e.printStackTrace();
        }

        return bitmap;
    }


}
