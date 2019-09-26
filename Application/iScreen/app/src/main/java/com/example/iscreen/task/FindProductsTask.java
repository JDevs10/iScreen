package com.example.iscreen.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.iscreen.interfaces.FindProductsListener;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.remote.model.Product;
import com.example.iscreen.remote.rest.FindProductsREST;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by JL on 07/19/2019.
 */

public class FindProductsTask extends AsyncTask<Void, Void, FindProductsREST> {
    private static final String TAG = FindProductsTask.class.getSimpleName();

    private FindProductsListener task;
    private String sortfield;
    private String sortorder;
    private long limit;
    private long page;
    private long category;
    private int mode = 1;
    private Context context;

    /**
     *
     * @param context
     * @param taskComplete
     * @param sortfield
     * @param sortorder
     * @param limit
     * @param page envoyer une valeur négative pour renvoyer la liste sans pagination
     * @param category
     */
    public FindProductsTask(Context context, FindProductsListener taskComplete, String sortfield, String sortorder, long limit, long page, long category) {
        this.task = taskComplete;
        this.sortfield = sortfield;
        this.sortorder = sortorder;
        this.limit = limit;
        this.page = page;
        this.category = category;
        this.context = context;
    }

    @Override
    protected FindProductsREST doInBackground(Void... voids) {
//        filtre les produits qui sont en vente
        String sqlfilters = "tosell=1";
//        Requete de connexion de l'internaute sur le serveur
        Call<ArrayList<Product>> call = null;
        if (page < 0) {
            call = ApiUtils.getIScreenService(context).findProductsByCategorie(sqlfilters, sortfield, this.sortorder, this.limit, this.category, this.mode);
        } else {
            call = ApiUtils.getIScreenService(context).findProducts(sqlfilters, sortfield, this.sortorder, this.limit, this.page, this.mode);
        }
        try {
            Response<ArrayList<Product>> response = call.execute();
            if (response.isSuccessful()) {
                ArrayList<Product> productArrayList = response.body();
                ArrayList<Product> products = new ArrayList<>();
//                Log.e(TAG, "doInBackground: products=" + productArrayList.size());

                return new FindProductsREST(productArrayList, category);
            } else {
                String error = null;
                FindProductsREST findProductsREST = new FindProductsREST();
                findProductsREST.setProducts(null);
                findProductsREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    findProductsREST.setErrorBody(error);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return findProductsREST;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(FindProductsREST findProductsREST) {
//        super.onPostExecute(findProductsREST);
        task.onFindProductsCompleted(findProductsREST);
    }
}
