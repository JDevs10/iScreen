package com.example.iscreen.interfaces;


import com.example.iscreen.remote.rest.FindProductsREST;

/**
 * Created by netserve on 29/08/2018.
 */

public interface FindProductsListener {
    void onFindProductsCompleted(FindProductsREST findProductsREST);
    void onFindAllProductsCompleted();
}
