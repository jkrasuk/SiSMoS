package com.jk.sismos.main.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jk.sismos.R;
import com.jk.sismos.main.data.remote.APIService;
import com.jk.sismos.main.data.remote.ApiUtils;
import com.jk.sismos.main.data.remote.Request;
import com.jk.sismos.main.data.remote.RequestCallbacks;


public class LoginActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = "LoginActivity";
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.input_email);
        final EditText password = findViewById(R.id.input_password);
        Button submitBtn = findViewById(R.id.btn_login);
        TextView register = findViewById(R.id.register);
        mAPIService = ApiUtils.getAPIService();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trimmedEmail = email.getText().toString().trim();
                String trimmedPassword = password.getText().toString().trim();

                if (!TextUtils.isEmpty(trimmedEmail) && !TextUtils.isEmpty(trimmedPassword)) {
                    sendLogin(trimmedEmail, trimmedPassword);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // TODO: La última vez que ejecute me tiraba "Error de autenticación", capaz que faltan campos
    public void sendLogin(String email, String password) {
        Request request = new Request();
        request.sendLogin(email, password, new RequestCallbacks() {
            @Override
            public void onSuccess(@NonNull String value) {
                if(value.contains("state='success'")){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
            }
        });
    }
}
