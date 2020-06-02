package com.jk.sismos.main.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jk.sismos.main.sensors.accelerometer.ShakeDetector;
import com.jk.sismos.main.sensors.gyroscope.RotationDetector;
import com.jk.sismos.main.sensors.light.LightDetector;
import com.jk.sismos.main.utils.AlarmManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ShakeService extends Service implements ShakeDetector.Listener, LightDetector.Listener, RotationDetector.Listener {
    private final static String TAG = "SERVICE";
    private boolean noHayRotacion;
    private boolean luzApagada;
    private boolean hayMovimiento;
    private ShakeDetector sd;
    private LightDetector ld;
    private RotationDetector rd;
    private SharedPreferences prefs;
    private AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        alarmManager = AlarmManager.getInstance();
        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        sd = new ShakeDetector(this);
        sd.start(sensorManager);

        ld = new LightDetector(this);
        ld.start(sensorManager);

        rd = new RotationDetector(this);
        rd.start(sensorManager);
    }

    @Override
    public void onDestroy() {
        sd.stop();
        ld.stop();
        rd.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void hearShake() {
        this.hayMovimiento = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hayMovimiento = false;
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void senseNoLight() {
        this.luzApagada = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                luzApagada = false;
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void noRotation() {
        this.noHayRotacion = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                noHayRotacion = false;
            }
        }, 2000);
        verificacionSismo();
    }


    public void verificacionSismo() {
//        Log.d(TAG, "noHayRotacion " + noHayRotacion + " - luzApagada " + luzApagada + " - hayMovimiento " + hayMovimiento);

        if (noHayRotacion && luzApagada && hayMovimiento) {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            //Con esto traigo a primer plano la app
            Intent intent = packageManager.getLaunchIntentForPackage("com.jk.sismos");
            if (intent != null) {
                intent.setPackage(null);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                getApplicationContext().startActivity(intent);

                //Aviso que hay que mostrar la pantalla de alerta
                Intent retIntent = new Intent("earthquake");
                sendBroadcast(retIntent);
            }


            String data = System.currentTimeMillis() + "-Movimiento detectado";
            Gson gson = new Gson();
            ArrayList<String> textList = null;

            if (prefs.contains("history")) {
                String jsonText = prefs.getString("history", null);
//                Log.d(TAG, "HISTORIAL: " + jsonText);
                textList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonText, String[].class)));
            } else {
                textList = new ArrayList<String>();
            }

            textList.add(data);
            String jsonText = gson.toJson(textList);
            //TODO Deberia agrupar los distintos sismos, utilizando el timestamp
            Log.d(TAG, jsonText);

            alarmManager.startSound(this, "alerta.mp3", true, false);
            prefs.edit().putString("history", jsonText).apply();
        }
    }


}
