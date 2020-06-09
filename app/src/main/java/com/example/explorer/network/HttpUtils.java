package com.example.explorer.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.explorer.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class HttpUtils {

    private static final  String TAG = HttpUtils.class.getSimpleName();

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildUrl(String query, String yearStart, String yearEnd, Context context)
    {

        String baseAPIUrl = context.getString(R.string.base_api_url);

        Uri.Builder builder = Uri.parse(baseAPIUrl).buildUpon();
        if(query != null){
            builder.appendQueryParameter(context.getString(R.string.param_key_query), query);
        }
        if(yearStart != null){
            builder.appendQueryParameter(context.getString(R.string.param_key_year_start), yearStart);
        }
        if(yearEnd != null){
            builder.appendQueryParameter(context.getString(R.string.param_key_year_end), yearEnd);
        }

        Uri uri = Uri.parse(baseAPIUrl).buildUpon()
                .appendQueryParameter(context.getString(R.string.param_key_media_type),
                        context.getString(R.string.param_value_media_type))
                .build();
        Log.d(TAG, "builtUri ::::::::::::::::::::::::" + uri.toString());

        return uri.toString();

    }


}
