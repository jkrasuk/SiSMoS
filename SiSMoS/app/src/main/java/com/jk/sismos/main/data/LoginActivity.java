package com.jk.sismos.main.data;

import android.os.Bundle;
import android.text.TextUtils;
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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = email.getText().toString().trim();
                String body = password.getText().toString().trim();
                Log.d(TAG, title);
                Log.d(TAG, body);
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                    sendLogin(title, body);
                }
            }
        });
    }

    public void sendLogin(String email, String password) {
        mAPIService.login(email, password).enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else{

                    Log.i(TAG, "Ocurrió un error.");
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");

            }
        });
    }

    public void showResponse(String response) {
        Log.d(TAG, response);
    }
}
