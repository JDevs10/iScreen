package com.example.iscreen.pages.home;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iscreen.R;
import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.pages.Loading;
import com.example.iscreen.pages.home.fragments.Affichage;
import com.example.iscreen.pages.home.fragments.Parametre;
import com.example.iscreen.remote.ConnectionManager;
import com.example.iscreen.utility.IScreenUtility;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static AppDatabase db;

    private ImageView icon;

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

        icon = navigationView.getHeaderView(0).findViewById(R.id.nav_header_currentUserImage);
        icon.setImageResource(R.mipmap.ic_launcher);

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
}
