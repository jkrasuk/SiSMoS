package com.jk.sismos.main.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.jk.sismos.R;
import com.jk.sismos.main.data.adapter.FeltEarthquakeListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;


public class FeltHistoryContentFragment extends Fragment {

    private static final String TAG = "FeltHistory";
    private static final String TEXT = "text";
    private ListView feltEarthquakeList;
    private FeltEarthquakeListAdapter feltEarthquakeListAdapter;
    private SharedPreferences prefs;

    public static FeltHistoryContentFragment newInstance(String text) {
        FeltHistoryContentFragment frag = new FeltHistoryContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_felt_history, container, false);
        prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);

        ArrayList<String> feltEarthquakes = null;
        Gson gson = new Gson();

        if (prefs.contains("history")) {
            String jsonText = prefs.getString("history", null);
            feltEarthquakes = new ArrayList<>(Arrays.asList(gson.fromJson(jsonText, String[].class)));
        } else {
            feltEarthquakes = new ArrayList<String>();
        }
        //Sismos más recientes primero
        Collections.reverse(feltEarthquakes);
        feltEarthquakeList = layout.findViewById(R.id.feltEarthquakeList);
        feltEarthquakeListAdapter = new FeltEarthquakeListAdapter(getActivity(), feltEarthquakes);
        feltEarthquakeList.setAdapter(feltEarthquakeListAdapter);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

