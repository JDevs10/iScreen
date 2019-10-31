package com.example.iscreen.pages.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.CategorieEntry;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.remote.ConnectionManager;
import com.example.iscreen.remote.model.Config;
import com.example.iscreen.utility.IScreenUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Parametre extends Fragment {
    private final String TAG = Affichage.class.getSimpleName();
    private Context mContext;
    private Configuration currentConfig, saveConfig;
    private AppDatabase db;

    private CheckBox random_cb, randomCat_cb, recentProducts_cb, carouselSlide_cb, carouselTitles_cb, fullScreeMode_cb;
    private Spinner randomCat_x_sp, nbProduct_sp, carouselSpeed_sp;
    private Button save_btn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = AppDatabase.getInstance(mContext);
        currentConfig = db.configurationDao().getCurrentConfig().get(0);
        saveConfig = currentConfig;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        random_cb = view.findViewById(R.id.fragment_config_random_cb);
        randomCat_cb = view.findViewById(R.id.fragment_config_random_category_cb);
        randomCat_x_sp = view.findViewById(R.id.fragment_config_random_categoryX_sp);
        recentProducts_cb = view.findViewById(R.id.fragment_config_recent_product_cb);
        nbProduct_sp = view.findViewById(R.id.fragment_config_nbProduct);
        carouselSlide_cb = view.findViewById(R.id.fragment_config_carouselSlide_cb);
        carouselSpeed_sp = view.findViewById(R.id.fragment_config_carouselSpeed);
        carouselTitles_cb = view.findViewById(R.id.fragment_config_carouselTitles_cb);
        fullScreeMode_cb = view.findViewById(R.id.fragment_config_fullScreeMode_cb);

        save_btn = view.findViewById(R.id.fragment_config_save_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        random_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_btn.setEnabled(true);
            }
        });
        random_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setRandomProduct(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }
        });

        randomCat_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_btn.setEnabled(true);
            }
        });
        randomCat_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setRandomCategory(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }
        });

        recentProducts_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_btn.setEnabled(true);
            }
        });
        recentProducts_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setRecentProducts(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }
        });

        carouselSlide_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setCarouselSlide(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }
        });

        carouselTitles_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setShowCarouselTitle(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }
        });

        fullScreeMode_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentConfig.setFullScreenMode(isChecked);
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
                new IScreenUtility().fullScreenMode(mContext, getActivity());
            }
        });

        ArrayAdapter<String> categoryListAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, getAllCategoryLabels());
        categoryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        randomCat_x_sp.setAdapter(categoryListAdapter);
        randomCat_x_sp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                save_btn.setEnabled(true);
                return false;
            }
        });
        randomCat_x_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, " setOnItemSelectedListener : Test with no touch || "+parent.getItemAtPosition(position)+"");
                if ((parent.getItemAtPosition(position)+"").equals("Veuillez selection category")){
                    currentConfig.setRandomCategoryX("-1");
                }else {
                    currentConfig.setRandomCategoryX(getCategoryId(parent.getItemAtPosition(position)+""));
                }
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> carouselSizeAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, getCarouselSizes());
        carouselSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbProduct_sp.setAdapter(carouselSizeAdapter);
        nbProduct_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentConfig.setCarouselSize(Integer.valueOf(parent.getItemAtPosition(position)+""));
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> carouselSpeedAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, getCarouselSpeeds());
        carouselSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carouselSpeed_sp.setAdapter(carouselSpeedAdapter);
        carouselSpeed_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentConfig.setCarouselSpeed(Integer.valueOf(parent.getItemAtPosition(position)+""));
                db.configurationDao().updateConfig(currentConfig);
                displayCurrentConfig(currentConfig);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        displayCurrentConfig(currentConfig);

        save_btn.setEnabled(false);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToServer();
            }
        });
    }

    private void displayCurrentConfig(Configuration config){
        random_cb.setChecked(config.isRandomProduct());
        randomCat_cb.setChecked(config.isRandomCategory());
        recentProducts_cb.setChecked(config.isRecentProducts());
        carouselSlide_cb.setChecked(config.isCarouselSlide());
        carouselTitles_cb.setChecked(config.isShowCarouselTitle());
        fullScreeMode_cb.setChecked(config.isFullScreenMode());

        randomCat_x_sp.setSelection(setSelectedCategory(config.getRandomCategoryX()));
        nbProduct_sp.setSelection(setSelectedSize(config.getCarouselSize()));
        carouselSpeed_sp.setSelection(setSelectedSpeed(config.getCarouselSpeed()));

        String log = " DB Config:\n" +
                "ID: "+config.getId()+"\n" +
                "Random Product => "+config.isRandomProduct()+"\n" +
                "Random Category => "+config.isRandomCategory()+"\n" +
                "Random Cat X => "+config.getRandomCategoryX()+"\n" +
                "Recent Product => "+config.isRecentProducts()+"\n" +
                "Nombrer of Product => "+config.getCarouselSize()+"\n" +
                "Carrousel Slide => "+config.isCarouselSlide()+"\n" +
                "Speed Slide => "+config.getCarouselSpeed()+"\n";
        Log.e(TAG, log);
    }

    private int setSelectedCategory(String categoryID) {
        int categorySpinnerPosition = -1;
        String categoryName = null;

        Log.e(TAG, " setSelectedCategory : "+categoryID);
        if (categoryID == null || categoryID.isEmpty() || categoryID.equals("-1")){
            return 0;
        }

        List<CategorieEntry> categorieEntries = db.categorieDao().getAllCategories();
        for (int i=0; i<categorieEntries.size(); i++){
            if (categorieEntries.get(i).getId().equals(Long.valueOf(categoryID))){
                categoryName = categorieEntries.get(i).getLabel();
                break;
            }
        }

        List<String> allClients = getAllCategoryLabels();
        for (int index=0; index<allClients.size(); index++){
            if (allClients.get(index).equals(categoryName)){
                categorySpinnerPosition = index;
                break;
            }
        }

        return categorySpinnerPosition;
    }

    private String getCategoryId(String name){
        String categoryId = null;

        Log.e(TAG, " getCategoryId : "+name);
        if (name == null || name.isEmpty()){
            return null;
        }

        if(name.equals("Veuillez choisir une catégorie")){
            return "-1";
        }

        List<CategorieEntry> categorieEntries = db.categorieDao().getAllCategories();
        for (int i=0; i<categorieEntries.size(); i++){
            if (categorieEntries.get(i).getLabel().equals(name)){
                categoryId = categorieEntries.get(i).getId()+"";
                break;
            }
        }
        return categoryId;
    }

    private int setSelectedSize(int size){
        int sizeSpinnerPosition = -1;

        if (size == -1){
            return 0;
        }

        List<String> allSizes = getCarouselSizes();
        for (int index=0; index<allSizes.size(); index++){
            if (allSizes.get(index).equals(size+"")){
                sizeSpinnerPosition = index;
                break;
            }
        }
        return sizeSpinnerPosition;
    }

    private int setSelectedSpeed(int speed){
        int speedSpinnerPosition = -1;

        if (speed == -1){
            return 0;
        }

        List<String> allSpeeds = getCarouselSpeeds();
        for (int index=0; index<allSpeeds.size(); index++){
            if (allSpeeds.get(index).equals(speed+"")){
                speedSpinnerPosition = index;
                break;
            }
        }
        return speedSpinnerPosition;
    }

    private List<String> getAllCategoryLabels(){
        List<String> theList = new ArrayList<>();
        List<CategorieEntry> list = db.categorieDao().getAllCategories();

        theList.add("Veuillez choisir une catégorie");
        for (int i=0; i<list.size(); i++){
            theList.add(list.get(i).getLabel());
        }
        return theList;
    }

    private List<String> getCarouselSizes(){
        List<String> carouselSize = new ArrayList<>();

        int size = 0;
        while (size<=100){
            carouselSize.add(""+size);
            size += 10;
        }
        return carouselSize;
    }

    private List<String> getCarouselSpeeds(){
        List<String> speedList = new ArrayList<>();

        int speed = 0;
        while (speed<=30){
            speedList.add(""+speed);
            speed++;
        }

        return speedList;
    }

    private void saveToServer(){
        Log.e(TAG, " saveToServer");

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Configuration");
        progressDialog.setMessage("Enregistrement des configurations sur le server...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(mContext)) {
            Toast.makeText(mContext, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        Configuration currentConfigDB = db.configurationDao().getCurrentConfig().get(0);

        //Save the modification to the server
        Config config = new Config();
        config.setRowid("1");

        if (currentConfigDB.isRandomProduct()){
            config.setP_aleatoir("1");
        }else {
            config.setP_aleatoir("0");
        }

        if (currentConfigDB.isRandomCategory()){
            config.setA_category("1");
        }else {
            config.setA_category("0");
        }

        config.setCategory_x(currentConfigDB.getRandomCategoryX());

        if (currentConfigDB.isRecentProducts()){
            config.setP_recente("1");
        }else {
            config.setP_recente("0");
        }

        Call<Long> callUpdate = ApiUtils.getIScreenService(mContext).updateConfiguration(config.getRowid(), config.getP_aleatoir(), config.getA_category(), config.getCategory_x(), config.getP_recente());
        callUpdate.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, " saveToServer ===> onResponse : isSuccessful");
                    if (response.body() == 1){
                        progressDialog.dismiss();
                        save_btn.setEnabled(false);
                        Toast.makeText(mContext, "Les configurations sauvegardé au server!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.e(TAG, " saveToServer ===> onResponse : not isSuccessful");
                    progressDialog.dismiss();
                    Toast.makeText(mContext, getResources().getString(R.string.service_indisponible), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(TAG, " saveToServer ===> onFailure: "+t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(mContext, getResources().getString(R.string.service_indisponible), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
