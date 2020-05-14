package com.jk.sismos.main.data.remote;
import com.jk.sismos.main.data.model.UserPost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("api/api/login")
    @FormUrlEncoded
    Call<UserPost> login(@Field("email") String email,
                         @Field("password") String password);
}