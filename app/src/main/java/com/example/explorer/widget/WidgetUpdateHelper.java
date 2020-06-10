package com.example.explorer.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.explorer.R;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

public class WidgetUpdateHelper {

    private static final String TAG = WidgetUpdateHelper.class.getSimpleName();

    private Context mContext;

    public WidgetUpdateHelper(Context mContext) {
        this.mContext = mContext;
    }

    public  void getImageForTheDay() {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String imageURlStr = "";

        switch (dayOfWeek){
            case 1: imageURlStr =   mContext.getString(R.string.image_sunday);

                break;
            case 2: imageURlStr =  mContext.getString(R.string.image_monday);
                break;
            case 3: imageURlStr =  mContext.getString(R.string.image_tuesday);
                break;
            case 4: imageURlStr =   mContext.getString(R.string.image_wednesday);
                break;
            case 5: imageURlStr =   mContext.getString(R.string.image_thursday);
                break;
            case 6: imageURlStr =   mContext.getString(R.string.image_friday);
                break;
            case 7: imageURlStr =  mContext.getString(R.string.image_saturday);
                break;
            default: imageURlStr =   mContext.getString(R.string.image_default);
        }

        //now download the image from network
        Bitmap bitmap =  downloadImageBitmap(imageURlStr);

        Log.d(TAG, "imageURl chosen is = "+ imageURlStr);
        setImageToWidget(bitmap);
    }


    public void setImageToWidget(Bitmap bitmap){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, ExplorerWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.iv_widget);
        ExplorerWidgetProvider.updateExplorerWidget( mContext, appWidgetManager, appWidgetIds, bitmap);

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
