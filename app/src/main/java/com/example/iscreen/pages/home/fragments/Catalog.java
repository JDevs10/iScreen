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

    private RecyclerView random_rv;
    private RecyclerView randomCat_rv;
    private RecyclerView recentProducts_rv;

    private ProgressDialog progressDialog;
    private AppDatabase db;
    private List<ProduitEntry> allProducts;

    final int durationRandom_rv = 10;
    final int durationRandomCat_rv = 15;
    final int durationRecentProducts_rv = 13;
    final int pixelsToMove = 30;

    private final Handler mHandlerRandom_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_random_rv = new Runnable() {
        @Override
        public void run() {
            random_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRandom_rv.postDelayed(this, durationRandom_rv);
        }
    };

    private final Handler mHandlerRandomCat_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_randomCat_rv = new Runnable() {
        @Override
        public void run() {
            randomCat_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRandomCat_rv.postDelayed(this, durationRandomCat_rv);
        }
    };

    private final Handler mHandlerRecentProducts_rv = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE_recentProducts_rv = new Runnable() {
        @Override
        public void run() {
            recentProducts_rv.smoothScrollBy(pixelsToMove, 0);
            mHandlerRecentProducts_rv.postDelayed(this, durationRecentProducts_rv);
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        random_rv = view.findViewById(R.id.randomRecycler);
        randomCat_rv = view.findViewById(R.id.randomCategoryRecycler);
        recentProducts_rv = view.findViewById(R.id.lastProductRecycler);

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
        FindCarouselsList findCarouselsList = new FindCarouselsList(mContext, Catalog.this, 40);
        findCarouselsList.execute();
    }

    @Override
    public void onLoadCarouselsData(Carousel carousel) {
        getRandomProducts(carousel.getRandomProductList());
        getRandomFromEachCategory(carousel.getRandomFromSelectedCategoryList());
        getRecentProducts(carousel.getRecentProductList());

        setupCarouselData = false;
        showProgressDialog(setupCarouselData, null, null);
    }

    private void recycleViewAnimation(final RecyclerView recyclerView, final RecyclerView.Adapter adapter, final LinearLayoutManager llm, final int delay) {
//        new LinearSnapHelper().attachToRecyclerView(recyclerView);

//        for (int index = 0; index <= (adapter.getItemCount() - 1); index++) {
//            if (index == (adapter.getItemCount() - 1)) {
//                index = 0;
//            }
//
//            recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
//        }

        int totalItemCount = adapter.getItemCount();
        do {
            if (totalItemCount <= 0) return;
            int lastVisibleItemIndex = llm.findLastVisibleItemPosition();
            if (lastVisibleItemIndex >= totalItemCount) {
                lastVisibleItemIndex = 0;
            }
            llm.smoothScrollToPosition(recyclerView, null, lastVisibleItemIndex + 1);

            Log.e(TAG, " recycleViewAnimation() "+delay);

        } while (true);
    }

    private void getRandomProducts(List<ProduitEntry> randomProductList){

        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomProductList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        random_rv.setLayoutManager(mLinearLayoutManager);
        random_rv.setAdapter(randomProductAdapter);

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
    }

    private void getRandomFromEachCategory(List<ProduitEntry> randomFromSelectedCategoryList){

        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomFromSelectedCategoryList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        randomCat_rv.setLayoutManager(mLinearLayoutManager);
        randomCat_rv.setAdapter(randomProductAdapter);

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
    }

    private void getRecentProducts(List<ProduitEntry> recentProductList){

        final RecentProductAdapter recentProductAdapter = new RecentProductAdapter(this.mContext, recentProductList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recentProducts_rv.setLayoutManager(mLinearLayoutManager);
        recentProducts_rv.setAdapter(recentProductAdapter);

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
    }

}
