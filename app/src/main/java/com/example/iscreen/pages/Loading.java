package com.example.iscreen.pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.pages.home.HomeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Loading extends AppCompatActivity {
    private static final String TAG = Loading.class.getSimpleName();

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 4000;
    private ProgressDialog progressDialog;

    private static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_main);

        db = AppDatabase.getInstance(getApplicationContext());
        progressDialog = new ProgressDialog(Loading.this);

        // Set loading...
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Load Current Configuration...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(getLocalConf() == 1){
                    //progressDialog.dismiss();
                    startActivity(new Intent(Loading.this, HomeActivity.class));
                }else{
                    getServerConf();
                    //progressDialog.dismiss();
                    startActivity(new Intent(Loading.this, HomeActivity.class));
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public int getLocalConf(){
        // Testing
        // Test Config
        Configuration config = new Configuration();
        config.setId(1);
        config.setRandom(true);
        config.setRecentProducts(false);
        config.setPromotion(false);
        config.setCategory(-1);
        config.setSplitScreen(true);
        config.setGotConfig(false);
        db.configurationDao().insertConfig(config);

        // add static test products
        db.productDao().deleteAllProduit();
        for (int i=0; i<100; i++){
            ProduitEntry produitEntry = new ProduitEntry();
            //produitEntry.setId((long) i);
            produitEntry.setCategorie_id((long) 0);
            produitEntry.setLabel("Test Label "+(i+1));
            produitEntry.setPrice("12.5");
            produitEntry.setPrice_ttc("12.7");
            produitEntry.setRef("REF-12345_"+i);
            produitEntry.setStock_reel(5);
            produitEntry.setDescription("Ma description");
            produitEntry.setTva_tx("12");
            produitEntry.setNote("@JL Note");
            produitEntry.setNote_public("@JL Note Public");
            produitEntry.setNote_private("@JL Note Private");
            produitEntry.setDate_creation(""+Calendar.getInstance().getTime().getTime());

            db.productDao().insertProduit(produitEntry);
            Log.e(TAG, " product saved: "+i+"\n" +
                    "Date creation: "+produitEntry.getDate_creation());
        }

        Log.e(TAG, " product list size: "+db.productDao().getProducts().size());

        //get local config
        List<Configuration> currentConfig = db.configurationDao().getCurrentConfig();
        String log = "DB size: "+currentConfig.size()+"\n" +
                "ID: " +currentConfig.get(0).getId()+" \n"+
                "Carousel random: " +currentConfig.get(0).isRandom()+" \n"+
                "isGotConfig: "+currentConfig.get(0).getId();
        Log.e(TAG, " "+log);
        return currentConfig.size();
    }

    protected void getServerConf(){

    }

}

