package com.example.iscreen.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.iscreen.database.entity.ProduitEntry;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    void insertProduit(ProduitEntry produitEntry);

    @Query("SELECT * FROM produit")
    List<ProduitEntry> getProducts();

    @Query("SELECT * FROM produit ORDER BY label")
    List<ProduitEntry> getAllProducts();

    @Query("SELECT * FROM produit WHERE id = :id")
    ProduitEntry getProductById(long id);

    @Query("SELECT * FROM produit ORDER BY date_creation DESC")
    List<ProduitEntry> loadRecentProduct();

    @Query("SELECT * FROM produit WHERE id = :id")
    List<ProduitEntry> loadProductById(int id);

    @Query("SELECT * FROM produit WHERE categorie_id = :id")
    List<ProduitEntry> getProductsByCategory(long id);

    @Query("UPDATE produit SET file_content=:localPath WHERE id = :id")
    void updateLocalImgPath(long id, String localPath);

    @Query("DELETE FROM produit")
    void deleteAllProducts();
}
