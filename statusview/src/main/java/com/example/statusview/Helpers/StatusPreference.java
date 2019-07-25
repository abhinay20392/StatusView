package com.example.statusview.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class StatusPreference {
    private static final String PREF_NAME = "statusview_cache_pref";
    private static final int PREF_MODE_PRIVATE = 1;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public StatusPreference(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }

    public void clearStatusPreferences() {
        editor.clear();
        editor.apply();
    }

    public void setStatusVisited(String uri) {
        editor.putBoolean(uri, true);
        editor.apply();
    }

    public boolean isStatusVisited(String uri) {
        return preferences.getBoolean(uri, false);
    }
}
