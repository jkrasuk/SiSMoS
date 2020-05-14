package com.jk.sismos.main.data;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.sismos.R;
import com.jk.sismos.main.data.model.Earthquake;
import com.jk.sismos.main.data.model.Feed;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_history);
        earthquakeList = findViewById(R.id.earthquakeList);

        mAPIService = ApiUtils.getAPIService();
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
                        item.setLongitude(split[3]);
                        item.setLongitude(split[4]);
                        item.setMagnitude(split[5]);
                        item.setDepth(split[6]);
                        item.setProvince(split[7]);

                        String[] splitReference = item.getDescription().split(" La magnitud");
                        item.setPlaceReference(splitReference[0]);
                        Log.i("XML RESULTADO", item.toString());
                    }
                    earthquakeListAdapter = new EarthquakeListAdapter(getApplicationContext(), feed.getEarthquakeList());
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
