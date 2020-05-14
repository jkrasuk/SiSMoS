package com.jk.sismos.main.data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.sismos.R;
import com.jk.sismos.main.data.model.UserPost;
import com.jk.sismos.main.data.remote.APIService;
import com.jk.sismos.main.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private APIService mAPIService;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.input_email);
        final EditText password = (EditText) findViewById(R.id.input_password);
        Button submitBtn = (Button) findViewById(R.id.btn_login);

        mAPIService = ApiUtils.getAPIService();

//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String trimmedEmail = email.getText().toString().trim();
//                String trimmedPassword = password.getText().toString().trim();
//
//                if (!TextUtils.isEmpty(trimmedEmail) && !TextUtils.isEmpty(trimmedPassword)) {
//                    sendLogin(trimmedEmail, trimmedPassword);
//                }
//            }
//        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, OfficialHistoryActivity.class);
                startActivity(intent);
            }
        });
    }


    public void sendLogin(String email, String password) {
        mAPIService.login(email, password).enqueue(new Callback<UserPost>() {
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
