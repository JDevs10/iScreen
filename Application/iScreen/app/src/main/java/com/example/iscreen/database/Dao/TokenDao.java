package com.example.iscreen.database.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.iscreen.database.entity.TokenEntry;

import java.util.List;

/**
 * Created by netserve on 07/10/2018.
 */

@Dao
public interface TokenDao {
    @Query("SELECT * FROM token")
    List<TokenEntry> getAllToken();

    @Insert
    void insertToken(TokenEntry tokenEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateToken(TokenEntry tokenEntry);

    @Delete
    void deleteToken(TokenEntry tokenEntry);

    @Query("DELETE FROM token")
    void deleteAllToken();

}
