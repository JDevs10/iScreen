package com.example.iscreen.model;

import com.example.iscreen.database.entity.ProduitEntry;

import java.util.List;

public class Carousel {
    private List<ProduitEntry> randomProductList;
    private List<ProduitEntry> randomFromEachCategoryList;
    private List<ProduitEntry> randomFromSelectedCategoryList;
    private List<ProduitEntry> recentProductList;

    public Carousel() {
    }

    public Carousel(List<ProduitEntry> randomProductList, List<ProduitEntry> randomFromEachCategoryList, List<ProduitEntry> randomFromSelectedCategoryList, List<ProduitEntry> recentProductList){
        this.randomProductList = randomProductList;
        this.randomFromEachCategoryList = randomFromEachCategoryList;
        this.randomFromSelectedCategoryList = randomFromSelectedCategoryList;
        this.recentProductList = recentProductList;
    }

    public List<ProduitEntry> getRandomProductList() {
        return randomProductList;
    }

    public void setRandomProductList(List<ProduitEntry> randomProductList) {
        this.randomProductList = randomProductList;
    }

    public List<ProduitEntry> getRandomFromEachCategoryList() {
        return randomFromEachCategoryList;
    }

    public void setRandomFromEachCategoryList(List<ProduitEntry> randomFromEachCategoryList) {
        this.randomFromEachCategoryList = randomFromEachCategoryList;
    }

    public List<ProduitEntry> getRandomFromSelectedCategoryList() {
        return randomFromSelectedCategoryList;
    }

    public void setRandomFromSelectedCategoryList(List<ProduitEntry> randomFromSelectedCategoryList) {
        this.randomFromSelectedCategoryList = randomFromSelectedCategoryList;
    }

    public List<ProduitEntry> getRecentProductList() {
        return recentProductList;
    }

    public void setRecentProductList(List<ProduitEntry> recentProductList) {
        this.recentProductList = recentProductList;
    }
}
