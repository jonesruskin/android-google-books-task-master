package com.ersen.BookWorld.application;

import android.app.Application;

import com.ersen.BookWorld.constants.FaxiConstants;
import com.ersen.BookWorld.network.NetworkAPIs;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class FaxiApplication extends Application {
    private static FaxiApplication sInstance; //Get this application instance
    private Retrofit mRetrofitClient;
    private NetworkAPIs mNetworkAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static FaxiApplication getInstance() {
        return sInstance;
    }

    public NetworkAPIs getNetworkAPI(){
        if(mNetworkAPI == null){
            mNetworkAPI = getRetrofitClient().create(NetworkAPIs.class);
        }
        return  mNetworkAPI;
    }

    private Retrofit getRetrofitClient(){
        if (mRetrofitClient == null){
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);

            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(FaxiConstants.URLConstants.GOOGLE_BOOKS_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return mRetrofitClient;
    }
}
