package com.example.iscreen.remote.rest;

/**
 * Created by JL on 07/19/2019.
 */

public class IScreenREST {
    protected int errorCode;
    protected String errorBody;

    public IScreenREST() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }
}
