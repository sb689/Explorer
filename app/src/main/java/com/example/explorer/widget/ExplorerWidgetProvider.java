package com.example.explorer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.explorer.R;
import com.example.explorer.ui.SearchActivity;

/**
 * Implementation of App Widget functionality.
 */
public class ExplorerWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ExplorerWidgetProvider.class.getSimpleName();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Bitmap bitmap, String assetId) {

        // Construct the RemoteViews object
        Log.d(TAG, "inside updateAppWidget, setting widget image");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.explorer_widget);
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(context.getString(R.string.widget_intent_asset_id_key), assetId);
        intent.setAction(context.getString(R.string.widget_action_detail_view));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                0);

        if(bitmap != null) {
            views.setImageViewBitmap(R.id.iv_widget, bitmap);
        }
        views.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }

        WidgetUpdateService.startActionUpdateWidget(context);
    }

    public static void updateExplorerWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                            Bitmap bitmap, String assetId){

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, bitmap, assetId);
        }
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

