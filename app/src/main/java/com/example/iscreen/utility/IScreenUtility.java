package com.example.iscreen.utility;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.iscreen.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by JL on 07/19/2019.
 */

public final class IScreenUtility {
    private static final String TAG = IScreenUtility.class.getSimpleName();

    public static String ENCODE_IMG = "&img";
    public static String ISALES_PRODUCTS_IMAGESPATH_FOLDER = "iScreen/iScreen Produits";

    // Renvoi le nom de l'image du produit a partir de la description
    public static String getImgProduit(String description) {
        if (description == null || description.isEmpty()) {
            return null;
        }

//        Log.e(TAG, "getImgProduit: description="+description);
        // extraction de la chaine apres code encodage de la photo
        String[] descriptionTab = description.split(ENCODE_IMG);
//        Log.e(TAG, "getImgProduit: descriptionTab before length="+descriptionTab.length);
        // S'il n'ya pas d'encode de img, on renvoi null
        if (descriptionTab.length < 2) {
            return null;
        }
//        Log.e(TAG, "getImgProduit: descriptionTab after length="+descriptionTab.length);
        // extraction de la chaine avant code ':&'
        /*String[] imgTab = descriptionTab[1].split(":&");
        if (imgTab.length <= 1) {
            return null;
        }*/
        // console.log(this.TAG, "descriptionTab:getImgProduit ", descriptionTab);
        // console.log(this.TAG, "descriptionTab:imgTab ", imgTab);
        return descriptionTab[1];
    }

    public static final void makeSureFileWasCreatedThenMakeAvailable(Context context, File file) {
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
//                        Log.e(TAG, "onScanCompleted: Scanned=" + path);
//                        Log.e(TAG, "onScanCompleted: uri=" + uri);
                    }
                });
    }

    private static final String getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatted = simpleDateFormat.format(calendar.getTime());
        return formatted;
    }

//    enregistre la photo d'un produit en loca
    public static final String saveProduitImage(Context context, Bitmap imageToSave, String filename) {
        String currentDateAndTime = getCurrentDateAndTime();
        File dir = new File(Environment.getExternalStorageDirectory(), ISALES_PRODUCTS_IMAGESPATH_FOLDER);
        if (!dir.exists()) {
            if (dir.mkdirs()){
                Log.e(TAG, "saveProduitImage: folder created" );
            }
        }

        File file = new File(dir, String.format("%s.jpg", filename, currentDateAndTime));

        if (file.exists ()) file.delete();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            makeSureFileWasCreatedThenMakeAvailable(context, file);

            return file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "saveProduitImage:FileNotFoundException "+e.getMessage() );
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "saveProduitImage:IOException "+e.getMessage() );
            return null;
        }

    }

    public static final void deleteProduitsImgFolder() {
        File myDir = new File(Environment.getExternalStorageDirectory(), ISALES_PRODUCTS_IMAGESPATH_FOLDER);
        if (myDir.isDirectory() && myDir.list() != null) {
            String[] children = myDir.list();
            for (int i = 0; i < children.length; i++) {
                new File(myDir, children[i]).delete();
            }
        }
    }
}