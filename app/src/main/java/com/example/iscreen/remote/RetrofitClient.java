package com.example.iscreen.remote;

import android.content.Context;

import com.example.iscreen.database.AppDatabase;
import com.example.iscreen.database.entity.TokenEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JL on 07/19/2019.
 */

public class RetrofitClient {

    public static final String TAG = RetrofitClient.class.getSimpleName();
    private static Retrofit retrofit = null;

    public static Retrofit getClient(final Context context, String url) {
//        Log.e(TAG, "getClient: input baseURL="+url );
//        if (retrofit == null) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//                    .addInterceptor(logging)
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();

//                            Log.e(TAG, "intercept:Before "+originalHttpUrl.toString());

//                            if it is login query, don't add dolapikey
                            if (originalHttpUrl.toString().contains("/login")) {
                                return chain.proceed(original);
                            }
                            AppDatabase mDb = AppDatabase.getInstance(context.getApplicationContext());
                            TokenEntry tokenEntry = mDb.tokenDao().getAllToken().get(0);
//                            Log.e(TAG, "intercept: token="+tokenEntry.getToken());

//                            Adding DOLIBARR API KEY to all queries
                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter(ApiUtils.DOLAPIKEY, tokenEntry.getToken())
                                    .build();
//                            Log.e(TAG, "intercept:After url= "+url.toString());

                            // Request customization: add request headers
                            Request.Builder requestBuilder = original.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
            retrofit = new Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(url)
                    .build();
//        }
        return retrofit;
    }
}
