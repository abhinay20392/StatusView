/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 7:55 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 5:29 PM
 *
 */

package com.example.statusview.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.statusview.Modal.StatusModel;
import com.example.statusview.R;

import java.util.ArrayList;

public class StatusPlayerProgressView extends View {
    public static final int PROGRESS_BAR_HEIGHT = 2;
    public static final int GAP_BETWEEN_PROGRESS_BARS = 2;
    public static final int SINGLE_STATUS_DISPLAY_TIME = 1000;
    public static final String PROGRESS_PRIMARY_COLOR = "#009988";
    public static final String PROGRESS_SECONDARY_COLOR = "#EEEEEE";
    ArrayList<StatusModel> statusList = new ArrayList<>();
    private int mScreenWidth;
    private int mProgressHeight;
    private int mGapBetweenProgressBars;
    private Resources resources;
    private Paint mProgressPaint;
    private int singleProgressBarWidth;
    private int progressBarsCount;
    private int[] progressBarRightEdge;
    private RectF progressBarRectF;
    private int top;
    private int bottom;
    private int progressBarPrimaryColor;
    private int progressBarSecondaryColor;
    private int currentProgressIndex;
    private long singleStatusDisplayTime;
    private ValueAnimator progressAnimator;
    private boolean isCancelled;
    private StatusPlayerListener statusPlayerListener;

    public StatusPlayerProgressView(Context context) {
        super(context);
        setDefaults();
        init(context);
        prepareValues();
    }

    public StatusPlayerProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatusPlayerProgressView, 0, 0);
        try {
            mProgressHeight = getPxFromDp((int) ta.getDimension(R.styleable.StatusPlayerProgressView_progressBarHeight, PROGRESS_BAR_HEIGHT));
            mGapBetweenProgressBars = getPxFromDp((int) ta.getDimension(R.styleable.StatusPlayerProgressView_gapBetweenProgressBar, GAP_BETWEEN_PROGRESS_BARS));
            progressBarPrimaryColor = ta.getColor(R.styleable.StatusPlayerProgressView_progressBarPrimaryColor, Color.parseColor(PROGRESS_PRIMARY_COLOR));
            progressBarSecondaryColor = ta.getColor(R.styleable.StatusPlayerProgressView_progressBarSecondaryColor, Color.parseColor(PROGRESS_SECONDARY_COLOR));
            singleStatusDisplayTime = ta.getInt(R.styleable.StatusPlayerProgressView_singleStatusDisplayTime, SINGLE_STATUS_DISPLAY_TIME);
        } finally {
            ta.recycle();
        }
        prepareValues();
    }

    private void init(Context context) {
        resources = context.getResources();
        mScreenWidth = resources.getDisplayMetrics().widthPixels;
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
    }

    private void setDefaults() {
        mProgressHeight = getPxFromDp(PROGRESS_BAR_HEIGHT);
        mGapBetweenProgressBars = getPxFromDp(GAP_BETWEEN_PROGRESS_BARS);
        progressBarPrimaryColor = Color.parseColor(PROGRESS_PRIMARY_COLOR);
        progressBarSecondaryColor = Color.parseColor(PROGRESS_SECONDARY_COLOR);
        singleStatusDisplayTime = SINGLE_STATUS_DISPLAY_TIME;
    }

    public void setList(ArrayList<StatusModel> statusList) {
        this.statusList = statusList;
    }

    private void prepareValues() {
        top = mGapBetweenProgressBars;
        bottom = mGapBetweenProgressBars + mProgressHeight;
        mProgressPaint.setColor(progressBarSecondaryColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressBarRectF = new RectF(0, top, 0, bottom);
    }

    public void setProgressBarsCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count cannot be less than 1");
        }
        this.progressBarsCount = count;
        progressBarRightEdge = new int[progressBarsCount];
        calculateWidthOfEachProgressBar(progressBarsCount);
        invalidate();
        startAnimating(0);
    }

    public void setProgressBarHeight(int dpValue) {
        mProgressHeight = getPxFromDp(dpValue);
        invalidate();
    }

    public void setGapBetweenProgressBars(int dpValue) {
        mGapBetweenProgressBars = getPxFromDp(dpValue);
        invalidate();
    }

    public void setProgressPrimaryColor(int color) {
        progressBarPrimaryColor = color;
        invalidate();
    }

    public void setProgressSecondaryColor(int color) {
        progressBarSecondaryColor = color;
        invalidate();
    }

    public void setSingleStatusDisplayTime(int time) {
        singleStatusDisplayTime = time;
        invalidate();
    }

    public void pauseProgress() {
        if (progressAnimator != null) {
            if (progressAnimator.isRunning()) {
                progressAnimator.pause();
            }
        }
    }

    public void resumeProgress() {
        if (progressAnimator != null) {
            if (progressAnimator.isPaused()) {
                progressAnimator.resume();
            }
        }
    }

    public void cancelAnimation() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
            isCancelled = true;
        }
    }

    public void finishAnimation() {
        if (progressAnimator != null) {
            progressAnimator.end();
            isCancelled = false;
        }
    }

    public void endCurrent() {
        if (progressAnimator != null) {
            progressAnimator.end();
            isCancelled = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < progressBarsCount; i++) {
            int left = (mGapBetweenProgressBars + singleProgressBarWidth) * i + mGapBetweenProgressBars;
            int right = (i + 1) * (mGapBetweenProgressBars + singleProgressBarWidth);
            mProgressPaint.setColor(progressBarSecondaryColor);
            progressBarRectF.set(left, top, right, bottom);
            canvas.drawRoundRect(progressBarRectF, mProgressHeight, mProgressHeight, mProgressPaint);
            right = progressBarRightEdge[i];
            if (right > 0) {
                mProgressPaint.setColor(progressBarPrimaryColor);
                progressBarRectF.set(left, top, right, bottom);
                canvas.drawRoundRect(progressBarRectF, mProgressHeight, mProgressHeight, mProgressPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mScreenWidth - getPaddingStart() + getPaddingEnd();
        int height = getPaddingTop() + getPaddingBottom() + (2 * mGapBetweenProgressBars) + mProgressHeight;
        int w = resolveSizeAndState(width, widthMeasureSpec, 0);
        int h = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    public void startAnimating(final int index) {
        if (index >= progressBarsCount) {
            if (statusPlayerListener != null) {
                statusPlayerListener.onFinishedPlaying();
                return;
            }
        }
        progressAnimator = ValueAnimator.ofInt(0, singleProgressBarWidth);
        progressAnimator.setDuration(statusList.get(index).getDuration());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                progressBarRightEdge[index] = (index + 1) * mGapBetweenProgressBars + index * singleProgressBarWidth + value;
                invalidate();
            }
        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isCancelled) startAnimating(index + 1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isCancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        progressAnimator.start();
        if (statusPlayerListener != null) {
            statusPlayerListener.onStartedPlaying(index);
        }
    }

    private int getPxFromDp(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }

    private void calculateWidthOfEachProgressBar(int progressBarsCount) {
        this.singleProgressBarWidth = (mScreenWidth - (progressBarsCount + 1) * mGapBetweenProgressBars) / progressBarsCount;
    }

    public void setStatusPlayerListener(StatusPlayerListener statusPlayerListener) {
        this.statusPlayerListener = statusPlayerListener;
    }

    public interface StatusPlayerListener {
        void onStartedPlaying(int index);

        void onFinishedPlaying();
    }
}

