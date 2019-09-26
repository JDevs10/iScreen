package com.example.iscreen.interfaces;

import com.example.iscreen.database.entity.ProduitEntry;

import java.util.List;

public interface ProduitsAdapterListener {
    void onDetailsSelected(ProduitEntry product);
}
