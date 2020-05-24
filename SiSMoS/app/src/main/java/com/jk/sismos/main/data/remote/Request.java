package com.jk.sismos.main.data.remote;

import android.util.Log;

import com.jk.sismos.main.data.model.event.EventPost;
import com.jk.sismos.main.data.model.user.UserPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Request {
    private final static Integer GROUP = 614;
    private final static Integer COMMISION = 2900;
    private final static String ENV = "TEST";
    private final static String TAG = "REQUEST";
    private APIService mAPIService;

    public Request() {
        mAPIService = ApiUtils.getAPIService();
    }

    public void sendLogin(String email, String password, final RequestCallbacks requestCallbacks) {
        mAPIService.login(ENV, email, password, COMMISION, GROUP).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (requestCallbacks != null) {
                    if (response.isSuccessful()) {
                        requestCallbacks.onSuccess(response.body());
                    } else {
                        requestCallbacks.onErrorBody(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                if (requestCallbacks != null) {
                    requestCallbacks.onError(t);
                }
                Log.e(TAG, "Error al enviar el request.");
            }

        });
    }

    public void sendRegister(String name, String surname, String dni, String email, String
            password) {
        mAPIService.register(ENV, name, surname, Integer.valueOf(dni), email, password, COMMISION, GROUP).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                } else {

                    Log.i(TAG, "Ocurrió un error.");
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Error al enviar el request.");
            }
        });
    }

    public void registerEvent(String token, String typeEvents, String state, String description) {
        mAPIService.registerEvent(token, ENV, typeEvents, state, description).enqueue(new Callback<EventPost>() {
            @Override
            public void onResponse(Call<EventPost> call, Response<EventPost> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                } else {

                    Log.i(TAG, "Ocurrió un error.");
                }
            }

            @Override
            public void onFailure(Call<EventPost> call, Throwable t) {
                Log.e(TAG, "Error al enviar el request.");
            }
        });
    }

    public void showResponse(String response) {
        Log.d(TAG, response);
    }

}
