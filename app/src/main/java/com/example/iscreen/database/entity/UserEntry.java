package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JL on 07/19/2019.
 */

@Entity(tableName = "user")
public class UserEntry {
    @PrimaryKey(autoGenerate = false)
    private Long id;
    public String statut;
    public String employee;
    public String gender;
    public String birth;
    public String email;
    public String firstname;
    public String lastname;
    public String name;
    public String country;
    public String dateemployment;
    public String photo;
    public String datelastlogin;
    public String datec;
    public String datem;
    public String admin;
    public String login;
    public String town;
    public String address;

    public UserEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateemployment() {
        return dateemployment;
    }

    public void setDateemployment(String dateemployment) {
        this.dateemployment = dateemployment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDatelastlogin() {
        return datelastlogin;
    }

    public void setDatelastlogin(String datelastlogin) {
        this.datelastlogin = datelastlogin;
    }

    public String getDatec() {
        return datec;
    }

    public void setDatec(String datec) {
        this.datec = datec;
    }

    public String getDatem() {
        return datem;
    }

    public void setDatem(String datem) {
        this.datem = datem;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
