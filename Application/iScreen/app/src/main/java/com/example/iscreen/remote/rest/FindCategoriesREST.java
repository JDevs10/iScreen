package com.example.iscreen.remote.rest;

import com.example.iscreen.remote.model.Categorie;

import java.util.ArrayList;

/**
 * Created by JL on 07/19/2019.
 */

public class FindCategoriesREST extends IScreenREST {
    private ArrayList<Categorie> categories;

    public FindCategoriesREST() {
    }

    public FindCategoriesREST(ArrayList<Categorie> categories) {
        this.categories = categories;
    }

    public FindCategoriesREST(int errorCode, String errorBody) {
        this.errorCode = errorCode;
        this.errorBody = errorBody;
    }

    public ArrayList<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categorie> categories) {
        this.categories = categories;
    }
}
