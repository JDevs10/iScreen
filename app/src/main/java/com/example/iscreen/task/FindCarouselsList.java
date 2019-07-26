package com.example.iscreen.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.model.Carousel;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.LoadCarousels;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by JL on 07/19/2019.
 */

public class FindCarouselsList extends AsyncTask<Void, Void, Carousel> {
    private final String TAG = FindCarouselsList.class.getSimpleName();
    private Context mContext;

    private int CAROUSEL_ITEM_SIZE;
    private boolean randomProduct;
    private boolean randomCategory;
    private String randomCategoryX;
    private boolean recentProducts;

    private LoadCarousels mLoadCarousels;
    private AppDatabase db;

    public FindCarouselsList(Context context, LoadCarousels mLoadCarousels, int CAROUSEL_ITEM_SIZE,
                             boolean randomProduct, boolean randomCategory, String randomCategoryX, boolean recentProducts){
        this.mContext = context;
        this.mLoadCarousels = mLoadCarousels;
        this.CAROUSEL_ITEM_SIZE = CAROUSEL_ITEM_SIZE;
        this.randomProduct = randomProduct;
        this.randomCategory = randomCategory;
        this.randomCategoryX = randomCategoryX;
        this.recentProducts = recentProducts;
        this.db = AppDatabase.getInstance(this.mContext);
    }

    @Override
    protected Carousel doInBackground(Void... voids) {
        Carousel carousel = null;

        if (randomProduct || randomCategory || !randomCategoryX.equals("-1") || recentProducts){
            carousel = new Carousel();
        }
        if (randomProduct){
            carousel.setRandomProductList(saveRandomProducts());
        }
        if (randomCategory){
            carousel.setRandomFromEachCategoryList(saveRandomFromEachCategory());
        }
        if (randomCategoryX.equals("-1")){
            carousel.setRandomFromSelectedCategoryList(saveProductsFromCategory(randomCategoryX));
        }
        if (recentProducts){
            carousel.setRecentProductList(saveRecentProducts());
        }
        return carousel;
    }

    @Override
    protected void onPostExecute(Carousel carousel) {
        super.onPostExecute(carousel);
        mLoadCarousels.onLoadCarouselsData(carousel);
    }

    private List<ProduitEntry> saveRandomProducts(){
        List<ProduitEntry> allProducts = db.productDao().getProducts();
        int max = allProducts.size();
        List<ProduitEntry> randomProductList = new ArrayList<>();
        ArrayList<Integer> saved = new ArrayList<>();
        Random random = new Random();

        int index = 0;

        while (index < CAROUSEL_ITEM_SIZE){
            int x = random.nextInt(((max-1) - 1) + 1) + 1;

            //Check if the product exist in the filter dataList
            if (saved.size() != 0){
                if (!saved.contains(x)) {
                    saved.add(x);
                    randomProductList.add(allProducts.get(x));
                    index++;
//                    Log.e(TAG, "next => x: "+x+"\n" +
//                            "index: "+index+"\n" +
//                            "saved size: "+saved.size());
                }
            }else{
                saved.add(x);
                randomProductList.add(allProducts.get(x));
                index++;
//                Log.e(TAG, "first => x: "+x+"\n" +
//                        "index: "+index+"\n" +
//                        "saved size: "+saved.size());
            }
        }
        return randomProductList;
    }

    private List<ProduitEntry> saveRandomFromEachCategory(){
        List<ProduitEntry> allProducts = db.productDao().getProducts();
        //allProducts = db.productDao().getProductsByCategory(categotyId);
        List<ProduitEntry> randomFromSelectedCategoryList = new ArrayList<>();
        ArrayList<Integer> saved = new ArrayList<>();
        Random random = new Random();
        int max = allProducts.size();

        int index = 0;

        while (index < CAROUSEL_ITEM_SIZE){
            int x = random.nextInt(((max-1) - 1) + 1) + 1;

            //Check if the product exist in the filter dataList
            if (saved.size() != 0){
                if (!saved.contains(x)) {
                    saved.add(x);
                    randomFromSelectedCategoryList.add(allProducts.get(x));
                    index++;
//                    Log.e(TAG, "next => x: "+x+"\n" +
//                            "index: "+index+"\n" +
//                            "saved size: "+saved.size());
                }
            }else{
                saved.add(x);
                randomFromSelectedCategoryList.add(allProducts.get(x));
                index++;
//                Log.e(TAG, "first => x: "+x+"\n" +
//                        "index: "+index+"\n" +
//                        "saved size: "+saved.size());
            }
        }
        return randomFromSelectedCategoryList;
    }

    private List<ProduitEntry> saveProductsFromCategory(String categoryID){
        return db.productDao().getProductsByCategory(Long.valueOf(categoryID));
    }

    private List<ProduitEntry> saveRecentProducts(){
        List<ProduitEntry> recentProductList = new ArrayList<>();
        for (int i=0; i<db.productDao().loadRecentProduct().size(); i++){
            recentProductList.add(db.productDao().loadRecentProduct().get(i));
            if ( (i+1) == CAROUSEL_ITEM_SIZE){
                break;
            }
        }
        return recentProductList;
    }

}
