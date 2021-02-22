package com.example.bimapp.network;

import com.example.bimapp.model.AudioModel;
import com.example.bimapp.model.BaseResponseModel;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiInterface {


    @GET("api/categories/get_catV2.php")
    Call<List<AudioModel>> getAudio();

//    @FormUrlEncoded
//    @POST("api/user/login.php")
//    Call<BaseResponseModel> Login(@FieldMap Map<String, String> params);


}