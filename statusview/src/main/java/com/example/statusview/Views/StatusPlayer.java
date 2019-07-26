/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 8:14 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 6:44 AM
 *
 */

package com.example.statusview.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.statusview.CustomViews.StatusPlayerProgressView;
import com.example.statusview.CustomViews.StatusView;
import com.example.statusview.Helpers.StatusPreference;
import com.example.statusview.Modal.StatusModel;
import com.example.statusview.R;

import java.util.ArrayList;

public class StatusPlayer extends AppCompatActivity implements StatusPlayerProgressView.StatusPlayerListener {
    public static final String STATUS_IMAGE_KEY = "statusImages";

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    StatusPlayerProgressView statusPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    VideoView videoView;
    ArrayList<StatusModel> statusList;
    StatusPreference statusPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_player);
        statusPlayerProgressView = findViewById(R.id.progressBarView);
        name = findViewById(R.id.statusUserName);
        time = findViewById(R.id.statusTime);
        videoView = findViewById(R.id.videoView);
        statusPlayerProgressView.setSingleStatusDisplayTime(2000);
        imageView = findViewById(R.id.statusImage);
        statusPreference = new StatusPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            statusList = intent.getParcelableArrayListExtra(STATUS_IMAGE_KEY);
            initStatusProgressView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusPlayerProgressView.cancelAnimation();
    }

    private void initStatusProgressView() {
        if (statusList != null && statusList.size() > 0) {
            statusPlayerProgressView.setStatusPlayerListener(this);
            statusPlayerProgressView.setProgressBarsCount(statusList.size());
            setTouchListener();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();

                Toast.makeText(StatusPlayer.this, "" + x, Toast.LENGTH_SHORT).show();


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //pause
                    statusPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //resume
                    statusPlayerProgressView.resumeProgress();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    @Override
    public void onStartedPlaying(int index) {
        loadItem(index);
        name.setText(statusList.get(index).getName());
        time.setText(statusList.get(index).getTime());
        statusPreference.setStatusVisited(statusList.get(index).getImageUri());
    }

    @Override
    public void onFinishedPlaying() {
        finish();
    }

    private void loadItem(int index) {
        StatusModel statusModel = statusList.get(index);
        if (statusModel.getType() == TYPE_IMAGE) {
            imageView.setImageDrawable(getResources().getDrawable(statusModel.getImageUri()));
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);

        } else if (statusModel.getType() == TYPE_VIDEO) {
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }

       /* Glide.with(this)
                .load(stories.get(index).imageUri)
                .transition(DrawableTransitionOptions.withCrossFade(800))
                .into(imageView);*/
    }
}
