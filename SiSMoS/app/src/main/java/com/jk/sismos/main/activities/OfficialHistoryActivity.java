package com.jk.sismos.main.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jk.sismos.R;
import com.jk.sismos.main.data.adapter.EarthquakeListAdapter;
import com.jk.sismos.main.data.model.inpresList.Earthquake;
import com.jk.sismos.main.data.model.inpresList.Feed;
import com.jk.sismos.main.data.remoteXML.APIService;
import com.jk.sismos.main.data.remoteXML.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfficialHistoryActivity extends AppCompatActivity {
    private ListView earthquakeList;
    private APIService mAPIService;
    private String TAG = "OfficialHistoryActivity";
    private EarthquakeListAdapter earthquakeListAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_history);

        earthquakeList = findViewById(R.id.earthquakeList);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAPIService = ApiUtils.getAPIService();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(OfficialHistoryActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "aca");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("Latitud", String.valueOf(location.getLatitude()));
                                lastKnownLocation = location;
                            }
                        }
                    });
        }


        getDataFromINPRES();
    }

    private void getDataFromINPRES() {
        Log.d(TAG, "dentro");
        mAPIService.getEarthquakeData().enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if (response.isSuccessful()) {
                    Feed feed = response.body();

                    for (Earthquake item : feed.getEarthquakeList()) {
                        String[] split = item.getTitle().split(" -- ");
                        item.setDate(split[1]);
                        item.setTime(split[2]);
                        item.setLatitude(split[3]);
                        item.setLongitude(split[4]);
                        item.setMagnitude(split[5]);
                        item.setDepth(split[6]);
                        item.setProvince(split[7]);

                        String[] splitReference = item.getDescription().split(" La magnitud");
                        item.setPlaceReference(splitReference[0]);
                        Log.i("XML RESULTADO", item.toString());
                    }

                    earthquakeListAdapter = new EarthquakeListAdapter(getApplicationContext(), feed.getEarthquakeList(), lastKnownLocation);
                    earthquakeList.setAdapter(earthquakeListAdapter);
                } else {
                    Log.i("XML ERROR", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "Error al enviar el request.");

            }
        });
    }
}
