package com.example.explorer.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.explorer.R;
import com.example.explorer.ui.SearchActivity;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class ExplorerWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ExplorerWidgetProvider.class.getSimpleName();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, String imageUrl, String assetId) {

        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.explorer_widget);
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(context.getString(R.string.widget_intent_asset_id_key), assetId);
        intent.setAction(context.getString(R.string.widget_action_detail_view));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d(TAG, "updateAppWidget");
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                Picasso.get().load(imageUrl).into(views, R.id.iv_widget, appWidgetIds);
            }
        });

        views.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "called onUpdate in ExplorerWidgetProvider");
       // WidgetUpdateService.startActionUpdateWidget(context);
        WidgetUpdateHelper helper = new WidgetUpdateHelper(context);
        String assetId = helper.getAssetIdOfTheDay();
        String url = helper.getImageUrlOfTheDay();
        Log.d(TAG, "imageUrl onUpdate is : "+ url);
        updateExplorerWidget(context, appWidgetManager, appWidgetIds, url, assetId);
    }


    public static void updateExplorerWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                            String imageUrl, String assetId){
         updateAppWidget(context, appWidgetManager, appWidgetIds, imageUrl, assetId);

    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

