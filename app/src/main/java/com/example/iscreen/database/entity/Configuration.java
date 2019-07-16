package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "config")
public class Configuration {
    @PrimaryKey //(autoGenerate = true)
    private int id;

    /**
     * For Carousel display setup
     */
    private boolean random;
    private boolean recentProducts;
    private int category;
    private boolean promotion;

    /**
     * Detail Display
     * split screen = true ? false
     */
    private boolean splitScreen;

    /**
     *  First trigger to get server config
     */
    private boolean gotConfig;

    public Configuration() {
    }

    public Configuration(int id, boolean random, boolean recentProducts, int category, boolean promotion, boolean splitScreen, boolean gotConfig) {
        this.id = id;
        this.random = random;
        this.recentProducts = recentProducts;
        this.category = category;
        this.promotion = promotion;
        this.splitScreen = splitScreen;
        this.gotConfig = gotConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isRecentProducts() {
        return recentProducts;
    }

    public void setRecentProducts(boolean recentProducts) {
        this.recentProducts = recentProducts;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public boolean isSplitScreen() {
        return splitScreen;
    }

    public void setSplitScreen(boolean splitScreen) {
        this.splitScreen = splitScreen;
    }

    public boolean isGotConfig() {
        return gotConfig;
    }

    public void setGotConfig(boolean gotConfig) {
        this.gotConfig = gotConfig;
    }
}
