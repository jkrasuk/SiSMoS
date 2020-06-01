package com.jk.sismos.main.activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jk.sismos.R;
import com.jk.sismos.main.utils.AlarmManager;


public class HomeAlertContentFragment extends Fragment {

    private static final String TAG = "HomeAlertContent";

    private static final String TEXT = "text";
    private TextView helpText;

    public static HomeAlertContentFragment newInstance(String text) {
        HomeAlertContentFragment frag = new HomeAlertContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Pongo por default ese contenido
        View layout = inflater.inflate(R.layout.activity_home_alert_content, container, false);
        Button stopAlarmBtn = layout.findViewById(R.id.btn_stop_alarm);
        stopAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager.getInstance().stopSound();
                //Cargo el home de nuevo
                Fragment fragment = HomeContentFragment.newInstance(getString(R.string.menu_home));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();
            }
        });

        return layout;
    }

}

