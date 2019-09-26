package com.example.iscreen.remote.model;

/**
 * Created by JL on 07/19/2019.
 */

public class Internaute {
    private String login;
    private String password;
    private String entity;
    private Integer reset;

    public Internaute() {
    }

    public Internaute(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getReset() {
        return reset;
    }

    public void setReset(Integer reset) {
        this.reset = reset;
    }
}
