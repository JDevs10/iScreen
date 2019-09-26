package com.example.iscreen.remote.model;

public class ProductVirtual {
    private String rowid;
    private String fk_product_pere;
    private String fk_product_fils;
    private String qty;
    private String ref;
    private String datec;
    private String label;
    private String description;
    private String note_public;
    private String note;
    private String price;
    private String price_ttc;
    private String price_min;
    private String price_min_ttc;
    private String price_base_type;
    private String tva_tx;
    private String seuil_stock_alerte;
    private String stock;
    private String local_poster_path;

    public ProductVirtual() {
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getFk_product_pere() {
        return fk_product_pere;
    }

    public void setFk_product_pere(String fk_product_pere) {
        this.fk_product_pere = fk_product_pere;
    }

    public String getFk_product_fils() {
        return fk_product_fils;
    }

    public void setFk_product_fils(String fk_product_fils) {
        this.fk_product_fils = fk_product_fils;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDatec() {
        return datec;
    }

    public void setDatec(String datec) {
        this.datec = datec;
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

    public String getNote_public() {
        return note_public;
    }

    public void setNote_public(String note_public) {
        this.note_public = note_public;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_ttc() {
        return price_ttc;
    }

    public void setPrice_ttc(String price_ttc) {
        this.price_ttc = price_ttc;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_min_ttc() {
        return price_min_ttc;
    }

    public void setPrice_min_ttc(String price_min_ttc) {
        this.price_min_ttc = price_min_ttc;
    }

    public String getPrice_base_type() {
        return price_base_type;
    }

    public void setPrice_base_type(String price_base_type) {
        this.price_base_type = price_base_type;
    }

    public String getTva_tx() {
        return tva_tx;
    }

    public void setTva_tx(String tva_tx) {
        this.tva_tx = tva_tx;
    }

    public String getSeuil_stock_alerte() {
        return seuil_stock_alerte;
    }

    public void setSeuil_stock_alerte(String seuil_stock_alerte) {
        this.seuil_stock_alerte = seuil_stock_alerte;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getLocal_poster_path() {
        return local_poster_path;
    }

    public void setLocal_poster_path(String local_poster_path) {
        this.local_poster_path = local_poster_path;
    }
}