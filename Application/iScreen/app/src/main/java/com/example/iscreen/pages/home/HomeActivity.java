package com.example.iscreen.pages.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.CategorieEntry;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.interfaces.FindCategorieListener;
import com.example.iscreen.interfaces.FindImagesProductsListener;
import com.example.iscreen.interfaces.FindProductsListener;
import com.example.iscreen.pages.Loading;
import com.example.iscreen.pages.home.fragments.About;
import com.example.iscreen.pages.home.fragments.Affichage;
import com.example.iscreen.pages.home.fragments.Parametre;
import com.example.iscreen.remote.ConnectionManager;
import com.example.iscreen.remote.model.Categorie;
import com.example.iscreen.remote.model.Product;
import com.example.iscreen.remote.rest.FindCategoriesREST;
import com.example.iscreen.remote.rest.FindProductsREST;
import com.example.iscreen.task.FindCategorieTask;
import com.example.iscreen.task.FindImagesProductsTask;
import com.example.iscreen.task.FindProductsTask;
import com.example.iscreen.utility.IScreenUtility;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FindProductsListener, FindCategorieListener, FindImagesProductsListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static AppDatabase db;

    private ProgressDialog progressDialog;
    /** Get products and Categories **/
    //    task de recuperation des produits
    private FindProductsTask mFindProductsTask = null;

    //    task de recuperation des categories
    private FindCategorieTask mFindCategorieTask = null;

    private int mPageCategorie = 0;
    //position courante de la requete de recuperation des produit
    private int mCurrentPdtQuery = 0;
    private int mTotalPdtQuery = 0;
    private int mLimit = 50;
    private long categorieIdGlobal = 0;
    private int mCountRequestImg = 0;
    private int mCountRequestImgTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = AppDatabase.getInstance(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        //starting with Client_FragmentFindMerchant()
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_homeLayout);
        navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Side Menu info
        View header = navigationView.getHeaderView(0);
        TextView nav_header_currentUserName_tv = (TextView)header.findViewById(R.id.nav_header_currentUserName);
        nav_header_currentUserName_tv.setText(db.userDao().getUser().get(0).getLogin());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ImageView icon = navigationView.getHeaderView(0).findViewById(R.id.nav_header_currentUserImage);
        TextView companyNameText = navigationView.getHeaderView(0).findViewById(R.id.nav_header_currentUserName);
        icon.setImageResource(R.drawable.iscreen_logo);
        companyNameText.setText(db.serverDao().getActiveServer(true).getTitle());

        //Check / Set Full Screen Mode
        new IScreenUtility().fullScreenMode(this, this);

        if (savedInstanceState == null){
            //getSupportActionBar().hide();
            //default fragment when activity is running
            getSupportActionBar().setTitle("Affichage");
            navigationView.setCheckedItem(R.id.nav_affichage);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Affichage()).commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_affichage:
                //init Side Menu user information
                //initMenuHeaderInfo(navigationView);

                toolbar.setTitle("Affichage");
                navigationView.setCheckedItem(R.id.nav_affichage);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Affichage()).commit();
                break;

            case R.id.nav_settings:
                //init Side Menu user information
                //initMenuHeaderInfo(navigationView);

                toolbar.setTitle("Paramètre");
                navigationView.setCheckedItem(R.id.nav_settings);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Parametre()).commit();
                break;

            case R.id.nav_refresh:
                final AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("Synchroniser les produits.")
                        .setMessage(String.format("%s", getResources().getString(R.string.action_sychro)))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Si le téléphone n'est pas connecté
                                if (!ConnectionManager.isPhoneConnected(getApplicationContext())) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // Delete all the products and images
                                showProgressDialog(true, "Produit", "Téléchargement des catégories depuis le server...");
                                db.categorieDao().deleteAllCategorie();
                                db.productDao().deleteAllProducts();
                                executeFindCategorieProducts();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                dialog1.show();
                break;

            case R.id.nav_about:
                toolbar.setTitle("A Propos");
                navigationView.setCheckedItem(R.id.nav_about);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new About()).commit();
                break;

            case R.id.nav_disconnect:
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Confirmer la déconnexion.")
                        .setMessage(String.format("%s", getResources().getString(R.string.action_risque_deconnexion)))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Si le téléphone n'est pas connecté
                                if (!ConnectionManager.isPhoneConnected(getApplicationContext())) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                db.userDao().deleteAllUser();
                                db.tokenDao().deleteAllToken();
                                db.categorieDao().deleteAllCategorie();
                                db.productDao().deleteAllProducts();
                                db.configurationDao().deleteAllConfig();
                                IScreenUtility.deleteProduitsImgFolder();
                                startActivity(new Intent(HomeActivity.this, Loading.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                dialog.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showProgressDialog(boolean show, String title, String message) {
        if (show) {
            progressDialog = new ProgressDialog(HomeActivity.this);
            if (title != null) progressDialog.setTitle(title);
            if (message != null) progressDialog.setMessage(message);

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_view));
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    /**
     * Get all products from the server and save them locally
     **/
    private void executeFindCategorieProducts(){
        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(this)) {
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            showProgressDialog(false, null, null);
            return;
        }

        //Get all the products from the server
        if (mFindCategorieTask == null) {

            Log.e(TAG, "executeFindCategorieProducts: page=" + mPageCategorie);
            mFindCategorieTask = new FindCategorieTask(this, HomeActivity.this, "label", "asc", mLimit, mPageCategorie, "product");
            mFindCategorieTask.execute();
        }
    }

    @Override
    public void onFindCategorieCompleted(FindCategoriesREST findCategoriesREST) {
        mFindCategorieTask = null;

        //        Si la recupération echoue, on renvoi un message d'erreur
        if (findCategoriesREST == null) {
            //        Fermeture du loader
            showProgressDialog(false, null, null);
            Toast.makeText(this, getString(R.string.service_indisponible), Toast.LENGTH_LONG).show();
            return;
        }
        if (findCategoriesREST.getCategories() == null) {
            //Log.e(TAG, "onFindCategorieCompleted: findCategoriesREST findCategoriesREST null");
            //reinitialisation du nombre de page
            mPageCategorie = 0;

            showProgressDialog(false, null, null);
            executeFindProducts();
            return;
        }

        for (Categorie categorieItem : findCategoriesREST.getCategories()) {
            CategorieEntry categorieEntry = new CategorieEntry();
            categorieEntry.setId(Long.parseLong(categorieItem.getId()));
            categorieEntry.setLabel(categorieItem.getLabel());
            categorieEntry.setDescription(categorieItem.getDescription());
            categorieEntry.setPoster_name(IScreenUtility.getImgProduit(categorieItem.getDescription()));

            Log.e(TAG, "onFindThirdpartieCompleted: insert categorieEntry");

//            insertion du client dans la BD
            db.categorieDao().insertCategorie(categorieEntry);
        }
        Log.e(TAG, "onFindCategorieCompleted: mPage=" + mPageCategorie);

        mPageCategorie++;
        executeFindCategorieProducts();
    }

    private void executeFindProducts(){
        showProgressDialog(true, "Produit", "Téléchargement des produits depuis le server...");

        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(this)) {
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            showProgressDialog(false, null, null);
            return;
        }

        List<CategorieEntry> categorieEntries = db.categorieDao().getAllCategories();
        //modification de la position de la requete totale de recupération des produits
        mTotalPdtQuery = categorieEntries.size();

        Log.e(TAG, "executeFindProducts: categorieEntries=" + categorieEntries.size() +" mTotalPdtQuery=" + mTotalPdtQuery);

        for (int i = 0; i < categorieEntries.size(); i++) {

            CategorieEntry categorieEntry = categorieEntries.get(i);

            Log.e(TAG, "executeFindProducts: mCurrentPdtQuery=" + mCurrentPdtQuery +
                    " categorieID=" + categorieEntry.getId() +
                    " categorieLabel=" + categorieEntry.getLabel());

            FindProductsTask findProductsTask = new FindProductsTask(this, HomeActivity.this, "label", "asc", 0, -1, categorieEntry.getId());
            findProductsTask.execute();
        }
    }

    private String decimalPrice(String price_str){
        double price_db = Double.valueOf(price_str);
        return new DecimalFormat("##.##").format(price_db);
    }

    @Override
    public void onFindProductsCompleted(FindProductsREST findProductsREST) {

        //modification de la position de la requete courante de recupération des produits
        mCurrentPdtQuery++;
        //Log.e(TAG, "onFindProductsCompleted: FindProductsREST getThirdparties mCurrentPdtQuery=" + mCurrentPdtQuery + " mTotalPdtQuery=" + mTotalPdtQuery);

        if (findProductsREST != null && findProductsREST.getProducts() != null) {
            //Log.e(TAG, "onFindProductsCompleted: saving product categorie=" + findProductsREST.getCategorie_id() + " pdtSize=" + findProductsREST.getProducts().size());
            for (Product productItem : findProductsREST.getProducts()) {
                //Log.e(TAG, "onFindProductsCompleted: tva_tx=" + productItem.getTva_tx());
                final ProduitEntry produitEntry = new ProduitEntry();
                produitEntry.setId(Long.parseLong(productItem.getId()));
                produitEntry.setCategorie_id(findProductsREST.getCategorie_id());
                produitEntry.setLabel(productItem.getLabel());
                produitEntry.setPrice(decimalPrice(productItem.getPrice()).replace(",","."));
                produitEntry.setPrice_ttc(decimalPrice(productItem.getPrice_ttc()).replace(",","."));
                produitEntry.setRef(productItem.getRef());
                produitEntry.setStock_reel(productItem.getStock_reel());
                produitEntry.setDescription(productItem.getDescription());
                produitEntry.setTva_tx(productItem.getTva_tx());
                produitEntry.setNote(productItem.getNote());
                produitEntry.setNote_public(productItem.getNote_public());
                produitEntry.setNote_private(productItem.getNote_private());

//                    Log.e(TAG, "onFindThirdpartieCompleted: insert produitEntry");
//            insertion du client dans la BD
                if (db.productDao().getProductById(produitEntry.getId()) == null) {
                    db.productDao().insertProduit(produitEntry);
                }
            }
//            Log.e(TAG, "onFindProductsCompleted: mPage=" + mCurrentPdtQuery);

            if (mCurrentPdtQuery >= mTotalPdtQuery - 1) {
                //Objects.requireNonNull(this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                //Fermeture du loader
                showProgressDialog(false, null, null);

                //initContent();
                Toast.makeText(this, getString(R.string.liste_produits_synchronises), Toast.LENGTH_LONG).show();

                executeFindImageProduct();

                /*
                Log.e(TAG, " onFindProductsCompleted() || findImage() => Start");
                //showProgressDialog(true, null, getString(R.string.miseajour_images_produits));

                //Suppression des images des clients en local
                ISalesUtility.deleteProduitsImgFolder();

                findImage();
                Log.e(TAG, " onFindProductsCompleted() || findImage() => End");

                initContent();
                */
                return;
            }
        } else {
            if (mCurrentPdtQuery >= mTotalPdtQuery - 1) {
                //Objects.requireNonNull(this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                //        Fermeture du loader
                showProgressDialog(false, null, null);

                Toast.makeText(this, getString(R.string.liste_produits_synchronises), Toast.LENGTH_LONG).show();
                //loadProduits(-1, null, 0);
                //initContent();

                return;
            }
        }

    }

    private void executeFindImageProduct(){
        //affichage du loader dialog
        showProgressDialog(true, "Image", getString(R.string.miseajour_images_produits));

        //Suppression des images des clients en local
        IScreenUtility.deleteProduitsImgFolder();

        final List<ProduitEntry> produitEntries = db.productDao().getAllProducts();
        mCountRequestImg = 0;
        mCountRequestImgTotal = produitEntries.size();
        Log.e(TAG, "findImage: produitEntriesSize=" + produitEntries.size() + " mCountRequestImgTotal=" + mCountRequestImgTotal);
        if (produitEntries.size() > 0) {

            //setAutoOrientationEnabled(getContext(), true);
            for (ProduitEntry produitEntry : produitEntries) {

                //Si le téléphone n'est pas connecté
                if (!ConnectionManager.isPhoneConnected(this)) {
                    showProgressDialog(false, null, null);
                    Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
                    break;
                }

                FindImagesProductsTask task = new FindImagesProductsTask(this, HomeActivity.this, produitEntry);
                task.execute();
            }
            return;
        } else {
            showProgressDialog(false, null, null);
            Toast.makeText(this, "Aucun produits", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onFindImagesProductsComplete(String pathFile) {
        mCountRequestImg++;

        //Log.e(TAG, "onFindImagesProductsComplete: pathFile="+pathFile+" mCountRequestImg="+mCountRequestImg+" mCountRequestImgTotal="+mCountRequestImgTotal);
        if (progressDialog != null) {
            progressDialog.setMessage(String.format("%s. %s / %s ", getString(R.string.miseajour_images_produits), mCountRequestImg, mCountRequestImgTotal));
        }
        if (mCountRequestImg == mCountRequestImgTotal) {
//            Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            //initContent();
            showProgressDialog(false, null, null);

            //setAutoOrientationEnabled(getContext(), false);
            Toast.makeText(this, getString(R.string.miseajour_images_produits_effectuee), Toast.LENGTH_LONG).show();

            startActivity(new Intent(HomeActivity.this, Affichage.class));
            return;
        }
    }
}
