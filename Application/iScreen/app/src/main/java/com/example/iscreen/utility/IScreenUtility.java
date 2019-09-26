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
 * Created by netserve on 30/08/2018.
 */

public final class IScreenUtility {
    private static final String TAG = IScreenUtility.class.getSimpleName();
    public static String ENCODE_IMG = "&img";
    public static String ENCODE_DESC = "&desc";
    private static String ENCODE_CAROUSEL = "&amp;carousel;";

    public static String CURRENCY = "€";
    public static String ISALES_PATH_FOLDER = "iScreen";
    public static String ISALES_PRODUCTS_IMAGESPATH_FOLDER = "iScreen/iScreen Produits";

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 296);
        return noOfColumns;
    }

    public static int calculateNoOfColumnsCmde(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 320);
        return noOfColumns;
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

    // Renvoi le nom les images carousel du produit a partir de la description
    public static String[] getCarouselProduit(String description) {

        String[] carousel;
        // extraction de la chaine apres code encodage de la photo
        String[] descriptionTab = description.split(ENCODE_CAROUSEL);
        Log.e(TAG, "descriptionTab:getCarouselProduit " + descriptionTab.toString());
        // S'il n'ya pas d'encode de img, on renvoi null
        if (descriptionTab.length <= 1) {
            return null;
        }
        // extraction de la chaine avant code ':'
        String[] carouselTab = descriptionTab[1].split(":&");
        Log.e(TAG, "descriptionTab:carouselTab " + carouselTab.toString());
        if (carouselTab.length <= 1) {
            return null;
        }
        carousel = carouselTab[0].split(":");
        return carousel;
    }

    //    Arromdi les bord d'une image bitmap
    public static Bitmap getRoundedCornerBitmap(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    public static String strCapitalize(String str) {
        String name = str;
        if (name.length() >= 2) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        return name;
    }

    public static String amountFormat2(String value) {

        double valueDouble = Double.parseDouble(value.replace(",","."));
        String str = String.format(Locale.FRANCE,
                "%,-10.2f", valueDouble);
        return String.valueOf(str);
    }

    public static String roundOffTo2DecPlaces(String value) {
        double valueDouble = Double.parseDouble(value);
        return String.format("%.2f", valueDouble);
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

    //enregistre la photo d'un produit en loca
    public static final Target getTargetSaveProduitImage(final String filename) {
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

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
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
                            fos.flush();
                            fos.close();
//                            makeSureFileWasCreatedThenMakeAvailable(context, file);

//                            return file.getAbsolutePath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.e(TAG, "saveProduitImage:FileNotFoundException "+e.getMessage() );
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "saveProduitImage:IOException "+e.getMessage() );
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

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