package com.example.iscreen.pages.home.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.Configuration;

public class Settings extends Fragment {
    private final String TAG = Catalog.class.getSimpleName();
    private Context mContext;
    private Configuration currentConfig;
    private AppDatabase db;

    private CheckBox random_cb, randomCat_cb, recentProducts_cb;

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
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        random_cb = view.findViewById(R.id.fragment_config_random_cb);
        randomCat_cb = view.findViewById(R.id.fragment_config_random_category_cb);
        recentProducts_cb = view.findViewById(R.id.fragment_config_recent_product_cb);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentConfig = db.configurationDao().getCurrentConfig().get(0);
        displayCurrentConfig(db.configurationDao().getCurrentConfig().get(0));
    }

    private void displayCurrentConfig(Configuration config){
        random_cb.setChecked(config.isRandom());
        randomCat_cb.setChecked(config.isPromotion());
        recentProducts_cb.setChecked(config.isRecentProducts());
    }
}
