package com.jk.sismos.main.sensors.light;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightDetector implements SensorEventListener {
    private final Listener listener;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    public LightDetector(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void senseNoLight();
    }

    public boolean start(SensorManager sensorManager) {
        if (lightSensor != null) {
            return true;
        }

        lightSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            this.sensorManager = sensorManager;
            sensorManager.registerListener(this, lightSensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        return lightSensor != null;
    }


    public void stop() {
        if (lightSensor != null) {
            sensorManager.unregisterListener(this, lightSensor);
            sensorManager = null;
            lightSensor = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean isLightOff = isLightOff(event);
        if (isLightOff) {
            listener.senseNoLight();
        }
    }

    private boolean isLightOff(SensorEvent event) {
        float currentLight = event.values[0];

        if(currentLight < 1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
