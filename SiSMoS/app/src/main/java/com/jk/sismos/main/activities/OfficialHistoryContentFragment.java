package com.jk.sismos.main.activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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


public class OfficialHistoryContentFragment extends Fragment {

    private ListView earthquakeList;
    private APIService mAPIService;
    private String TAG = "OfficialHistoryActivity";
    private EarthquakeListAdapter earthquakeListAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;

    private static final String TEXT = "text";

    public static OfficialHistoryContentFragment newInstance(String text) {
        OfficialHistoryContentFragment frag = new OfficialHistoryContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Pongo por default ese contenido
        View layout = inflater.inflate(R.layout.activity_official_history, container, false);

        earthquakeList = layout.findViewById(R.id.earthquakeList);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mAPIService = ApiUtils.getAPIService();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
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
        return layout;
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

                    earthquakeListAdapter = new EarthquakeListAdapter(getActivity(), feed.getEarthquakeList(), lastKnownLocation);
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

