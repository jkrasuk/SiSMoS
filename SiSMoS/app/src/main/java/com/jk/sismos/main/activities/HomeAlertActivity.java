package com.jk.sismos.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.sismos.R;
import com.jk.sismos.main.utils.AlarmManager;

public class HomeAlertActivity extends AppCompatActivity {
    private static final String TAG = "HomeAlertActivity";
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alert_content);

        alarmManager = AlarmManager.getInstance();

        Button stopAlarmBtn = findViewById(R.id.btn_stop_alarm);

        stopAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.stopSound();
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}

