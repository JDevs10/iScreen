package com.example.iscreen.pages;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.CategorieEntry;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.database.entity.ProduitEntry;
import com.example.iscreen.database.entity.ServerEntry;
import com.example.iscreen.database.entity.TokenEntry;
import com.example.iscreen.database.entity.UserEntry;
import com.example.iscreen.interfaces.FindCategorieListener;
import com.example.iscreen.interfaces.FindConfigurationListener;
import com.example.iscreen.interfaces.FindImagesProductsListener;
import com.example.iscreen.interfaces.FindProductsListener;
import com.example.iscreen.interfaces.OnInternauteLoginComplete;
import com.example.iscreen.pages.home.HomeActivity;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.remote.ConnectionManager;
import com.example.iscreen.remote.model.Categorie;
import com.example.iscreen.remote.model.Internaute;
import com.example.iscreen.remote.model.Product;
import com.example.iscreen.remote.model.User;
import com.example.iscreen.remote.rest.FindCategoriesREST;
import com.example.iscreen.remote.rest.FindConfigurationREST;
import com.example.iscreen.remote.rest.FindProductsREST;
import com.example.iscreen.remote.rest.LoginREST;
import com.example.iscreen.task.FindCategorieTask;
import com.example.iscreen.task.FindConfigurationTask;
import com.example.iscreen.task.FindImagesProductsTask;
import com.example.iscreen.task.FindProductsTask;
import com.example.iscreen.task.InternauteLoginTask;
import com.example.iscreen.utility.IScreenUtility;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loading extends AppCompatActivity implements OnInternauteLoginComplete, FindProductsListener, FindCategorieListener, FindImagesProductsListener, FindConfigurationListener {
    private static final String TAG = Loading.class.getSimpleName();

    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Dialog mDialog;

    /** If no token saved **/
    private InternauteLoginTask mAuthTask = null;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 96;

    // UI references.
    private AutoCompleteTextView mUsernameET;
    private EditText mPasswordET, mServerET;
    private ImageView mServerIV;
    private View mLoginFormView;

    private String mServer;
    private String mUsername;
    private String mPassword;

    private List<ServerEntry> serverEntries;
    private ServerEntry mServerChoose;

    /** Get Server Configuration **/
    private FindConfigurationTask findConfigurationTask = null;

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

    private static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_main);

        db = AppDatabase.getInstance(getApplicationContext());

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Loading.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Loading.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                /**
                 * Show an explanation to the user *asynchronously* -- don't block
                 * this thread waiting for the user's response! After the user
                 * sees the explanation, try again to request the permission.
                 */

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Loading.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Loading.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        progressDialog = new ProgressDialog(Loading.this);

        // Set loading...
        progressDialog.setTitle("Configuration");
        progressDialog.setMessage("Chargement de la configuration actuelle...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //setStaticProduct();

        /** new Thread to Retrieve/Load Configurations **/
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(10);
            }
        };

        if (getSaveTokenData()) {

            /** New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds. **/
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (progressDialog.getProgress() <= progressDialog.getMax()) {
                            Thread.sleep(200);
                            handler.sendMessage(handler.obtainMessage());

                            // Create an Intent that will start the Menu-Activity.
                            if (progressDialog.getProgress() == progressDialog.getMax() && getLocalConf() == 1) {
                                progressDialog.dismiss();
                                startActivity(new Intent(Loading.this, HomeActivity.class));

                                //kill the Thread with return
                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, "No Server Init Configuration !!!", Toast.LENGTH_LONG).show();

            /** Show a dialog to enter the server info **/
            dialogEnterServerInfo(true);
        }
    }

    private boolean getSaveTokenData(){
        boolean res = false;
        if (db.tokenDao().getAllToken().size() == 1 &&
                db.configurationDao().getCurrentConfig().size() == 1 &&
                db.categorieDao().getAllCategories().size() != 0 &&
                db.productDao().getProducts().size() != 0){
            res = true;
        }
        return res;
    }

    private void dialogEnterServerInfo(boolean status){
        if (status){
            mDialog = new Dialog(Loading.this);
            mDialog.setContentView(R.layout.dialog_server_info_login);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            InitServerInfo(mDialog);
            mDialog.show();
        }else {
            mDialog.dismiss();
        }
    }

    private void setStaticProduct(){
        // add static test products
        db.productDao().deleteAllProducts();
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
    }

    private void getServerConfigTEST(){
        // Testing
        // Test Config
        Configuration config = new Configuration();
        config.setId(1);
        config.setRandomProduct(true);
        config.setRandomCategory(false);
        config.setRandomCategoryX("-1");
        config.setRecentProducts(true);
        config.setCarouselSize(30);
        config.setCarouselSlide(true);
        config.setCarouselSpeed(15);
        db.configurationDao().insertConfig(config);
    }

    private String decimalPrice(String price_str){
        double price_db = Double.valueOf(price_str);
        return new DecimalFormat("##.##").format(price_db);
    }

    private int getLocalConf(){
        //get local config
        List<Configuration> currentConfig = db.configurationDao().getCurrentConfig();
//        String log = "DB size: "+currentConfig.size()+"\n" +
//                "ID: " +currentConfig.get(0).getId()+" \n"+
//                "Carrousel random: " +currentConfig.get(0).isRandom()+" \n"+
//                "isGotConfig: "+currentConfig.get(0).getId();
//        Log.e(TAG, " "+log);
        return currentConfig.size();
    }


    /**
     * Get server token/dolibarr key and save it locally
     **/

    private void initServerUrl() {
        //Suppression des serveurs
        db.serverDao().deleteAllServers();

        //Desactivation de tous les serveurs en local
        List<ServerEntry> serverEntries = new ArrayList<>();
        serverEntries.add(new ServerEntry("http://food.apps-dev.fr/api/index.php", "http://food.apps-dev.fr/api/ryimg", "France Food company FFC", "2 rue Charles De Gaulle ZI La Mariniere,", "91070", "Bondoufle", "91 - Essonne", "France", "EURO", "0758542161", "contact@francefoodcompany.fr", "", "", "France Food company FFC", false));
        serverEntries.add(new ServerEntry("http://soifexpress.apps-dev.fr/api/index.php", "http://soifexpress.apps-dev.fr/api/ryimg", "Soif Express", "7 AV Gabriel Peri", "91600", "Savigny Sur Orge", "91 - Essonne", "France", "EURO", "0758088361", "", "www.test.com", "", "SOIF EXPRESS", false));
        serverEntries.add(new ServerEntry("http://asiafood.apps-dev.fr/api/index.php", "http://82.253.71.109/prod/asiafood_v8/api/ryimg", "Asia Food", "8 avenue Duval le Camus", "92210", "ST CLOUD", "92 - Hauts-de-Seine", "France", "EURO", "+33(0)177583700", "contact@asiafoodco.com", "http://www.asiafoodco.com", "", "ASIA FOOD", false));
        serverEntries.add(new ServerEntry("http://bdc.apps-dev.fr/api/index.php", "http://82.253.71.109/prod/bdc_v8/api/ryimg", "BDC", "17 BD DE LA MUETTE", "95140", "GARGES LES GONESSE", "95 - Val-d Oise", "France", "EURO", "", "", "http://www.bigdataconsulting.fr", "", "BDC", false));

        for (ServerEntry serverItem : serverEntries) {
            if (db.serverDao().getServerByHostname(serverItem.getHostname()) == null) {
                db.serverDao().insertServer(serverItem);
            }
        }
    }

    private void InitServerInfo(Dialog mDialog) {
        //Ajouter les serveurs dans la BD
        initServerUrl();

        //Setup the Dialog views
        mServerIV = (ImageView) mDialog.findViewById(R.id.iv_login_server);
        mServerET = (EditText) mDialog.findViewById(R.id.et_login_server);

        // Set up the login form.
        mUsernameET = (AutoCompleteTextView) mDialog.findViewById(R.id.username);
        mPasswordET = (EditText) mDialog.findViewById(R.id.password);

        Button mEmailSignInButton = (Button) mDialog.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = mDialog.findViewById(R.id.login_form);

        //Prevent the keyboard from displaying on activity start
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void attemptLogin() {

        // Reset errors.
        mServerET.setError(null);
        mUsernameET.setError(null);
        mPasswordET.setError(null);

        // Store values at the time of the login attempt.
        mServer = mServerET.getText().toString().trim();
        mUsername = mUsernameET.getText().toString().trim();
        mPassword = mPasswordET.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Teste de validité de l'adresse du serveur
        if (mServer.isEmpty()) {
            mServerET.setError("Veuillez remplir l'addresse du serveur.");
            focusView = mServerET;
            cancel = true;
        }

        // Teste de validité du login
        if (mUsername.isEmpty()) {
            mUsernameET.setError("Veuillez remplir le nom d'utilisateur.");
            focusView = mUsernameET;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (mPassword.isEmpty() && cancel) {
            mPasswordET.setError("Veuillez remplir le mot de passe.");
            focusView = mPasswordET;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            String doliServer = mServerET.getText().toString();
            //String doliServer = mServer;

            serverEntries = db.serverDao().getAllServers();
            int i = 0;
            //Log.e(TAG, "onCreate: getServersSequence() serverEntries="+serverEntries.size());
            while (i < serverEntries.size()) {
                Log.e(TAG, "attemptLogin: getHostname=" + serverEntries.get(i).getHostname() + " doliServer=" + doliServer);
                //recupere le nom de sous-domaine dans le hostname
                if (serverEntries.get(i).getRaison_sociale().toLowerCase().contains(doliServer.toLowerCase())) {
                    //Log.e(TAG, "attemptLogin: equaled doliServer=" + doliServer);
                    mServerChoose = serverEntries.get(i);

                    saveServerurl();
                    executeLogin(mUsername, mPassword);
                    return;
                }

                i++;
            }

            Toast.makeText(Loading.this, "Nom de Compagie Incorrect", Toast.LENGTH_LONG).show();
            return;

        }
    }

    private void saveServerurl() {
        //Log.e(TAG, "saveServerurl: serverurl=" + mServerChoose.getHostname() + " title=" + mServerChoose.getTitle() + " id=" + mServerChoose.getId() + " is_active=" + mServerChoose.getIs_active());
        //desactivation de tous les serveurs en local
        db.serverDao().updateActiveAllserver(false);

        db.serverDao().updateActiveServer(mServerChoose.getId(), true);
    }

    private void showProgressDialog(boolean show, String title, String message) {
        if (show) {
            progressDialog = new ProgressDialog(Loading.this);
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

    private void executeLogin(String username, String password) {
        //masquage du formulaire de connexion
        showProgressDialog(true, "Authentification", "Verification d'identitée");

        if (!ConnectionManager.isPhoneConnected(Loading.this)) {
            Toast.makeText(Loading.this, "Erreur réseau. Veuillez vérifier votre connexion internet.", Toast.LENGTH_LONG).show();

            //masquage du formulaire de connexion
            showProgressDialog(false, null, null);
        }
        if (mAuthTask == null) {
            Internaute internaute = new Internaute(username, password);

            mAuthTask = new InternauteLoginTask(Loading.this, Loading.this, internaute);
            mAuthTask.execute();
        }
    }

    @Override
    public void onInternauteLoginTaskComplete(LoginREST loginREST) {
        mAuthTask = null;

        //Si la connexion echoue, on renvoi un message d'authentification
        if (loginREST == null) {
            Toast.makeText(Loading.this, "Service indisponible. Veuillez réssayer plutard.", Toast.LENGTH_LONG).show();

            //masquage du formulaire de connexion
            showProgressDialog(false, null, null);
            return;
        }
        if (loginREST.getInternauteSuccess() == null) {
            if (loginREST.getErrorCode() == 404) {
                Toast.makeText(Loading.this, "Service indisponible. Veuillez réssayer plutard.", Toast.LENGTH_LONG).show();

                //masquage du formulaire de connexion
                showProgressDialog(false, null, null);
                return;
            } else {
                Toast.makeText(Loading.this, "Erreur d'authentification. Paramètre de connexion incorrect.", Toast.LENGTH_LONG).show();

                //masquage du formulaire de connexion
                showProgressDialog(false, null, null);
                return;
            }
        }

        /**  Connexion reussie **/
        //Suppression du token
        db.tokenDao().deleteAllToken();
        //Enregistrement du token dans la BD local
        TokenEntry tokenEntry = new TokenEntry(
                loginREST.getInternauteSuccess().getSuccess().getToken(),
                loginREST.getInternauteSuccess().getSuccess().getMessage());
        db.tokenDao().insertToken(tokenEntry);

        String sqlfilter = "login=\"" + mUsername + "\"";
        Call<ArrayList<User>> callUser = ApiUtils.getIScreenService(Loading.this).findUserByLogin(sqlfilter);
        callUser.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> responseBody = response.body();
                    User user = responseBody.get(0);
                    Log.e(TAG , " response: "+responseBody.get(0));

                    //Enregistrement du user dans la BD
                    UserEntry userEntry = new UserEntry();
                    userEntry.setAddress(user.getAddress());
                    userEntry.setBirth(user.getBirth());
                    userEntry.setCountry(user.getCountry());
                    userEntry.setDatec(user.getDatec());
                    userEntry.setDateemployment(user.getDateemployment());
                    userEntry.setDatelastlogin(user.getDatelastlogin());
                    userEntry.setDatem(user.getDatem());
                    userEntry.setEmail(user.getEmail());
                    userEntry.setEmployee(user.getEmployee());
                    userEntry.setFirstname(user.getFirstname());
                    userEntry.setGender(user.getGender());
                    userEntry.setId(Long.parseLong(user.getId()));
                    userEntry.setLastname(user.getLastname());
                    userEntry.setAdmin(user.getAdmin());
                    userEntry.setLogin(user.getLogin());
                    userEntry.setName(user.getName());
                    userEntry.setPhoto(user.getPhoto());
                    userEntry.setStatut(user.getStatut());
                    userEntry.setTown(user.getTown());

                    //suppresion du user
                    db.userDao().deleteAllUser();
                    //insertion du user dans la BD
                    db.userDao().insertUser(userEntry);

                    //affichage du formulaire de connexion
                    showProgressDialog(false, null, null);

                    //Log.e(TAG, "doInBackground: internauteSuccess="+loginREST.getInternauteSuccess().getSuccess().getToken());
                    //Intent intent = new Intent(Loading.this, HomeActivity.class);
                    //startActivity(intent);

                    //Restart Activity
                    //Intent intent = getIntent();
                    //finish();
                    //startActivity(intent);

                    executeFindConfiguration();

                } else {

                    //affichage du formulaire de connexion
                    showProgressDialog(false, null, null);
                    try {
                        Log.e(TAG, "uploadDocument onResponse SignComm err: message=" + response.message() +
                                " | code=" + response.code() + " | code=" + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: message=" + e.getMessage());
                    }
                    if (response.code() == 404) {
                        Toast.makeText(Loading.this, "Service indisponsable. Veuillez réssayer plutard.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (response.code() == 401) {
                        Toast.makeText(Loading.this, "Echec d'authentification.", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Toast.makeText(Loading.this, "Service indisponsable. Veuillez réssayer plutard.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                //affichage du formulaire de connexion
                showProgressDialog(false, null, null);
                dialogEnterServerInfo(false);
                Toast.makeText(Loading.this, "Erreu réseau. Veuillez vérifier votre connexion internet.", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


    /**
     * Get Carousels configuration from server and save it locally
     **/
    private void executeFindConfiguration(){
        showProgressDialog(true, "Configuration", "Téléchargement des configuration depuis le server...");

        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(this)) {
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            showProgressDialog(false, null, null);
            return;
        }

        if (findConfigurationTask == null) {
            findConfigurationTask = new FindConfigurationTask(Loading.this, Loading.this, 1);
            findConfigurationTask.execute();
        }
    }

    @Override
    public void onFindConfiguration(FindConfigurationREST findConfigurationREST) {
        findConfigurationTask = null;

        //Si la recupération echoue, on renvoi un message d'erreur
        if (findConfigurationREST == null) {
            //Fermeture du loader
            showProgressDialog(false, null, null);
            Toast.makeText(this, getString(R.string.service_indisponible), Toast.LENGTH_LONG).show();
            return;
        }

        Configuration dbConfig = new Configuration();

        /** ========== (0 = deactivate | 1 = activate) ========== **/
        if (findConfigurationREST.getConfigs().getP_aleatoir().equals("0")){
            dbConfig.setRandomProduct(false);
        }else if (findConfigurationREST.getConfigs().getP_aleatoir().equals("1")){
            dbConfig.setRandomProduct(true);
        }

        if (findConfigurationREST.getConfigs().getA_category().equals("0")){
            dbConfig.setRandomCategory(false);
        }else if (findConfigurationREST.getConfigs().getA_category().equals("1")){
            dbConfig.setRandomCategory(true);
        }

        Log.e(TAG, "getCategory_x() != "+findConfigurationREST.getConfigs().getCategory_x());
        dbConfig.setRandomCategoryX(findConfigurationREST.getConfigs().getCategory_x());

        if (findConfigurationREST.getConfigs().getP_recente().equals("0")){
            dbConfig.setRecentProducts(false);
        }else if (findConfigurationREST.getConfigs().getP_recente().equals("1")){
            dbConfig.setRecentProducts(true);
        }

        // App default configurations
        dbConfig.setCarouselSize(20);
        dbConfig.setCarouselSlide(true);
        dbConfig.setCarouselSpeed(15);
        dbConfig.setShowCarouselTitle(true);
        dbConfig.setFullScreenMode(false);
        dbConfig.setDarkMode(false);

        db.configurationDao().deleteAllConfig();
        db.configurationDao().insertConfig(dbConfig);
        showProgressDialog(false, "Configuration", "Téléchargement des configuration depuis le server...");

        showProgressDialog(true, "Produit", "Téléchargement des catégories depuis le server...");
        db.categorieDao().deleteAllCategorie();
        db.productDao().deleteAllProducts();
        executeFindCategorieProducts();
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
            mFindCategorieTask = new FindCategorieTask(this, Loading.this, "label", "asc", mLimit, mPageCategorie, "product");
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

            FindProductsTask findProductsTask = new FindProductsTask(this, Loading.this, "label", "asc", 0, -1, categorieEntry.getId());
            findProductsTask.execute();
        }
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

                FindImagesProductsTask task = new FindImagesProductsTask(this, Loading.this, produitEntry);
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

            dialogEnterServerInfo(false);
            startActivity(new Intent(Loading.this, HomeActivity.class));
            return;
        }
    }

}

