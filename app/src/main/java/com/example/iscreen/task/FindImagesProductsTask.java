package com.example.iscreen.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.FindImagesProductsListener;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.utility.IScreenUtility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JL on 07/19/2019.
 */

public class FindImagesProductsTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = FindImagesProductsTask.class.getSimpleName();

    private FindImagesProductsListener task;
    private ProduitEntry produitEntry;
    private AppDatabase mDb;

    private Context context;

    public FindImagesProductsTask(Context context, FindImagesProductsListener taskComplete, ProduitEntry produit) {
        this.task = taskComplete;
        this.context = context;
        this.produitEntry = produit;
        this.mDb = AppDatabase.getInstance(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        return downloadBitmapAndSave(ApiUtils.getDownloadProductImg(context, produitEntry.getRef()));
    }

    private String downloadBitmapAndSave(String path) {
        Log.e(TAG, "downloadBitmapAndSave path="+path);
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                String pathFile = IScreenUtility.saveProduitImage(context, bitmap, produitEntry.getRef());
//                Log.e(TAG, "doInBackground:onBitmapLoaded pathFile=" + pathFile+
//                        " pdtRef="+produitEntry.getRef());

                //Modification du path de la photo du produit
                mDb.productDao().updateLocalImgPath(produitEntry.getId(), pathFile);

                return pathFile;
            }
        } catch (Exception e) {
            Log.d(TAG,"URLCONNECTIONERROR "+e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            //Log.w(TAG,"ImageDownloader "+ "Error downloading image from " + path);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String pathFile) {
//        super.onPostExecute(findDolPhotoREST);
        if (task == null) {
            super.onPostExecute(pathFile);
            return;
        }

        task.onFindImagesProductsComplete(pathFile);
    }
}
