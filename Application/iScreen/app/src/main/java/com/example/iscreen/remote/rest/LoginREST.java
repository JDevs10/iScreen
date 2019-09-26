package com.example.iscreen.remote.rest;


import com.example.iscreen.remote.model.InternauteSuccess;

/**
 * Created by JL on 07/19/2019.
 */

public class LoginREST extends IScreenREST {
    private InternauteSuccess internauteSuccess;

    public LoginREST() {
    }

    public LoginREST(InternauteSuccess internauteSuccess) {
        this.internauteSuccess = internauteSuccess;
    }

    public LoginREST(int errorCode, String errorBody) {
        this.errorCode = errorCode;
        this.errorBody = errorBody;
    }

    public InternauteSuccess getInternauteSuccess() {
        return internauteSuccess;
    }

    public void setInternauteSuccess(InternauteSuccess internauteSuccess) {
        this.internauteSuccess = internauteSuccess;
    }

}
