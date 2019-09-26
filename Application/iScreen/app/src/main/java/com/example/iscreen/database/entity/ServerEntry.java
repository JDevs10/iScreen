package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JL on 07/19/2019.
 */

@Entity(tableName = "server")
public class ServerEntry {
    @PrimaryKey(autoGenerate = false)
    private Long id;
    private String hostname;
    private String hostname_img;
    private String raison_sociale;
    private String adresse;
    private String code_postal;
    private String ville;
    private String departement;
    private String pays;
    private String devise;
    private String telephone;
    private String mail;
    private String website;
    private String note;
    private String title;
    private Boolean is_active;

    public ServerEntry() {

    }

    @Ignore
    public ServerEntry(String title, String hostname, String hostname_img, Boolean is_active) {
        this.title = title;
        this.hostname = hostname;
        this.hostname_img = hostname_img;
        this.is_active = is_active;
    }

    @Ignore
    public ServerEntry(String hostname, String hostname_img, String raison_sociale, String adresse, String code_postal, String ville, String departement, String pays, String devise, String telephone, String mail, String website, String note, String title, Boolean is_active) {
        this.hostname = hostname;
        this.hostname_img = hostname_img;
        this.raison_sociale = raison_sociale;
        this.adresse = adresse;
        this.code_postal = code_postal;
        this.ville = ville;
        this.departement = departement;
        this.pays = pays;
        this.devise = devise;
        this.telephone = telephone;
        this.mail = mail;
        this.website = website;
        this.note = note;
        this.title = title;
        this.is_active = is_active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRaison_sociale() {
        return raison_sociale;
    }

    public void setRaison_sociale(String raison_sociale) {
        this.raison_sociale = raison_sociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHostname_img() {
        return hostname_img;
    }

    public void setHostname_img(String hostname_img) {
        this.hostname_img = hostname_img;
    }
}
