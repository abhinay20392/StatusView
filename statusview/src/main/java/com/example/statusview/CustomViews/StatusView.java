package com.example.statusview.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.statusview.Helpers.StatusPreference;
import com.example.statusview.Modal.StatusModel;
import com.example.statusview.R;
import com.example.statusview.Views.StatusPlayer;

import java.util.ArrayList;

public class StatusView  extends View {
    public static final int STATUS_IMAGE_RADIUS_IN_DP = 36;
    public static final int STATUS_INDICATOR_WIDTH_IN_DP = 4;
    public static final int STATUS_BETWEEN_IMAGE_AND_INDICATOR = 4;
    public static final int START_ANGLE = 270;
    public static int ANGEL_OF_GAP = 15;
    public static final String PENDING_INDICATOR_COLOR = "#009988";
    public static final String VISITED_INDICATOR_COLOR = "#33009988";
    private int mStatusImageRadiusInPx;
    private int mStatusIndicatorWidthInPx;
    private int mSpaceBetweenImageAndIndicator;
    private int mPendingIndicatorColor;
    private int mVisistedIndicatorColor;
    private int mViewWidth;
    private int mViewHeight;
    private int mIndicatoryOffset;
    private int mIndicatorImageOffset;
    private Resources resources;
    private ArrayList<StatusModel> statusImageUris;
    private Paint mIndicatorPaint;
    private int indicatorCount;
    private int indicatorSweepAngle;
    private Bitmap mIndicatorImageBitmap;
    private Rect mIndicatorImageRect;
    private Context mContext;
    StatusPreference statusPreference;

    public StatusView(Context context) {
        super(context);
        init(context);
        setDefaults();
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatusView, 0, 0);
        try {
            mStatusImageRadiusInPx = getPxFromDp((int) ta.getDimension(R.styleable.StatusView_statusImageRadius, STATUS_IMAGE_RADIUS_IN_DP));
            mStatusIndicatorWidthInPx = getPxFromDp((int) ta.getDimension(R.styleable.StatusView_statusItemIndicatorWidth, STATUS_INDICATOR_WIDTH_IN_DP));
            mSpaceBetweenImageAndIndicator = getPxFromDp((int) ta.getDimension(R.styleable.StatusView_spaceBetweenImageAndIndicator, STATUS_BETWEEN_IMAGE_AND_INDICATOR));
            mPendingIndicatorColor = ta.getColor(R.styleable.StatusView_pendingIndicatorColor, Color.parseColor(PENDING_INDICATOR_COLOR));
            mVisistedIndicatorColor = ta.getColor(R.styleable.StatusView_visitedIndicatorColor, Color.parseColor(VISITED_INDICATOR_COLOR));
        } finally {
            ta.recycle();
        }
        prepareValues();
    }

    private void init(Context context) {
        this.mContext = context;
        statusPreference = new StatusPreference(context);
        resources = context.getResources();
        statusImageUris = new ArrayList<>();
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void setDefaults() {
        mStatusImageRadiusInPx = getPxFromDp(STATUS_IMAGE_RADIUS_IN_DP);
        mStatusIndicatorWidthInPx = getPxFromDp(STATUS_INDICATOR_WIDTH_IN_DP);
        mSpaceBetweenImageAndIndicator = getPxFromDp(STATUS_BETWEEN_IMAGE_AND_INDICATOR);
        mPendingIndicatorColor = Color.parseColor(PENDING_INDICATOR_COLOR);
        mVisistedIndicatorColor = Color.parseColor(VISITED_INDICATOR_COLOR);
        prepareValues();
    }

    private void prepareValues() {
        mViewHeight = 2 * (mStatusIndicatorWidthInPx + mSpaceBetweenImageAndIndicator + mStatusImageRadiusInPx);
        mViewWidth = mViewHeight;
        mIndicatoryOffset = mStatusIndicatorWidthInPx / 2;
        mIndicatorImageOffset = mStatusIndicatorWidthInPx + mSpaceBetweenImageAndIndicator;
        mIndicatorImageRect = new Rect(mIndicatorImageOffset, mIndicatorImageOffset, mViewWidth - mIndicatorImageOffset, mViewHeight - mIndicatorImageOffset);
    }

    public void resetStatusVisits(){
        statusPreference.clearStatusPreferences();
    }

    public void setImageUris(ArrayList<StatusModel> imageUris) {
        this.statusImageUris = imageUris;
        this.indicatorCount = imageUris.size();
        calculateSweepAngle(indicatorCount);
        invalidate();
        loadFirstImageBitamp();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            navigateToStatusPlayerPage();
            return true;
        }
        return true;
    }

    private void navigateToStatusPlayerPage() {
        Intent intent = new Intent(mContext, StatusPlayer.class);
        intent.putParcelableArrayListExtra(StatusPlayer.STATUS_IMAGE_KEY,statusImageUris);
        mContext.startActivity(intent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mIndicatorPaint.setColor(mPendingIndicatorColor);
        mIndicatorPaint.setStrokeWidth(mStatusIndicatorWidthInPx);
        int startAngle = START_ANGLE + ANGEL_OF_GAP / 2;
        for (int i = 0; i < indicatorCount; i++) {
            mIndicatorPaint.setColor(getIndicatorColor(i));
            canvas.drawArc(mIndicatoryOffset, mIndicatoryOffset, mViewWidth - mIndicatoryOffset, mViewHeight - mIndicatoryOffset, startAngle, indicatorSweepAngle - ANGEL_OF_GAP / 2, false, mIndicatorPaint);
            startAngle += indicatorSweepAngle + ANGEL_OF_GAP / 2;
        }
        if (mIndicatorImageBitmap != null) {
            canvas.drawBitmap(mIndicatorImageBitmap, null, mIndicatorImageRect, null);
        }
    }

    private int getIndicatorColor(int index) {
        return statusPreference.isStatusVisited(statusImageUris.get(index).imageUri) ? mVisistedIndicatorColor : mPendingIndicatorColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingStart() + getPaddingEnd() + mViewWidth;
        int height = getPaddingTop() + getPaddingBottom() + mViewHeight;
        int w = resolveSizeAndState(width, widthMeasureSpec, 0);
        int h = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    private void loadFirstImageBitamp() {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .asBitmap()
                .apply(options)
                .load(statusImageUris.get(0).imageUri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mIndicatorImageBitmap = resource;
                        invalidate();
                    }
                });
    }

    private void calculateSweepAngle(int itemCounts) {
        if (itemCounts == 1) {
            ANGEL_OF_GAP = 0;
        }
        this.indicatorSweepAngle = (360 / itemCounts) - ANGEL_OF_GAP / 2;
    }

    private int getPxFromDp(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }
}
