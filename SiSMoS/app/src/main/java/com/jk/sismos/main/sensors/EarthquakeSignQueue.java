package com.jk.sismos.main.sensors;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeSignQueue {

    private static final long MAX_WINDOW_SIZE = 250000000;
    private static final long MIN_WINDOW_SIZE = MAX_WINDOW_SIZE >> 1;

    private final SignPool pool = new SignPool();

    private EarthquakeSign oldest;
    private EarthquakeSign newest;
    private int sampleCount;
    private int acceleratingCount;

    void add(long timestamp, boolean accelerating) {
        purge(timestamp - MAX_WINDOW_SIZE);

        EarthquakeSign added = pool.acquire();
        added.timestamp = timestamp;
        added.isAccelerating = accelerating;
        added.nextSign = null;
        if (newest != null) {
            newest.nextSign = added;
        }
        newest = added;
        if (oldest == null) {
            oldest = added;
        }

        sampleCount++;
        if (accelerating) {
            acceleratingCount++;
        }
    }

    void clear() {
        while (oldest != null) {
            EarthquakeSign removed = oldest;
            oldest = removed.nextSign;
            pool.release(removed);
        }
        newest = null;
        sampleCount = 0;
        acceleratingCount = 0;
    }

    void purge(long cutoff) {
        while (sampleCount >= 4
                && oldest != null && cutoff - oldest.timestamp > 0) {

            EarthquakeSign removed = oldest;
            if (removed.isAccelerating) {
                acceleratingCount--;
            }
            sampleCount--;

            oldest = removed.nextSign;
            if (oldest == null) {
                newest = null;
            }
            pool.release(removed);
        }
    }

    List<EarthquakeSign> asList() {
        List<EarthquakeSign> list = new ArrayList<EarthquakeSign>();
        EarthquakeSign s = oldest;
        while (s != null) {
            list.add(s);
            s = s.nextSign;
        }
        return list;
    }

    boolean isShaking() {
        return newest != null
                && oldest != null
                && newest.timestamp - oldest.timestamp >= MIN_WINDOW_SIZE
                && acceleratingCount >= (sampleCount >> 1) + (sampleCount >> 2);
    }
}
