package com.example.iscreen.database.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.iscreen.database.entity.Configuration;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConfig(Configuration config);

    @Query("SELECT * FROM config")
    List<Configuration> getCurrentConfig();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateConfig(Configuration config);

    @Delete
    void deleteConfig(Configuration config);
}
