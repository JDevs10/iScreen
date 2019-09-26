package com.example.iscreen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JL on 07/19/2019.
 */

@Entity(tableName = "categorie")
public class CategorieEntry {
    @PrimaryKey(autoGenerate = false)
    private Long id;
    private String fk_parent;
    private String label;
    private String description;
    private String color;
    private String type;
    private String ref;
    private String ref_ext;
    private String visible;
    private String entity;
    private String poster_name;
    private String poster_content;
    private Integer count_produits;

    public CategorieEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFk_parent() {
        return fk_parent;
    }

    public void setFk_parent(String fk_parent) {
        this.fk_parent = fk_parent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef_ext() {
        return ref_ext;
    }

    public void setRef_ext(String ref_ext) {
        this.ref_ext = ref_ext;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }

    public String getPoster_content() {
        return poster_content;
    }

    public void setPoster_content(String poster_content) {
        this.poster_content = poster_content;
    }

    public Integer getCount_produits() {
        return count_produits;
    }

    public void setCount_produits(Integer count_produits) {
        this.count_produits = count_produits;
    }
}
