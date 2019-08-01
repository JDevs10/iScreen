package com.example.iscreen.remote;

import android.content.Context;
import android.util.Log;

import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.ServerEntry;
import com.example.iscreen.database.entity.TokenEntry;

/**
 * Created by JL on 07/19/2019.
 */

public final class ApiUtils {
    private static final String TAG = ApiUtils.class.getSimpleName();

    //    body query properties
    public static final String DOLAPIKEY = "DOLAPIKEY";
    public static final String sortfield = "sortfield";
    public static final String sortorder = "sortorder";
    public static final String sqlfilters = "sqlfilters";
    public static final String limit = "limit";
    public static final String page = "page";
    public static final String mode = "mode";
    public static final String category = "category";
    public static final String type = "type";
    public static final String id = "id";

    private ApiUtils() {
    }

    //get an instance of Movies API services
    public static IScreenServicesRemote getIScreenService(Context context) {
        AppDatabase mDb = AppDatabase.getInstance(context.getApplicationContext());

        ServerEntry serverEntry = mDb.serverDao().getActiveServer(true);

        if (serverEntry == null) {
            return RetrofitClient.getClient(context, "/").create(IScreenServicesRemote.class);
        }
//        Log.e(TAG, "getIScreenService: serverEntry="+serverEntry.getHostname());
//        return RetrofitClient.getClient(context, BASE_URL).create(IScreenServicesRemote.class);
        return RetrofitClient.getClient(context, serverEntry.getHostname()+"/").create(IScreenServicesRemote.class);
    }

    // Renvoi l'url de recuperation des images de produit
    public static String getDownloadProductImg(Context context, String ref) {
        AppDatabase mDb = AppDatabase.getInstance(context.getApplicationContext());
        TokenEntry tokenEntry = mDb.tokenDao().getAllToken().get(0);
        ServerEntry serverEntry = mDb.serverDao().getActiveServer(true);
//        http://localhost:8888/Images.iSales/download.php?module_part=produit&original_file=cheese_cake/cheese_cake-Cheese_cake.jpg&DOLAPIKEY=9c524dc13288320153128086e6e69144fa743be3
        String url = String.format("%s/product.php?ref=%s&DOLAPIKEY=%s", serverEntry.getHostname_img(), ref, tokenEntry.getToken());
//        Log.e(TAG, "getDownloadImg: url="+url);
        return url;
    }

}
