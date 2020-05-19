package com.jk.sismos.main.data.remote;

import android.util.Log;
import android.widget.Toast;

import com.jk.sismos.main.data.model.UserPost;

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
    public void sendLogin(String email, String password){
        mAPIService.login(ENV, email, password, COMMISION, GROUP).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "Request enviado." + response.body().toString());
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
    public void sendRegister(String name, String surname, String dni, String email, String password) {
        mAPIService.register(ENV, name, surname, Integer.valueOf(dni), email, password, COMMISION, GROUP).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "Request enviado." + response.body().toString());
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

    public void showResponse(String response) {
        Log.d(TAG, response);
    }

}
