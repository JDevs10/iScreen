package com.example.iscreen.interfaces;


import com.example.iscreen.remote.rest.FindCategoriesREST;

/**
 * Created by netserve on 05/09/2018.
 */

public interface FindCategorieListener {
    void onFindCategorieCompleted(FindCategoriesREST findCategoriesREST);
}
