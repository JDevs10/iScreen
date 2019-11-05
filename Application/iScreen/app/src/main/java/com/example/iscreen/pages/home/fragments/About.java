package com.example.iscreen.pages.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.utility.IScreenUtility;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class About extends Fragment {
    private final String TAG = Affichage.class.getSimpleName();
    private Context mContext;
    private AppDatabase db;

    private TextView versionApp;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

//        random_cb = view.findViewById(R.id.fragment_config_random_cb);
//        randomCat_cb = view.findViewById(R.id.fragment_config_random_category_cb);
//        randomCat_x_sp = view.findViewById(R.id.fragment_config_random_categoryX_sp);
//        recentProducts_cb = view.findViewById(R.id.fragment_config_recent_product_cb);
//        nbProduct_sp = view.findViewById(R.id.fragment_config_nbProduct);
//        carouselSlide_cb = view.findViewById(R.id.fragment_config_carouselSlide_cb);
//        carouselSpeed_sp = view.findViewById(R.id.fragment_config_carouselSpeed);
//        carouselTitles_cb = view.findViewById(R.id.fragment_config_carouselTitles_cb);
//        fullScreeMode_cb = view.findViewById(R.id.fragment_config_fullScreeMode_cb);
//
//        save_btn = view.findViewById(R.id.fragment_config_save_btn);
        versionApp = view.findViewById(R.id.fragment_a_propos_versionApp_tv);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PackageManager pm = getContext().getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(getContext().getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        versionApp.setText("Version: "+pInfo.versionName);
    }
}
