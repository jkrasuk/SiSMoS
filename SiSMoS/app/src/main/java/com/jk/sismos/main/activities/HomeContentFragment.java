package com.jk.sismos.main.activities;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jk.sismos.R;


public class HomeContentFragment extends Fragment {

    private static final String TAG = "HomeContent";

    private static final String TEXT = "text";
    private TextView helpText;

    public static HomeContentFragment newInstance(String text) {
        HomeContentFragment frag = new HomeContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Pongo por default ese contenido
        View layout = inflater.inflate(R.layout.activity_home_content, container, false);

        TextClock textClock = layout.findViewById(R.id.hk_time);
        textClock.setFormat24Hour("kk:mm:ss");
        this.helpText = layout.findViewById(R.id.helpText); //txt is object of TextView
        helpText.setPaintFlags(helpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        helpText.setMovementMethod(LinkMovementMethod.getInstance());
        helpText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.argentina.gob.ar/salud/desastres/cuidados-terremotos"));
                startActivity(browserIntent);
            }
        });
        return layout;
    }

    public void setHelpText(String msg) {
        if (helpText != null) {
            helpText.setText(msg);
        }
    }
}

