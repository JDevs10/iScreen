package com.example.iscreen.remote.rest;

import com.example.iscreen.remote.model.Config;

import java.util.ArrayList;

/**
 * Created by JL on 07/19/2019.
 */

public class FindConfigurationREST extends IScreenREST {
    private Config configs;
    private long config_id;

    public FindConfigurationREST() {
    }

    public FindConfigurationREST(Config configs) {
        this.configs = configs;
    }

    public FindConfigurationREST(Config configs, long config_id) {
        this.configs = configs;
        this.config_id = config_id;
    }

    public FindConfigurationREST(int errorCode, String errorBody) {
        this.errorCode = errorCode;
        this.errorBody = errorBody;
    }

    public Config getConfigs() {
        return configs;
    }

    public void setConfigs(Config configs) {
        this.configs = configs;
    }

    public long getConfig_id() {
        return config_id;
    }

    public void setConfig_id(long config_id) {
        this.config_id = config_id;
    }
}
