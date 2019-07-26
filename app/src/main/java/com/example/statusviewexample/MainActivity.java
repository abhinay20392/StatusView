/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 8:14 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 7:24 AM
 *
 */

package com.example.statusviewexample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.statusview.CustomViews.StatusView;
import com.example.statusview.Modal.StatusModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusView statusView = findViewById(R.id.statusView);
        statusView.resetStatusVisits();
        ArrayList<StatusModel> uris = new ArrayList<>();
        uris.add(new StatusModel(R.drawable.ic_android_black_24dp, "Agent Black", "12:00 PM", 1));
        uris.add(new StatusModel(R.drawable.ic_android_blue_24dp, "Agent Blue", "01:00 PM", 1));
        uris.add(new StatusModel(R.drawable.ic_android_green_24dp, "Agent Green", "02:00 PM", 1));
        uris.add(new StatusModel(R.drawable.ic_android_red_24dp, "Agent Red", "03:00 PM", 1));
        uris.add(new StatusModel(R.drawable.ic_android_yellow_24dp, "Agent Yellow", "04:00 PM", 1));
        statusView.setImageUris(uris);
        //statusView.setImage(getResources().getDrawable(R.drawable.sbpd));
    }
}
