package com.example.iscreen.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.iscreen.database.Dao.ConfigurationDao;
import com.example.iscreen.database.Dao.ProductDao;
import com.example.iscreen.database.entity.Configuration;
import com.example.iscreen.database.entity.ProduitEntry;


@Database(entities = {Configuration.class, ProduitEntry.class},
        version = 3,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private final String TAG = AppDatabase.class.getSimpleName();

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "isales_store";
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














    /*
    public static final String CONFIG_TABLE_NAME = "configuration";
    public static final String CONFIG_TABLE_COL_1 = "id";
    public static final String CONFIG_TABLE_COL_2 = "random";
    public static final String CONFIG_TABLE_COL_3 = "lastProducts";
    public static final String CONFIG_TABLE_COL_4 = "category";
    public static final String CONFIG_TABLE_COL_5 = "promotion";
    public static final String CONFIG_TABLE_COL_6 = "promotion";
    public static final String CONFIG_TABLE_COL_7 = "promotion";
    public static final String CONFIG_TABLE_COL_8 = "promotion";

    private static final String CREATE_CONFIG_TABLE = "create table "+CONFIG_TABLE_NAME+" (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CONFIG_TABLE_COL_1+" TEXT, "+
            CONFIG_TABLE_COL_2+" TEXT, "+
            CONFIG_TABLE_COL_3+" TEXT, "+
            CONFIG_TABLE_COL_4+" TEXT, "+
            CONFIG_TABLE_COL_5+" TEXT) ";

    private static final String DROPE_CONFIG = "DROP TABLE IF EXISTs "+CONFIG_TABLE_NAME;

    public AppDatabase(Context context) {
        super(context, "iscreen_db", null, 1);
        Log.e("DB: ", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONFIG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROPE_CONFIG);
        onCreate(db);
    }


    public int insertConfig(Configuration config){

    }

    public ArrayList<Configuration> getCurrentConfig(){

    }

    public void deleteConfig(long id){

    }
    */

}
