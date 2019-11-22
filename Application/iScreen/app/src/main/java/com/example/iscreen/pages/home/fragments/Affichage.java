package com.example.iscreen.pages.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.adapter.ProductAdapter;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.interfaces.FindConfigurationListener;
import com.example.iscreen.interfaces.OnItemClickListener;
import com.example.iscreen.interfaces.ProduitsAdapterListener;
import com.example.iscreen.model.Carrousel;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.LoadCarousels;
import com.example.iscreen.pages.Loading;
import com.example.iscreen.pages.home.DetailProduct;
import com.example.iscreen.pages.home.HomeActivity;
import com.example.iscreen.remote.ConnectionManager;
import com.example.iscreen.remote.rest.FindConfigurationREST;
import com.example.iscreen.task.FindCarouselsList;
import com.example.iscreen.task.FindConfigurationTask;
import com.example.iscreen.utility.IScreenUtility;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Affichage extends Fragment implements LoadCarousels, ProduitsAdapterListener, FindConfigurationListener {
    private final String TAG = Affichage.class.getSimpleName();
    private Context mContext;
    private boolean isCarouselSlide;
    private int mainLayoutWidth, mainLayoutHeight;

    private RecyclerView random_rv;
    private RecyclerView randomCat_rv;
    private RecyclerView randomCat_X_rv;
    private RecyclerView recentProducts_rv;

    private ProductAdapter productAdapter = null;

    private LinearLayout randomTitle_ll, randomCatTitle_ll, randomCat_XTitle_ll, recentProductsTitle_ll;
    private TextView randomTitle, randomCatTitle, randomCat_XTitle, recentProductsTitle;

    private ProgressDialog progressDialog;
    private AppDatabase db;
    private List<ProduitEntry> allProducts;
    private boolean carrouselSetup = false;

    final int durationRandom_rv = 10;
    final int durationRandomCat_rv = 15;
    final int durationRecentProducts_rv = 10;
    final int pixelsToMove = 30;

    /** Get Server Configuration **/
    private FindConfigurationTask findConfigurationTask = null;


    private boolean checkRandom_rvRunnable = false;
    private final Handler mHandlerRandom_rv = new Handler(Looper.getMainLooper());
    private Runnable SCROLLING_RUNNABLE_random_rv = new Runnable() {
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

        randomTitle_ll = (LinearLayout) view.findViewById(R.id.fragment_catalog_randomTitle_1);
        randomCatTitle_ll = (LinearLayout) view.findViewById(R.id.fragment_catalog_randomCategoryTitle_1);
        randomCat_XTitle_ll = (LinearLayout) view.findViewById(R.id.fragment_catalog_categoryProductsTitle_1);
        recentProductsTitle_ll = (LinearLayout) view.findViewById(R.id.fragment_catalog_lastProductTitle_1);

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
        Log.e(TAG, "Config size: "+db.configurationDao().getCurrentConfig().size());
        setResponsible(db.configurationDao().getCurrentConfig().get(0));

        setupCarrouselData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(carrouselSetup) {
                            Log.e(TAG, "Timer: Start ==> executeFindConfiguration()");
                            executeFindConfiguration();
                        }
                    }
                }, 0,60000);
            }
        });
    }

    /**
     * Get Carousels configuration from server and save it locally
     **/
    private void executeFindConfiguration(){

        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(mContext)) {
            Toast.makeText(mContext, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            return;
        }

        if (findConfigurationTask == null) {
            //showProgressDialog(true, "Configuration", "Téléchargement des configuration depuis le server...");
            findConfigurationTask = new FindConfigurationTask(mContext, Affichage.this, 1);
            findConfigurationTask.execute();
        }
    }

    @Override
    public void onFindConfiguration(FindConfigurationREST findConfigurationREST) {
        findConfigurationTask = null;
        //Si la recupération echoue, on renvoi un message d'erreur
        if (findConfigurationREST == null) {
            //Fermeture du loader
            //showProgressDialog(false, null, null);
            Toast.makeText(mContext, getString(R.string.service_indisponible), Toast.LENGTH_LONG).show();
            return;
        }

        //Garbage Collector
        System.gc();

        boolean settingUpdate = false;
        Configuration dbConfig = db.configurationDao().getCurrentConfig().get(0);

        Log.e(TAG, "dbConfig.isRandomProduct() == "+booleanToString(dbConfig.isRandomProduct())+" || "+findConfigurationREST.getConfigs().getP_aleatoir());
        Log.e(TAG, "dbConfig.isRandomCategory() == "+booleanToString(dbConfig.isRandomCategory())+" || "+findConfigurationREST.getConfigs().getA_category());
        Log.e(TAG, "dbConfig.getRandomCategoryX() == "+dbConfig.getRandomCategoryX()+" || "+findConfigurationREST.getConfigs().getCategory_x());
        Log.e(TAG, "dbConfig.isRecentProducts() == "+booleanToString(dbConfig.isRecentProducts())+" || "+findConfigurationREST.getConfigs().getP_recente());


        if (findConfigurationREST.getConfigs().getP_aleatoir() != null && !findConfigurationREST.getConfigs().getP_aleatoir().equals("")){
            if (!booleanToString(dbConfig.isRandomProduct()).equals( findConfigurationREST.getConfigs().getP_aleatoir() )){
                dbConfig.setRandomProduct( stringToBoolean(findConfigurationREST.getConfigs().getP_aleatoir()) );
                settingUpdate = true;
            }
        }
        if (findConfigurationREST.getConfigs().getA_category() != null && !findConfigurationREST.getConfigs().getA_category().equals("")){
            if (!booleanToString(dbConfig.isRandomCategory()).equals( findConfigurationREST.getConfigs().getA_category() )){
                dbConfig.setRandomCategory( stringToBoolean(findConfigurationREST.getConfigs().getA_category()) );
                settingUpdate = true;
            }
        }
        if (findConfigurationREST.getConfigs().getCategory_x() != null && !findConfigurationREST.getConfigs().getCategory_x().equals("")){
            if (!dbConfig.getRandomCategoryX().equals(findConfigurationREST.getConfigs().getCategory_x()))
            {
                dbConfig.setRandomCategoryX(findConfigurationREST.getConfigs().getCategory_x());
                settingUpdate = true;
            }
        }
        if (findConfigurationREST.getConfigs().getP_recente() != null && !findConfigurationREST.getConfigs().getP_recente().equals("")){
            if (!booleanToString(dbConfig.isRecentProducts()).equals( findConfigurationREST.getConfigs().getP_recente() )){
                dbConfig.setRecentProducts( stringToBoolean(findConfigurationREST.getConfigs().getP_recente()) );
                settingUpdate = true;
            }
        }

        if (settingUpdate){
            db.configurationDao().deleteAllConfig();
            db.configurationDao().insertConfig(dbConfig);
            //showProgressDialog(false, null, null);
            startActivity(new Intent(mContext, HomeActivity.class));
            return;
        }
    }

    public String booleanToString(boolean value) {
        // Convert true to 1 and false to 0.
        String value_str;
        if (value){
            value_str = "1";
        }else{
            value_str = "0";
        }
        return value_str;
    }

    public static boolean stringToBoolean(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s+" is not a bool. Only 1 and 0 are.");
    }

    private void setResponsible(Configuration config){
        int w = 0;
        int x = 0;
        int y = 0;
        int z = 0;

        if (config.isRandomProduct()){  w = 1;}
        if (config.isRandomCategory()){  x = 1;}
        Log.e(TAG, "config.getRandomCategoryX(): "+config.getRandomCategoryX());
        if (config.getRandomCategoryX() != null && !config.getRandomCategoryX().equals("-1")){  y = 1;}
        if (config.isRecentProducts()){  z = 1;}

        //Remove margin
        if(w == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            randomTitle_ll.setLayoutParams(params);
            randomTitle_ll.setVisibility(View.GONE);
            Log.e(TAG, "randomTitle_ll: 0 margins");
        }
        if(x == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            randomCatTitle_ll.setLayoutParams(params);
            randomCatTitle_ll.setVisibility(View.GONE);
            Log.e(TAG, "randomCatTitle_ll: 0 margins");
        }
        if(y == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            randomCat_XTitle_ll.setLayoutParams(params);
            randomCat_XTitle_ll.setVisibility(View.GONE);
            Log.e(TAG, "randomCat_XTitle_ll: 0 margins");
        }else{ // if y == 1 then set all margins to zero
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            randomCat_XTitle_ll.setLayoutParams(params);
        }
        if(z == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            recentProductsTitle_ll.setLayoutParams(params);
            recentProductsTitle_ll.setVisibility(View.GONE);
            Log.e(TAG, "recentProductsTitle_ll: 0 margins");
        }

        int res = w + x + y + z;
        switch (res){
            case 0:
                mainLayoutWidth = 0;
                mainLayoutHeight = 0;
                Log.e(TAG, "setResponsible() ==> 0 ; 0");
                break;
            case 1:
                if (db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    mainLayoutWidth = 1400;
                    mainLayoutHeight = 1600;
                    Log.e(TAG, "setResponsible() ==> 1000 ; 1600");
                }else{
                    mainLayoutWidth = 1350;
                    mainLayoutHeight = 1700;
                    Log.e(TAG, "setResponsible() ==> 1000 ; 1800");
                }
                break;
            case 2:
                if (db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    mainLayoutWidth = 800;
                    mainLayoutHeight = 800;
                    Log.e(TAG, "setResponsible() ==> 800 ; 800");
                }else {
                    mainLayoutWidth = 850;
                    mainLayoutHeight = 850;
                    Log.e(TAG, "setResponsible() ==> 850 ; 850");
                }
                break;
            case 3:
                if (db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()) {
                    mainLayoutWidth = 400;
                    mainLayoutHeight = 500;
                    Log.e(TAG, "setResponsible() ==> 400 ; 400");
                }else{
                    mainLayoutWidth = 400;
                    mainLayoutHeight = 550;
                    Log.e(TAG, "setResponsible() ==> 400 ; 550");
                }
                break;
            case 4:
                if (db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()) {
                    mainLayoutWidth = 350;
                    mainLayoutHeight = 360;
                    Log.e(TAG, "setResponsible() ==> 375 ; 375");
                }else{
                    mainLayoutWidth = 333;
                    mainLayoutHeight = 425;
                    Log.e(TAG, "setResponsible() ==> 425 ; 425");
                }
                break;
        }
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
        showProgressDialog(true, "Produit", "Chargement des carousels...");

        Log.e(TAG, " setupCarrouselData() | DB getCarouselSize = "+db.configurationDao().getCurrentConfig().get(0).getCarouselSize());
        Log.e(TAG, " setupCarrouselData() | DB isRandomProduct = "+db.configurationDao().getCurrentConfig().get(0).isRandomProduct());
        Log.e(TAG, " setupCarrouselData() | DB isRandomCategory = "+db.configurationDao().getCurrentConfig().get(0).isRandomCategory());
        Log.e(TAG, " setupCarrouselData() | DB getRandomCategoryX = "+db.configurationDao().getCurrentConfig().get(0).getRandomCategoryX());
        Log.e(TAG, " setupCarrouselData() | DB isRecentProducts = "+db.configurationDao().getCurrentConfig().get(0).isRecentProducts());
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
        if (carrousel != null) {
            Log.e(TAG, "onLoadCarouselsData() | carrousel != null");
            getRandomProducts(carrousel.getRandomProductList());
            getRandomFromEachCategory(carrousel.getRandomFromEachCategoryList());
            getRandomFromCategoryX(carrousel.getRandomFromSelectedCategoryList(), carrousel.getSelectedCategoryName());
            getRecentProducts(carrousel.getRecentProductList());
        }
        showProgressDialog(false, null, null);

        // Set carrouselSetup, so the auto config update can start
        carrouselSetup = true;
    }

    private void recycleViewAnimation(List<ProduitEntry> productList, final RecyclerView theRecyclerView, final ProductAdapter adapter, final LinearLayoutManager llm, final Handler handler, final Runnable runnable) {
        if (productList == null || productList.size() != 0) {
            theRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                int lastItem = llm.findLastCompletelyVisibleItemPosition();
                if (lastItem == llm.getItemCount() - 1) {
                    handler.removeCallbacks(runnable);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(null);
                            recyclerView.setAdapter(adapter);

                            handler.postDelayed(runnable, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
                        }
                    }, 2000);
                }
                }
            });
            handler.postDelayed(runnable, db.configurationDao().getCurrentConfig().get(0).getCarouselSpeed());
        }
    }

    private void getRandomProducts(List<ProduitEntry> randomProductList){
        if (db.configurationDao().getCurrentConfig().get(0).isRandomProduct()) {
            if (randomProductList != null || randomProductList.size() != 0) {

                //Show carousel title
                if(db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    randomTitle.setText("Produits aléatoire");
                }else{
                    randomTitle.setVisibility(View.GONE);
                }

                productAdapter = new ProductAdapter(this.mContext, randomProductList, mainLayoutWidth,mainLayoutHeight, this);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                random_rv.setLayoutManager(mLinearLayoutManager);
                random_rv.setAdapter(productAdapter);

                LinearSnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(random_rv);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomProductList, random_rv, productAdapter, mLinearLayoutManager, mHandlerRandom_rv, SCROLLING_RUNNABLE_random_rv);
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
                //Show carousel title
                if(db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    randomCatTitle.setText("Produits de chaque catégorie");
                }else{
                    randomCatTitle.setVisibility(View.GONE);
                }

                final ProductAdapter productAdapter = new ProductAdapter(this.mContext, randomFromSelectedCategoryList, mainLayoutWidth, mainLayoutHeight, this);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                randomCat_rv.setLayoutManager(mLinearLayoutManager);
                randomCat_rv.setAdapter(productAdapter);

                LinearSnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(randomCat_rv);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomFromSelectedCategoryList, randomCat_rv, productAdapter, mLinearLayoutManager, mHandlerRandomCat_rv, SCROLLING_RUNNABLE_randomCat_rv);
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
                //Show carousel title
                if(db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    randomCat_XTitle.setText("Produits de la catégorie : " + categoryName);
                }else{
                    randomCat_XTitle.setVisibility(View.GONE);
                }

                final ProductAdapter productAdapter = new ProductAdapter(this.mContext, randomFromCategoryXList, mainLayoutWidth, mainLayoutHeight, this);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                randomCat_X_rv.setLayoutManager(mLinearLayoutManager);
                randomCat_X_rv.setAdapter(productAdapter);

                LinearSnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(randomCat_X_rv);

                if (isCarouselSlide) {
                    recycleViewAnimation(randomFromCategoryXList, randomCat_X_rv, productAdapter, mLinearLayoutManager, mHandlerRandomCat_X_rv, SCROLLING_RUNNABLE_randomCat_X_rv);
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
                //Show carousel title
                if(db.configurationDao().getCurrentConfig().get(0).isShowCarouselTitle()){
                    recentProductsTitle.setText("Produits récente");
                }else{
                    recentProductsTitle.setVisibility(View.GONE);
                }

                final ProductAdapter recentProductAdapter = new ProductAdapter(this.mContext, recentProductList, mainLayoutWidth, mainLayoutHeight, this);
                final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recentProducts_rv.setLayoutManager(mLinearLayoutManager);
                recentProducts_rv.setAdapter(recentProductAdapter);

                LinearSnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(recentProducts_rv);

                if (isCarouselSlide) {
                    recycleViewAnimation(recentProductList, recentProducts_rv, recentProductAdapter, mLinearLayoutManager, mHandlerRecentProducts_rv, SCROLLING_RUNNABLE_recentProducts_rv);
                }
            }
        }else{
            recentProductsTitle.setVisibility(View.GONE);
            recentProducts_rv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetailsSelected(ProduitEntry product) {
        Log.e(TAG, "Called ==> onDetailsSelected()");
        Intent intent = new Intent(getContext(), DetailProduct.class);
        intent.putExtra("ref_produit", product.getRef());
        startActivity(intent);
    }
}
