package com.example.iscreen.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by netserve on 27/08/2018.
 */

public final class ConnectionManager {

    public static boolean isPhoneConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) &&
                activeNetwork.isConnectedOrConnecting();
    }
}
