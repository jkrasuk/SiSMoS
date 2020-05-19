package com.jk.sismos.main.sensors;

public class SignPool {
    private EarthquakeSign head;

    EarthquakeSign acquire() {
        EarthquakeSign acquired = head;
        if (acquired == null) {
            acquired = new EarthquakeSign();
        } else {
            head = acquired.nextSign;
        }
        return acquired;
    }

    void release(EarthquakeSign sample) {
        sample.nextSign = head;
        head = sample;
    }
}