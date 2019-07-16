package com.example.iscreen.pages.home;

import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.pages.home.fragments.Catalog;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hides App bar at the top..................................................................
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        toolbar = findViewById(R.id.toolbar);
        //starting with Client_FragmentFindMerchant()
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_homeLayout);
        navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().hide();
        if (savedInstanceState == null){
            //default fragment when activity is running
            navigationView.setCheckedItem(R.id.nav_affichage);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Catalog()).commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_affichage:
                //init Side Menu user information
                //initMenuHeaderInfo(navigationView);

                toolbar.setTitle("");
                navigationView.setCheckedItem(R.id.nav_affichage);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Catalog()).commit();
                break;

            case R.id.nav_settings:
                //init Side Menu user information
                //initMenuHeaderInfo(navigationView);

                toolbar.setTitle("Stores");
                navigationView.setCheckedItem(R.id.nav_settings);
                //getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Client_FragmentFindMerchant()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
