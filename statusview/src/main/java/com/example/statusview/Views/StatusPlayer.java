package com.example.statusview.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.statusview.CustomViews.StatusPlayerProgressView;
import com.example.statusview.Helpers.StatusPreference;
import com.example.statusview.Modal.StatusModel;
import com.example.statusview.R;

import java.util.ArrayList;

public class StatusPlayer extends AppCompatActivity implements StatusPlayerProgressView.StatusPlayerListener {
    public static final String STATUS_IMAGE_KEY = "statusImages";
    StatusPlayerProgressView statusPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    ArrayList<StatusModel> stories;
    StatusPreference statusPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_player);
        statusPlayerProgressView = findViewById(R.id.progressBarView);
        name = findViewById(R.id.statusUserName);
        time = findViewById(R.id.statusTime);
        statusPlayerProgressView.setSingleStatusDisplayTime(2000);
        imageView = findViewById(R.id.statusImage);
        statusPreference = new StatusPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            stories = intent.getParcelableArrayListExtra(STATUS_IMAGE_KEY);
            initStatusProgressView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusPlayerProgressView.cancelAnimation();
    }

    private void initStatusProgressView() {
        if (stories != null && stories.size() > 0) {
            statusPlayerProgressView.setStatusPlayerListener(this);
            statusPlayerProgressView.setProgressBarsCount(stories.size());
            setTouchListener();
        }
    }

    private void setTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //pause
                    statusPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //resume
                    statusPlayerProgressView.resumeProgress();
                    return true;
                }else {
                    return false;
                }
            }
        });
    }


    @Override
    public void onStartedPlaying(int index) {
        loadImage(index);
        name.setText(stories.get(index).name);
        time.setText(stories.get(index).time);
        statusPreference.setStatusVisited(stories.get(index).imageUri);
    }

    @Override
    public void onFinishedPlaying() {
        finish();
    }

    private void loadImage(int index) {
        Glide.with(this)
                .load(stories.get(index).imageUri)
                .transition(DrawableTransitionOptions.withCrossFade(800))
                .into(imageView);
    }
}
