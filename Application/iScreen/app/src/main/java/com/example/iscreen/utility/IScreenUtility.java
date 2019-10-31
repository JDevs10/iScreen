package com.example.iscreen.utility;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
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
 * Created by netserve on 30/08/2018.
 * Updates by JDevs
 */

public final class IScreenUtility {
    private static final String TAG = IScreenUtility.class.getSimpleName();
    public static String ENCODE_IMG = "&img";
    public static String ENCODE_DESC = "&desc";
    private static String ENCODE_CAROUSEL = "&amp;carousel;";

    public static String CURRENCY = "€";
    public static String ISALES_PATH_FOLDER = "iScreen";
    public static String ISALES_PRODUCTS_IMAGESPATH_FOLDER = "iScreen/iScreen Produits";

    //Full Screen Mode
    //Need to be call for each Activity and not fragments
    public void fullScreenMode(Context context, FragmentActivity fragmentActivity){
        AppDatabase db = AppDatabase.getInstance(context);
        boolean status = db.configurationDao().getCurrentConfig().get(0).isFullScreenMode();

        WindowManager.LayoutParams attrs = fragmentActivity.getWindow().getAttributes();
        if (status) {
            //Full screen mode activaed
            ((AppCompatActivity) fragmentActivity).getSupportActionBar().hide();
            fragmentActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            fragmentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }else {
            //Full screen mode desactivaed
            ((AppCompatActivity) fragmentActivity).getSupportActionBar().show();
            fragmentActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        fragmentActivity.getWindow().setAttributes(attrs);
    }

    public void darkMode(Context context, LinearLayout linearLayout){
        linearLayout.setBackgroundColor(Color.BLACK);
    }

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

    // Renvoi le nom de l'image du produit a partir de la description
    public static String getDescProduit(String description) {
        return description;

        /*if (description == null || description.isEmpty()) {
            return null;
        }

//        Log.e(TAG, "getDescProduit: description="+description);
        // extraction de la chaine apres code encodage de la photo
        String[] descriptionTab = description.split(ENCODE_DESC);
//        Log.e(TAG, "getDescProduit: descriptionTab before length="+descriptionTab.length);
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

//        return descriptionTab[1];
    }

    public static String amountFormat2(String value) {

        double valueDouble = Double.parseDouble(value.replace(",","."));
        String str = String.format(Locale.FRANCE,
                "%,-10.2f", valueDouble);
        return String.valueOf(str);
    }

    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * returns the bytesize of the give bitmap
     */
    public static int bitmapByteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    public static String getFilename(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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