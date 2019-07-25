package com.example.iscreen.remote.rest;

import com.example.iscreen.remote.model.Product;

import java.util.ArrayList;

/**
 * Created by JL on 07/19/2019.
 */

public class FindProductsREST extends IScreenREST {
    private ArrayList<Product> products;
    private long categorie_id;

    public FindProductsREST() {
    }

    public FindProductsREST(ArrayList<Product> products) {
        this.products = products;
    }

    public FindProductsREST(ArrayList<Product> products, long categorie_id) {
        this.products = products;
        this.categorie_id = categorie_id;
    }

    public FindProductsREST(int errorCode, String errorBody) {
        this.errorCode = errorCode;
        this.errorBody = errorBody;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public long getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(long categorie_id) {
        this.categorie_id = categorie_id;
    }
}
