package com.example.iscreen.pages.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.pages.Loading;
import com.example.iscreen.pages.home.fragments.Affichage;
import com.example.iscreen.pages.home.fragments.Parametre;

/**
 * Created by JL on 07/19/2019.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hides App bar at the top..................................................................
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

            case R.id.nav_disconnect:
                //init Side Menu user information
                //initMenuHeaderInfo(navigationView);

                db.userDao().deleteAllUser();
                db.tokenDao().deleteAllToken();
                db.categorieDao().deleteAllCategorie();
                db.productDao().deleteAllProducts();
                db.configurationDao().deleteAllConfig();
                startActivity(new Intent(HomeActivity.this, Loading.class));
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
