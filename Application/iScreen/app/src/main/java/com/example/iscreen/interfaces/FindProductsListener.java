package com.example.iscreen.interfaces;


import com.example.iscreen.remote.rest.FindProductsREST;

/**
 * Created by JL on 07/25/2019.
 */

public interface FindProductsListener {
    void onFindProductsCompleted(FindProductsREST findProductsREST);
}
