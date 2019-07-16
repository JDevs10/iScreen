package com.example.iscreen.pages.home.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.adapter.RandomProductAdapter;
import com.example.iscreen.adapter.RecentProductAdapter;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.ProduitEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Catalog extends Fragment {
    private final String TAG = Catalog.class.getSimpleName();
    private Context mContext;

    private RecyclerView random_rv;
    private RecyclerView randomCat_rv;
    private RecyclerView recentProducts_rv;

    private AppDatabase db;
    private List<ProduitEntry> allProducts;
    private List<ProduitEntry> randomProductList;


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

        allProducts = db.productDao().getProducts();
        getRandomProducts(allProducts.size());
        getRandomFromEachCategory();
        getRecentProducts();
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


    private void getRandomProducts(int max){
        List<ProduitEntry> randomProductList = new ArrayList<>();
        ArrayList<Integer> saved = new ArrayList<>();
        Random random = new Random();
        int index = 0;

        while (index < 30){
            int x = random.nextInt(((max-1) - 1) + 1) + 1;

            Log.e(TAG, "allProducts size: "+allProducts.size()+"\n" +
                    "Saved size: "+saved.size());

            if (saved.size() != 0){
                if (!saved.contains(x)) {
                    saved.add(x);
                    randomProductList.add(allProducts.get(x));
                    index++;
                    Log.e(TAG, "next => x: "+x+"\n" +
                            "index: "+index+"\n" +
                            "saved size: "+saved.size());
                }
            }else{
                saved.add(x);
                randomProductList.add(allProducts.get(x));
                index++;
                Log.e(TAG, "first => x: "+x+"\n" +
                        "index: "+index+"\n" +
                        "saved size: "+saved.size());
            }
        }

        if (index == 30){

            final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, randomProductList);
            final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            random_rv.setLayoutManager(mLinearLayoutManager);
            random_rv.setAdapter(randomProductAdapter);
            //recycleViewAnimation(random_rv, randomProductAdapter, mLinearLayoutManager, 1000);

            random_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if(lastItem == mLinearLayoutManager.getItemCount()-1){
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


        }else{
            Toast.makeText(mContext, "Error[301] : Failed to show random list", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRandomFromEachCategory(){

        final RandomProductAdapter randomProductAdapter = new RandomProductAdapter(this.mContext, db.productDao().getProducts());
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        randomCat_rv.setLayoutManager(mLinearLayoutManager);
        randomCat_rv.setAdapter(randomProductAdapter);

        randomCat_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastItem == mLinearLayoutManager.getItemCount()-1){
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

    void getRecentProducts(){
        List<ProduitEntry> lastProductList = new ArrayList<>();
        for (int i=0; i<db.productDao().loadRecentProduct().size(); i++){
            lastProductList.add(db.productDao().loadRecentProduct().get(i));
            if ( (i+1) == 30){
                break;
            }
        }

        final RecentProductAdapter recentProductAdapter = new RecentProductAdapter(this.mContext, lastProductList);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recentProducts_rv.setLayoutManager(mLinearLayoutManager);
        recentProducts_rv.setAdapter(recentProductAdapter);

        recentProducts_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastItem == mLinearLayoutManager.getItemCount()-1){
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
