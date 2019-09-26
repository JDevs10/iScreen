package com.example.iscreen.remote.model;

/**
 * Created by JL on 07/19/2019.
 */

public class Categorie {
    private String fk_parent;
    private String label;
    private String description;
    private String color;
    private String type;
    private String id;
    private String ref;
    private String ref_ext;
    private String visible;
    private String entity;

    public Categorie() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
