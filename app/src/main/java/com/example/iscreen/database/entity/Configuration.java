package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JL on 07/19/2019.
 */

@Entity(tableName = "config")
public class Configuration {
    @PrimaryKey //(autoGenerate = true)
    private int id;

    /**
     * For Carrousel display setup
     */
    private boolean randomProduct;
    private boolean randomCategory;
    private String randomCategoryX;
    private boolean recentProducts;
    private int carouselSize;
    private boolean carouselSlide;
    private int carouselSpeed;

    public Configuration() {
    }

    public Configuration(int id, boolean randomProduct, boolean randomCategory, String randomCategoryX, boolean recentProducts, int carouselSize, boolean carouselSlide, int carouselSpeed) {
        this.id = id;
        this.randomProduct = randomProduct;
        this.randomCategory = randomCategory;
        this.randomCategoryX = randomCategoryX;
        this.recentProducts = recentProducts;
        this.carouselSize = carouselSize;
        this.carouselSlide = carouselSlide;
        this.carouselSpeed = carouselSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRandomProduct() {
        return randomProduct;
    }

    public void setRandomProduct(boolean randomProduct) {
        this.randomProduct = randomProduct;
    }

    public boolean isRandomCategory() {
        return randomCategory;
    }

    public void setRandomCategory(boolean randomCategory) {
        this.randomCategory = randomCategory;
    }

    public String getRandomCategoryX() {
        return randomCategoryX;
    }

    public void setRandomCategoryX(String randomCategoryX) {
        this.randomCategoryX = randomCategoryX;
    }

    public boolean isRecentProducts() {
        return recentProducts;
    }

    public void setRecentProducts(boolean recentProducts) {
        this.recentProducts = recentProducts;
    }

    public int getCarouselSize() {
        return carouselSize;
    }

    public void setCarouselSize(int carouselSize) {
        this.carouselSize = carouselSize;
    }

    public boolean isCarouselSlide() {
        return carouselSlide;
    }

    public void setCarouselSlide(boolean carouselSlide) {
        this.carouselSlide = carouselSlide;
    }

    public int getCarouselSpeed() {
        return carouselSpeed;
    }

    public void setCarouselSpeed(int carouselSpeed) {
        this.carouselSpeed = carouselSpeed;
    }
}
