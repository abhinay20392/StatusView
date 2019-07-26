/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 7:55 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 5:29 PM
 *
 */

package com.example.statusview.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.statusview.CustomViews.StatusPlayerProgressView;
import com.example.statusview.Helpers.StatusPreference;
import com.example.statusview.Modal.StatusModel;
import com.example.statusview.R;
import com.example.statusview.Utils.MeasureUtils;

import java.util.ArrayList;

public class StatusPlayer extends AppCompatActivity implements StatusPlayerProgressView.StatusPlayerListener {
    public static final String STATUS_IMAGE_KEY = "statusImages";

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    public static int CLICK_ACTION_THRESHOLD_L = 50;
    public static int CLICK_ACTION_THRESHOLD_H = 200;
    StatusPlayerProgressView statusPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    VideoView videoView;
    ArrayList<StatusModel> statusList;
    StatusPreference statusPreference;
    private long startTime;
    private long endTime;
    private Context context;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_player);
        context = this;
        statusPlayerProgressView = findViewById(R.id.progressBarView);
        name = findViewById(R.id.statusUserName);
        time = findViewById(R.id.statusTime);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.statusImage);
        statusPreference = new StatusPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            statusList = intent.getParcelableArrayListExtra(STATUS_IMAGE_KEY);
            statusPlayerProgressView.setList(statusList);
            statusPlayerProgressView.setSingleStatusDisplayTime(2000);
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

                // Toast.makeText(StatusPlayer.this, "" + x + "/" + MeasureUtils.getScreenWidth(context), Toast.LENGTH_SHORT).show();


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startTime = System.currentTimeMillis();
                    //pause
                    statusPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    endTime = System.currentTimeMillis();
                    if (isAClick(startTime, endTime)) {
                        if (isClickedOnLeft(x)) {
                            gotoPrevious();
                        } else {
                            gotoNext();
                        }
                    }
                    //resume
                    statusPlayerProgressView.resumeProgress();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private boolean isAClick(long startTime, long endTime) {
        float difference = endTime - startTime;
        //String k = "Start: " + startTime + " End: " + endTime + " Difference: " + difference;
        //Log.d("***", "difference " + k);
        return (difference > CLICK_ACTION_THRESHOLD_L && difference < CLICK_ACTION_THRESHOLD_H);
    }

    private boolean isClickedOnLeft(int x) {
        int half = MeasureUtils.getScreenWidth(context) / 2;
        if (x < half) {
            return true;
        } else {
            return false;
        }
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
        currentIndex = index;
        if (videoView != null)
            videoView.stopPlayback();

        StatusModel statusModel = statusList.get(index);
        if (statusModel.getType() == TYPE_IMAGE) {
            imageView.setImageDrawable(getResources().getDrawable(statusModel.getImageUri()));
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);

        } else if (statusModel.getType() == TYPE_VIDEO) {
            String path = "android.resource://" + getPackageName() + "/" + statusModel.getVideoName();
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }

    }

    private void gotoPrevious() {
        //statusPlayerProgressView.endCurrent();
        if (currentIndex != 0)
            statusPlayerProgressView.startAnimating(currentIndex - 1);
        else {
            statusPlayerProgressView.startAnimating(0);
        }
        // statusPlayerProgressView.startCurrent();
    }

    private void gotoNext() {
        /*if (currentIndex != statusList.size() - 1)
            statusPlayerProgressView.startAnimating(currentIndex + 1);
        else
            finish();*/
        statusPlayerProgressView.finishAnimation();
    }
}
