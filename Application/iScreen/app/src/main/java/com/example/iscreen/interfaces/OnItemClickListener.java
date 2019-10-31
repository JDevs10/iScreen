package com.example.iscreen.interfaces;

import android.view.View;

import com.example.iscreen.database.entity.ProduitEntry;

public interface OnItemClickListener {
    public void setOnItemClick(View view, ProduitEntry produitEntry);
}
