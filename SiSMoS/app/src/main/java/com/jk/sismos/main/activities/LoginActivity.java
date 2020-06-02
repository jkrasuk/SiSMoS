package com.jk.sismos.main.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jk.sismos.R;
import com.jk.sismos.main.data.model.user.UserPost;
import com.jk.sismos.main.data.remote.APIService;
import com.jk.sismos.main.data.remote.ApiUtils;
import com.jk.sismos.main.data.remote.Request;
import com.jk.sismos.main.data.remote.RequestCallbacks;
import com.jk.sismos.main.utils.Constants;
import com.jk.sismos.main.utils.EventManager;

import java.io.IOException;

import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = "LoginActivity";
    SharedPreferences preferences;
    private APIService mAPIService;
    private EventManager eventManager;

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    protected void onResume() {
        super.onResume();
        final EditText email = findViewById(R.id.input_email);
        final EditText password = findViewById(R.id.input_password);
        final CheckBox rememberPassword = findViewById(R.id.rememberPassword);

        if (preferences.contains("user")) {
            email.setText(preferences.getString("user", null));
        } else {
            email.setText("");
        }

        if (preferences.contains("password")) {
            password.setText(preferences.getString("password", null));
        } else {
            password.setText("");
        }

        if (preferences.contains("rememberPassword")) {
            rememberPassword.setChecked(preferences.getString("rememberPassword", null).equals("true"));
        } else {
            rememberPassword.setChecked(false);
        }
        password.clearFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eventManager = new EventManager(this);
        final EditText email = findViewById(R.id.input_email);
        final EditText password = findViewById(R.id.input_password);
        final CheckBox rememberPassword = findViewById(R.id.rememberPassword);

        Button submitBtn = findViewById(R.id.btn_login);
        TextView register = findViewById(R.id.register);
        mAPIService = ApiUtils.getAPIService();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        if (preferences.contains("user")) {
            email.setText(preferences.getString("user", null));
        } else {
            email.setText("");
        }

        if (preferences.contains("password")) {
            password.setText(preferences.getString("password", null));
        } else {
            password.setText("");
        }

        if (preferences.contains("rememberPassword")) {
            rememberPassword.setChecked(preferences.getString("rememberPassword", null).equals("true"));
        } else {
            rememberPassword.setChecked(false);
        }

        if (!isOnline(getApplicationContext())) {
            Toast.makeText(this, "No hay internet", Toast.LENGTH_SHORT).show();
        }

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
                Boolean savePassword = rememberPassword.isChecked();
                if (!TextUtils.isEmpty(trimmedEmail) && !TextUtils.isEmpty(trimmedPassword)) {
                    sendLogin(trimmedEmail, trimmedPassword, savePassword);
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

    public void sendLogin(final String email, final String password, final Boolean savePassword) {
        Request request = new Request();
        request.sendLogin(email, password, new RequestCallbacks() {
            @Override
            public void onSuccess(@NonNull UserPost value) {
                if (value != null) {
                    if (value.getState().equals("success")) {
//                        Log.d(TAG, value.getToken());
                        preferences.edit().putString("token", value.getToken()).commit();

                        // Recordar usuario y contraseña
                        if (savePassword) {
                            preferences.edit().putString("user", email).commit();
                            preferences.edit().putString("password", password).commit();
                            preferences.edit().putString("rememberPassword", "true").commit();
                        } else {
                            preferences.edit().putString("user", email).commit();
                            preferences.edit().remove("password").commit();
                            preferences.edit().remove("rememberPassword").commit();
                        }

                        EventManager.registerEvent(Constants.LOGIN_CORRECT);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
//                        Log.d(TAG, value.toString());
                        Toast.makeText(getApplicationContext(), value.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorBody(@NonNull ResponseBody errorBody) {
                if (errorBody != null) {
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(errorBody.string());
                        Gson gson = new Gson();
                        UserPost errorResponse = gson.fromJson(mJson, UserPost.class);

//                        Log.d(TAG, errorResponse.getMsg());
                        Toast.makeText(getApplicationContext(), errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
