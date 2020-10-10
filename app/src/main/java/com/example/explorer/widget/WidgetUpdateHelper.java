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
import java.util.Random;

public class WidgetUpdateHelper {

    private static final String TAG = WidgetUpdateHelper.class.getSimpleName();

    private Context mContext;
    private String[] mAssetIds;
    private String[] mImageUrls;
    int dayOfWeek;

    public WidgetUpdateHelper(Context context) {

        this.mContext = context;
        this.mAssetIds = context.getResources().getStringArray(R.array.asset_ids);
        this.mImageUrls = context.getResources().getStringArray(R.array.image_urls);

        Calendar c = Calendar.getInstance();
        this.dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        //test purpose
        Random random = new Random();
        this.dayOfWeek = random.nextInt(8-1)+1;
        Log.d(TAG, "generated dayOfWeek is : " + dayOfWeek );
    }



    public String getImageUrlOfTheDay(Context context) {

       return mImageUrls[dayOfWeek];


    }
    public  String getAssetIdOfTheDay(Context context) {

       return mAssetIds[dayOfWeek];
    }



}
