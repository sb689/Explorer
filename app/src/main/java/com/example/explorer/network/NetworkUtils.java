package com.example.explorer.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static final int ERROR_TAG_NO_NETWORK = 1;
    public static final int ERROR_TAG_GENERAL = 2;
    public static final int ERROR_TAG_NO_DATA_FOUND = 3;

    public static boolean isNetworkConnected(Context context){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
    }
}
