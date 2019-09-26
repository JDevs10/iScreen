package com.example.iscreen.interfaces;


import com.example.iscreen.remote.rest.FindCategoriesREST;

/**
 * Created by JL on 07/25/2019.
 */

public interface FindCategorieListener {
    void onFindCategorieCompleted(FindCategoriesREST findCategoriesREST);
}
