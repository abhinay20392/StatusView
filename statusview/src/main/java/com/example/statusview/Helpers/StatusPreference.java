/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 8:14 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 6:41 AM
 *
 */

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

    public void setStatusVisited(int uri) {
        editor.putBoolean(String.valueOf(uri), true);
        editor.apply();
    }

    public boolean isStatusVisited(int uri) {
        return preferences.getBoolean(String.valueOf(uri), false);
    }
}
