package com.example.bimapp.network;


import com.example.bimapp.interfaces.Constant;
import com.example.bimapp.model.AudioModel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static ApiClient instance;
    private static final String BASE_URL = Constant.BASE_URL;
    private static Retrofit retrofit = null;
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    private ApiClient() {
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }


    final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public void getAudio(Callback<List<AudioModel>> callback) {
        Call<List<AudioModel>> call = apiService.getAudio();
        call.enqueue(callback);
    }

}