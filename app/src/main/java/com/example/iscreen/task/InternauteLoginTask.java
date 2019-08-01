package com.example.iscreen.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.iscreen.interfaces.OnInternauteLoginComplete;
import com.example.iscreen.remote.ApiUtils;
import com.example.iscreen.remote.model.Internaute;
import com.example.iscreen.remote.model.InternauteSuccess;
import com.example.iscreen.remote.rest.LoginREST;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by JL on 07/19/2019.
 */

public class InternauteLoginTask extends AsyncTask<Void, Void, LoginREST> {
    private static final String TAG = InternauteLoginTask.class.getSimpleName();

    private OnInternauteLoginComplete taskComplete;
    private Internaute internaute;

    private Context context;

    public InternauteLoginTask(Context context, OnInternauteLoginComplete task, Internaute internaute) {
        this.taskComplete = task;
        this.internaute = internaute;
        this.context = context;
    }

    @Override
    protected LoginREST doInBackground(Void... voids) {
//        Requete de connexion de l'internaute sur le serveur
        Call<InternauteSuccess> call = ApiUtils.getIScreenService(context).login(this.internaute);
        try {
            Response<InternauteSuccess> response = call.execute();
            if (response.isSuccessful()) {
                InternauteSuccess internauteSuccess = response.body();
                Log.e(TAG, "doInBackground: internauteSuccess=" + internauteSuccess.getSuccess().getToken());
                return new LoginREST(internauteSuccess);
            } else {
                String error = null;
                LoginREST loginREST = new LoginREST();
                loginREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    loginREST.setErrorBody(error);

                } catch (IOException e) {

                    e.printStackTrace();
                }

                return loginREST;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(LoginREST loginREST) {
//        super.onPostExecute(internauteSuccess);
        this.taskComplete.onInternauteLoginTaskComplete(loginREST);
    }
}
