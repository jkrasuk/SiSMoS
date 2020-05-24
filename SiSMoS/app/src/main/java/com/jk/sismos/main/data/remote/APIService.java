package com.jk.sismos.main.data.remote;

import com.jk.sismos.main.data.model.event.EventPost;
import com.jk.sismos.main.data.model.user.UserPost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {
    @POST("api/api/login")
    @FormUrlEncoded
    Call<UserPost> login(
            @Field("env") String env,
            @Field("email") String email,
            @Field("password") String password,
            @Field("commission") Integer commission,
            @Field("group") Integer group
    );

    @POST("api/api/register")
    @FormUrlEncoded
    Call<UserPost> register(@Field("env") String env,
                            @Field("name") String name,
                            @Field("lastname") String surname,
                            @Field("dni") Integer dni,
                            @Field("email") String email,
                            @Field("password") String password,
                            @Field("commission") Integer commission,
                            @Field("group") Integer group);

    @POST("api/api/event")
    @FormUrlEncoded
    Call<EventPost> registerEvent(@Header("token") String token,
                             @Field("env") String env,
                             @Field("type_events") String typeEvents,
                             @Field("state") String state,
                             @Field("description") String description);
}