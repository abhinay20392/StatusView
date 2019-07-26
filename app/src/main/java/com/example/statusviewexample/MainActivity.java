/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 7:55 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 5:19 PM
 *
 */

package com.example.statusviewexample;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.statusview.CustomViews.StatusView;
import com.example.statusview.Modal.StatusModel;
import com.example.statusview.Utils.MeasureUtils;
import com.example.statusview.Views.StatusPlayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        StatusView statusView = findViewById(R.id.statusView);
        statusView.resetStatusVisits();
        ArrayList<StatusModel> statusList = new ArrayList<>();
        statusList.add(new StatusModel(R.drawable.bruce, "Hulk", "12:00 PM", StatusPlayer.TYPE_IMAGE, 0, 2000));
        statusList.add(new StatusModel(R.drawable.haweye, "HawkEye", "01:00 PM", StatusPlayer.TYPE_IMAGE, 0, 2000));
        statusList.add(new StatusModel(R.drawable.ironman, "Iron man", "02:00 PM", StatusPlayer.TYPE_IMAGE, 0, 2000));
        statusList.add(new StatusModel(R.drawable.marvel, "Captain Marvel", "03:00 PM", StatusPlayer.TYPE_IMAGE, 0, 2000));
        statusList.add(new StatusModel(R.drawable.thor, "Thor", "04:00 PM", StatusPlayer.TYPE_IMAGE, 0, 2000));
        statusList.add(new StatusModel(0, "Random Video1", "05:00 PM", StatusPlayer.TYPE_VIDEO, R.raw.vid1, MeasureUtils.getDuration(context, R.raw.vid1)));
        statusList.add(new StatusModel(0, "Random Video2", "06:00 PM", StatusPlayer.TYPE_VIDEO, R.raw.vid2, MeasureUtils.getDuration(context, R.raw.vid1)));
        statusView.setImageUris(statusList);
        statusView.setImage(getResources().getDrawable(R.drawable.bruce));
    }
}
 