package com.example.iscreen.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.iscreen.interfaces.FindConfigurationListener;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.remote.model.Config;
import com.example.iscreen.remote.rest.FindConfigurationREST;
import com.example.iscreen.remote.rest.FindProductsREST;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by JL on 07/19/2019.
 */

public class FindConfigurationTask extends AsyncTask<Void, Void, FindConfigurationREST> {
    private static final String TAG = FindConfigurationTask.class.getSimpleName();

    private FindConfigurationListener task;
    private long rowid;
    private Context context;

    public FindConfigurationTask(Context context, FindConfigurationListener taskComplete, long rowid) {
        this.task = taskComplete;
        this.rowid = rowid;
        this.context = context;
    }

    @Override
    protected FindConfigurationREST doInBackground(Void... voids) {
        Call<Config> call = ApiUtils.getIScreenService(context).getConfiguration(rowid);
        try {
            Response<Config> response = call.execute();
            if (response.isSuccessful()) {
                Config productArrayList = response.body();

                return new FindConfigurationREST(productArrayList, rowid);
            } else {
                String error = null;
                FindConfigurationREST findConfigurationREST = new FindConfigurationREST();
                findConfigurationREST.setConfigs(null);
                findConfigurationREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    findConfigurationREST.setErrorBody(error);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return findConfigurationREST;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(FindConfigurationREST findConfigurationREST) {
        task.onFindConfiguration(findConfigurationREST);
    }
}
