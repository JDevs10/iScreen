package com.example.iscreen.remote;

import com.example.iscreen.remote.model.Categorie;
import com.example.iscreen.remote.model.Config;
import com.example.iscreen.remote.model.Internaute;
import com.example.iscreen.remote.model.InternauteSuccess;
import com.example.iscreen.remote.model.Product;
import com.example.iscreen.remote.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by JL on 07/19/2019.
 */

public interface IScreenServicesRemote {

    //    COnnexion d'un internaute
    @POST("login")
    Call<InternauteSuccess> login(@Body Internaute internaute);

    /** ============================= Server Configuration ===================================== **/
    @POST("iscreenapi/createConfig/{p_aleatoir_iscreen}/{a_category_iscreen}/{category_x_iscreen}/{p_recente_iscreen}")
    Call<Long> createConfiguration(Config config);

    @GET("iscreenapi/getConfig/{id}")
    Call<Config> getConfiguration(@Path("id") Long id);

    @PUT("iscreenapi/updateConfig/{rowid}/{p_aleatoir_iscreen}/{a_category_iscreen}/{category_x_iscreen}/{p_recente_iscreen}")
    Call<Long> updateConfiguration(@Path("rowid") String rowid,
                                   @Path("p_aleatoir_iscreen") String p_aleatoir_iscreen,
                                   @Path("a_category_iscreen") String a_category_iscreen,
                                   @Path("category_x_iscreen") String category_x_iscreen,
                                   @Path("p_recente_iscreen") String p_recente_iscreen);

    @DELETE("iscreenapi/deleteConfig{rowid}")
    Call<Long> deleteConfiguration(@Path("rowid") Long rowid);

    /** ============================ End Server Configuration ================================== **/

    //  Recupération de la liste des produits
    @GET("products")
    Call<ArrayList<Product>> findProductsByCategorie(@Query(ApiUtils.sqlfilters) String sqlfilters,
                                                     @Query(ApiUtils.sortfield) String sortfield,
                                                     @Query(ApiUtils.sortorder) String sortorder,
                                                     @Query(ApiUtils.limit) long limit,
                                                     @Query(ApiUtils.category) long category,
                                                     @Query(ApiUtils.mode) int mode);

    //  Recupération de la liste des produits
    @GET("products")
    Call<ArrayList<Product>> findProducts(@Query(ApiUtils.sqlfilters) String sqlfilters,
                                          @Query(ApiUtils.sortfield) String sortfield,
                                          @Query(ApiUtils.sortorder) String sortorder,
                                          @Query(ApiUtils.limit) long limit,
                                          @Query(ApiUtils.page) long page,
                                          @Query(ApiUtils.mode) int mode);

    //  Enregistrement d'une categorie
    @POST("categories")
    Call<Long> saveCategorie(@Body Categorie categorie);

    //  Recupération de la liste des categories
    @GET("categories")
    Call<ArrayList<Categorie>> findCategories(@Query(ApiUtils.sortfield) String sortfield,
                                              @Query(ApiUtils.sortorder) String sortorder,
                                              @Query(ApiUtils.limit) long limit,
                                              @Query(ApiUtils.page) long page,
                                              @Query(ApiUtils.type) String type);

    //  Recupération d'un user a partir de son login
    @GET("users")
    Call<ArrayList<User>> findUserByLogin(@Query(ApiUtils.sqlfilters) String sqlfilters);

}
