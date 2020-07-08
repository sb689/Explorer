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
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


        String[] assetIds = mContext.getResources().getStringArray(R.array.asset_ids);
        String[] imageUrls = mContext.getResources().getStringArray(R.array.image_urls);

        String assetStr = "";
        String imagesStr = " ";
        for(int i = 0; i< assetIds.length; i++){
           assetStr += assetIds[i] + ", ";
        }
        for(int i = 0; i< imageUrls.length; i++){
            imagesStr += imageUrls[i] + ", ";
        }

        String imageURlStr = imageUrls[dayOfWeek];
        String assetId = assetIds[dayOfWeek];


        //now download the image from network
        Bitmap bitmap =  downloadImageBitmap(imageURlStr);

        Log.d(TAG, "imageURl chosen is = "+ imageURlStr);
        setImageToWidget(bitmap, assetId);
    }


    public void setImageToWidget(Bitmap bitmap, String assetId){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, ExplorerWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.iv_widget);
        ExplorerWidgetProvider.updateExplorerWidget( mContext, appWidgetManager, appWidgetIds, bitmap, assetId);

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
