package com.jk.sismos.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jk.sismos.main.data.remote.Request;

import static android.content.Context.MODE_PRIVATE;

public class EventManager {
    private static SharedPreferences prefs;
    private static Request request;
    private Context context;

    public EventManager(Context context) {
        this.context = context;
        prefs = this.context.getSharedPreferences("preferences", MODE_PRIVATE);
        request = new Request();
    }

    public static void registerEvent(Integer event) {
        String typeEvents = "";
        String description = "";
        String state = "";
        String token = prefs.getString("token", "");

        switch (event) {
            case Constants.LOGIN_CORRECT:
                typeEvents = "Login";
                description = "Usuario logueado correctamente";
                state = "ACTIVO";
                break;
        }

        if (!typeEvents.equals("") && !description.equals("") && !state.equals("") && !token.equals("")) {
            request.registerEvent(token, typeEvents, state, description);
        }
    }
}
