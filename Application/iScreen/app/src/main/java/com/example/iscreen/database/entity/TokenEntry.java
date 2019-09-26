package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JL on 07/19/2019.
 */

@Entity(tableName = "token")
public class TokenEntry {
    @PrimaryKey(autoGenerate = false)
    private Long id;
    private String token;
    private String message;

    public TokenEntry() {
    }

    @Ignore
    public TokenEntry(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
