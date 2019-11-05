package com.example.iscreen.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.iscreen.database.Dao.CategorieDao;
import com.example.iscreen.database.Dao.ConfigurationDao;
import com.example.iscreen.database.Dao.ProductDao;
import com.example.iscreen.database.Dao.ServerDao;
import com.example.iscreen.database.Dao.TokenDao;
import com.example.iscreen.database.Dao.UserDao;
import com.example.iscreen.database.entity.CategorieEntry;
import com.example.iscreen.database.entity.ServerEntry;
import com.example.iscreen.database.entity.TokenEntry;
import com.example.iscreen.database.entity.UserEntry;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.database.entity.ProduitEntry;

/**
 * Created by JL on 07/19/2019.
 */

@Database(entities = {ServerEntry.class, TokenEntry.class, UserEntry.class, Configuration.class, ProduitEntry.class, CategorieEntry.class},
        version = 10,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private final String TAG = AppDatabase.class.getSimpleName();

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "iscreen_database";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
//                Log.e(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries() // autorise Room a effectuer les requetes dans le main UI thread
                        .fallbackToDestructiveMigration() // regnere les table apres une incrementation de version
                        .build();
            }
        }
//        Log.e(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ConfigurationDao configurationDao();

    public abstract ProductDao productDao();

    public abstract CategorieDao categorieDao();

    public abstract ServerDao serverDao();

    public abstract TokenDao tokenDao();

    public abstract UserDao userDao();

}
