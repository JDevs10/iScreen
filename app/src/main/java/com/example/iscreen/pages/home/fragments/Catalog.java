package com.example.iscreen.pages.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.example.iscreen.R;
import com.example.iscreen.adapter.RandomProductAdapter;
import com.example.iscreen.adapter.RecentProductAdapter;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.model.Carousel;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.LoadCarousels;
import com.example.iscreen.task.FindCarouselsList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Catalog extends Fragment implements LoadCarousels {
    private final String TAG = Catalog.class.getSimpleName();
    private Context mContext;
    private boolean setupCarouselData = true;
    private boolean isCarouselSlide;

    private RecyclerView random_rv;
    private RecyclerView randomCat_rv;
    private RecyclerView randomCat_X_rv;
    private RecyclerView recentProducts_rv;

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
        randomCat_X_rv = view.findViewById(R.id.fragment_catalog_categoryProducts);
        recentProducts_rv = view.findViewById(R.id.fragment_catalog_lastProductRecycler);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupCarouselData();
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

    private void setupCarouselData(){
        showProgressDialog(setupCarouselData, "Produit", "Chargement des carousels...");
        FindCarouselsList findCarouselsList = new FindCarouselsList(
                mContext,
                Catalog.this,
                db.configurationDao().getCurrentConfig().get(0).getCarouselSize(),
                db.configurationDao().getCurrentConfig().get(0).isRandomProduct(),
                db.configurationDao().getCurrentConfig().get(0).isRandomCategory(),
                db.configurationDao().getCurrentConfig().get(0).getRandomCategoryX(),
                db.configurationDao().getCurrentConfig().get(0).isRecentProducts());
        findCarouselsList.execute();
    }

    @Override
    public void onLoadCarouselsData(Carousel carousel) {
        getRandomProducts(carousel.getRandomProductList());
        getRandomFromEachCategory(carousel.getRandomFromEachCategoryList());
        getRandomFromCategoryX(carousel.getRandomFromSelectedCategoryList());
        getRecentProducts(carousel.getRecentProductList());

        setupCarouselData = false;
        showProgressDialog(setupCarouselData, null, null);
    }

    private void recycleViewAnimation(List<ProduitEntry> productList, final RecyclerView theRecyclerView, final RecyclerView.Adapter adapter, final LinearLayoutManager llm, final Runnable runnable) {
        if (productList.size() != 0) {
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

        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomProductList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        random_rv.setLayoutManager(mLinearLayoutManager);
        random_rv.setAdapter(randomProductAdapter);

        if (isCarouselSlide) {
            recycleViewAnimation(randomProductList, random_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_random_rv);
        }
        /*
        if (randomProductList.size() != 0) {
            //recycleViewAnimation(random_rv, randomProductAdapter, mLinearLayoutManager, 1000);
            random_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastItem == mLinearLayoutManager.getItemCount() - 1) {
                        mHandlerRandom_rv.removeCallbacks(SCROLLING_RUNNABLE_random_rv);
                        Handler postHandler = new Handler();
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                random_rv.setAdapter(null);
                                random_rv.setAdapter(randomProductAdapter);
                                mHandlerRandom_rv.postDelayed(SCROLLING_RUNNABLE_random_rv, 2000);
                            }
                        }, 2000);
                    }
                }
            });
            mHandlerRandom_rv.postDelayed(SCROLLING_RUNNABLE_random_rv, 2000);
        }
        */
    }

    private void getRandomFromEachCategory(List<ProduitEntry> randomFromSelectedCategoryList){

        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomFromSelectedCategoryList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        randomCat_rv.setLayoutManager(mLinearLayoutManager);
        randomCat_rv.setAdapter(randomProductAdapter);

        if (isCarouselSlide) {
            recycleViewAnimation(randomFromSelectedCategoryList, randomCat_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_randomCat_rv);
        }
        /*
        if (randomFromSelectedCategoryList.size() != 0) {
            randomCat_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastItem == mLinearLayoutManager.getItemCount() - 1) {
                        mHandlerRandomCat_rv.removeCallbacks(SCROLLING_RUNNABLE_randomCat_rv);
                        Handler postHandler = new Handler();
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                randomCat_rv.setAdapter(null);
                                randomCat_rv.setAdapter(randomProductAdapter);
                                mHandlerRandomCat_rv.postDelayed(SCROLLING_RUNNABLE_randomCat_rv, 2000);
                            }
                        }, 2000);
                    }
                }
            });
            mHandlerRandomCat_rv.postDelayed(SCROLLING_RUNNABLE_randomCat_rv, 2000);
        }
        */
    }

    private void getRandomFromCategoryX(List<ProduitEntry> randomFromCategoryXList){
        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomFromCategoryXList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        randomCat_X_rv.setLayoutManager(mLinearLayoutManager);
        randomCat_X_rv.setAdapter(randomProductAdapter);

        if (isCarouselSlide) {
            recycleViewAnimation(randomFromCategoryXList, randomCat_X_rv, randomProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_randomCat_X_rv);
        }
    }

    private void getRecentProducts(List<ProduitEntry> recentProductList){

        final RecentProductAdapter recentProductAdapter = new RecentProductAdapter(this.mContext, recentProductList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recentProducts_rv.setLayoutManager(mLinearLayoutManager);
        recentProducts_rv.setAdapter(recentProductAdapter);

        if (isCarouselSlide) {
            recycleViewAnimation(recentProductList, recentProducts_rv, recentProductAdapter, mLinearLayoutManager, SCROLLING_RUNNABLE_recentProducts_rv);
        }
        /*
        if (recentProductList.size() != 0) {
            recentProducts_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastItem == mLinearLayoutManager.getItemCount() - 1) {
                        mHandlerRecentProducts_rv.removeCallbacks(SCROLLING_RUNNABLE_recentProducts_rv);
                        Handler postHandler = new Handler();
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recentProducts_rv.setAdapter(null);
                                recentProducts_rv.setAdapter(recentProductAdapter);
                                mHandlerRecentProducts_rv.postDelayed(SCROLLING_RUNNABLE_recentProducts_rv, 2000);
                            }
                        }, 2000);
                    }
                }
            });
            mHandlerRecentProducts_rv.postDelayed(SCROLLING_RUNNABLE_recentProducts_rv, 2000);
        }
        */
    }

}
