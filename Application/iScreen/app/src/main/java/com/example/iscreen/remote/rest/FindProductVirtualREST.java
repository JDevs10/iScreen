package com.example.iscreen.remote.rest;

import com.example.iscreen.remote.model.ProductVirtual;

import java.util.ArrayList;

public class FindProductVirtualREST extends IScreenREST {
    private ArrayList<ProductVirtual> productVirtuals;
    private long product_parent_id;

    public FindProductVirtualREST() {
    }

    public FindProductVirtualREST(ArrayList<ProductVirtual> products) {
        this.productVirtuals = products;
    }

    public FindProductVirtualREST(ArrayList<ProductVirtual> products, long parent_id) {
        this.productVirtuals = products;
        this.product_parent_id = parent_id;
    }

    public FindProductVirtualREST(int errorCode, String errorBody) {
        this.errorCode = errorCode;
        this.errorBody = errorBody;
    }

    public ArrayList<ProductVirtual> getProductVirtuals() {
        return productVirtuals;
    }

    public void setProductVirtuals(ArrayList<ProductVirtual> productVirtuals) {
        this.productVirtuals = productVirtuals;
    }

    public long getProduct_parent_id() {
        return product_parent_id;
    }

    public void setProduct_parent_id(long product_parent_id) {
        this.product_parent_id = product_parent_id;
    }
}
