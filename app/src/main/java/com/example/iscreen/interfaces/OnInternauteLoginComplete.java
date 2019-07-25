package com.example.iscreen.interfaces;

import com.example.iscreen.remote.rest.LoginREST;

/**
 * Created by JL on 07/25/2019.
 */

public interface OnInternauteLoginComplete {
    void onInternauteLoginTaskComplete(LoginREST loginREST);
}
