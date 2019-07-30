package com.example.iscreen.pages.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.adapter.RandomProductAdapter;
import com.example.iscreen.adapter.RecentProductAdapter;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.model.Carrousel;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.LoadCarousels;
import com.example.iscreen.task.FindCarouselsList;

import java.util.List;

public class Affichage extends Fragment implements LoadCarousels {
    private final String TAG = Affichage.class.getSimpleName();
    private Context mContext;
    private boolean setupCarouselData = true;
    private boolean isCarouselSlide;

    private RecyclerView random_rv;
    private RecyclerView randomCat_rv;
    private RecyclerView randomCat_X_rv;
    private RecyclerView recentProducts_rv;

    private TextView randomTitle, randomCatTitle, randomCat_XTitle, recentProductsTitle;

    private ProgressDialog progressDialog;
    private AppDatabase db;
    private List<ProduitEntry> allProducts;

    final int durationRandom_rv = 10;
    final int durationRandomCat_rv = 15;
    final int durationRecentProducts_rv = 10;
    final int pixelsToMove = 30;

    private final Handler mHandlerRandom_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_random_rv = new Runnable() {
        @Override
        public void run() {
            random_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRandom_rv.postDelayed(this, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
        }
    };

    private final Handler mHandlerRandomCat_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_randomCat_rv = new Runnable() {
        @Override
        public void run() {
            randomCat_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRandomCat_rv.postDelayed(this, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
        }
    };

    private final Handler mHandlerRandomCat_X_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_randomCat_X_rv = new Runnable() {
        @Override
        public void run() {
            randomCat_X_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRandomCat_X_rv.postDelayed(this, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
        }
    };

    private final Handler mHandlerRecentProducts_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_recentProducts_rv = new Runnable() {
        @Override
        public void run() {
            recentProducts_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRecentProducts_rv.postDelayed(this, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = AppDatabase.getInstance(mContext);
        progressDialog = new ProgressDialog(this.mContext);

        isCarouselSlide = db.configurationDao().getCurrentConfig().get(0).isCarouselSlide();
        Log.e(TAG, " isCarouselSlide => "+isCarouselSlide);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        random_rv = view.findViewById(R.id.fragment_catalog_randomRecycler);
        randomCat_rv = view.findViewById(R.id.fragment_catalog_randomCategoryRecycler);
        randomCat_X_rv = view.findViewById(R.id.fragment_catalog_categoryProductsRecycler);
        recentProducts_rv = view.findViewById(R.id.fragment_catalog_lastProductRecycler);

        randomTitle = (TextView) view.findViewById(R.id.fragment_catalog_randomTitle);
        randomCatTitle = (TextView) view.findViewById(R.id.fragment_catalog_randomCategoryTitle);
        randomCat_XTitle = (TextView) view.findViewById(R.id.fragment_catalog_categoryProductsTitle);
        recentProductsTitle = (TextView) view.findViewById(R.id.fragment_catalog_lastProductTitle);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupCarrouselData();
    }

    private void showProgressDialog(boolean show, String title, String message) {

        if (show) {
            progressDialog = new ProgressDialog(mContext);
            if (title != null) progressDialog.setTitle(title);
            if (message != null) progressDialog.setMessage(message);

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } else {
            if (progressDialog != null) progressDialog.dismiss();
        }
    }

    private void setupCarrouselData(){
        showProgressDialog(setupCarouselData, "Produit", "Chargement des carousels...");
        FindCarouselsList findCarouselsList = new FindCarouselsList(
                mContext,
                Affichage.this,
                db.configurationDao().getCurrentConfig().get(0).getCarouselSize(),
                db.configurationDao().getCurrentConfig().get(0).isRandomProduct(),
                db.configurationDao().getCurrentConfig().get(0).isRandomCategory(),
                db.configurationDao().getCurrentConfig().get(0).getRandomCategoryX(),
                db.configurationDao().getCurrentConfig().get(0).isRecentProducts());
        findCarouselsList.execute();
    }

    @Override
    public void onLoadCarouselsData(Carrousel carrousel) {
        getRandomProducts(carrousel.getRandomProductList());
        getRandomFromEachCategory(carrousel.getRandomFromEachCategoryList());
        getRandomFromCategoryX(carrousel.getRandomFromSelectedCategoryList(), carrousel.getSelectedCategoryName());
        getRecentProducts(carrousel.getRecentProductList());

        setupCarouselData = false;
        showProgressDialog(setupCarouselData, null, null);
    }

    private void recycleViewAnimation(List<ProduitEntry> productList, final RecyclerView theRecyclerView, final RecyclerView.Adapter adapter, final LinearLayoutManager llm, final Runnable runnable) {
        if (productList == null || productList.size() != 0) {
            theRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = llm.findLastCompletelyVisibleItemPosition();
                    if (lastItem == llm.getItemCount() - 1) {
                        mHandlerRandom_rv.removeCallbacks(runnable);
                        Handler postHandler = new Handler();
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(null);
                                recyclerView.setAdapter(adapter);
                                mHandlerRandom_rv.postDelayed(runnable, 2000);
                            }
                        }, 2000);
                    }
                }
            });
            mHandlerRandom_rv.postDelayed(runnable, 2000);
        }
    }

    private void getRandomProducts(List<ProduitEntry> randomProductList){
        if (db.configurationDao().getCurrentConfig().get(0).isRandomProduct()) {
            if (randomProductList != null || randomProductList.size() != 0) {
                randomTitle.setText("Produits aléatoires");

                final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomProductList);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                random_rv.setLayoutManager(mLinearLayoutManager);
                random_rv.setAdapter(randomProductAdapter);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomProductList, random_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_random_rv);
                }
            }
        }else{
            randomTitle.setVisibility(View.GONE);
            random_rv.setVisibility(View.GONE);
        }
    }

    private void getRandomFromEachCategory(List<ProduitEntry> randomFromSelectedCategoryList){
        if (db.configurationDao().getCurrentConfig().get(0).isRandomCategory()) {
            if (randomFromSelectedCategoryList != null || randomFromSelectedCategoryList.size() != 0) {
                randomCatTitle.setText("Produit de chaque categorie");

                final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomFromSelectedCategoryList);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                randomCat_rv.setLayoutManager(mLinearLayoutManager);
                randomCat_rv.setAdapter(randomProductAdapter);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomFromSelectedCategoryList, randomCat_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_randomCat_rv);
                }
            }
        }else{
            randomCatTitle.setVisibility(View.GONE);
            randomCat_rv.setVisibility(View.GONE);
        }
    }

    private void getRandomFromCategoryX(List<ProduitEntry> randomFromCategoryXList, String categoryName){
        if (!db.configurationDao().getCurrentConfig().get(0).getRandomCategoryX().equals("-1")) {
            if (randomFromCategoryXList != null || randomFromCategoryXList.size() != 0) {
                randomCat_XTitle.setText("Produit de la categorie : " + categoryName);

                final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomFromCategoryXList);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                randomCat_X_rv.setLayoutManager(mLinearLayoutManager);
                randomCat_X_rv.setAdapter(randomProductAdapter);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomFromCategoryXList, randomCat_X_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_randomCat_X_rv);
                }
            }
        }else{
            randomCat_XTitle.setVisibility(View.GONE);
            randomCat_X_rv.setVisibility(View.GONE);
        }
    }

    private void getRecentProducts(List<ProduitEntry> recentProductList){
        if (db.configurationDao().getCurrentConfig().get(0).isRecentProducts()) {
            if (recentProductList != null || recentProductList.size() != 0) {
                recentProductsTitle.setText("Produit récente");

                final RecentProductAdapter recentProductAdapter = new RecentProductAdapter(this.mContext, recentProductList);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recentProducts_rv.setLayoutManager(mLinearLayoutManager);
                recentProducts_rv.setAdapter(recentProductAdapter);

                if (isCarouselSlide) {
                    recycleViewAnimation(recentProductList, recentProducts_rv, recentProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_recentProducts_rv);
                }
            }
        }else{
            recentProductsTitle.setVisibility(View.GONE);
            recentProducts_rv.setVisibility(View.GONE);
        }
    }

}
