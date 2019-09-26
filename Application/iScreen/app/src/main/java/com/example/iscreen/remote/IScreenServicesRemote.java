package com.example.iscreen.remote;

import com.example.iscreen.remote.model.Categorie;
import com.example.iscreen.remote.model.Config;
import com.example.iscreen.remote.model.Internaute;
import com.example.iscreen.remote.model.InternauteSuccess;
import com.example.iscreen.remote.model.Product;
import com.example.iscreen.remote.model.ProductCustomerPrice;
import com.example.iscreen.remote.model.ProductVirtual;
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

    /*
    //  Recupération de la liste des thirdpartie(client, prospect, autre)
    @GET("thirdparties")
    Call<ArrayList<Thirdpartie>> findThirdpartie(@Query(ApiUtils.limit) long limit,
                                                 @Query(ApiUtils.page) long page,
                                                 @Query(ApiUtils.mode) int mode);

    //  Recupération d'un thirdpartie a partir de son id
    @GET("thirdparties/{thirdpartieId}")
    Call<Thirdpartie> findThirdpartieById(@Path("thirdpartieId") long thirdpartieId);

    //  Recupération du poster d'un produit
    @GET("documents/download")
    Call<DolPhoto> findProductsPoster(@Query(ApiUtils.module_part) String module_part,
                                      @Query(ApiUtils.original_file) String original_file);

    //  Recupération d'un document
    @GET("documents/download")
    Call<DolPhoto> findDocument(@Query(ApiUtils.module_part) String module_part,
                                @Query(ApiUtils.original_file) String original_file);

    //  Enregistrement d'un thirdparty
    @POST("thirdparties")
    Call<Long> saveThirdpartie(@Body Thirdpartie thirdpartie);

    //  Modification d'un thirdparty
    @PUT("thirdparties/{thirdpartiesId}")
    Call<Thirdpartie> updateThirdpartie(@Path("thirdpartiesId") Long thirdpartiesId,
                                        @Body Thirdpartie thirdpartie);

    //  suppression d'un thirdparty
    @DELETE("thirdparties/{thirdpartieId}")
    Call<Long> deleteThirdpartie(@Path("thirdpartieId") Long thirdpartieId);

    //  Enregistrement d'une commande client
    @POST("orders")
    Call<Long> saveCustomerOrder(@Body Order order);

    //  Recupération de la liste des commandes
    @GET("orders")
    Call<ArrayList<Order>> findOrders(@Query(ApiUtils.sqlfilters) String sqlfilters,
                                      @Query(ApiUtils.sortfield) String sortfield,
                                      @Query(ApiUtils.sortorder) String sortorder,
                                      @Query(ApiUtils.limit) long limit,
                                      @Query(ApiUtils.page) long page);

    //  Recupération des moyens de paiement
    @GET("setup/dictionary/payment_types")
    Call<ArrayList<PaymentTypes>> findPaymentTypes(@Query(ApiUtils.active) Integer active);

    //  Recupération de la liste des ligne d'une commandes
    @GET("orders/{orderId}/lines")
    Call<ArrayList<OrderLine>> findOrderLines(@Path("orderId") Long orderId);

    //  Valide un bon de commande
    @POST("orders/{orderId}/validate")
    Call<Order> validateCustomerOrder(@Path("orderId") Long orderId);

    //  Valide un bon de commande
    @POST("documents/upload")
    Call<String> uploadDocument(@Body Document document);
*/
    //  Recupération d'un user a partir de son login
    @GET("users")
    Call<ArrayList<User>> findUserByLogin(@Query(ApiUtils.sqlfilters) String sqlfilters);


    // ======== RYImg endpoinds  ==========

    //  Recupération des produit virtuels d'un produit
    @GET("product_virtual.php")
    Call<ArrayList<ProductVirtual>> ryFindProductVirtual(@Query(ApiUtils.id) Long productId);

    //  Recupération des produits affecté a un client
    @GET("product_customer_price.php")
    Call<List<ProductCustomerPrice>> ryFindProductPrice(@Query(ApiUtils.soc_id) Long productId);


}
