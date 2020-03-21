package com.godliness.android.moduleaudiodemo.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.godliness.android.moduleaudiodemo.util.ScreenUnit;


/**
 * Created by godliness on 2019/05/22
 *
 * @author godliness
 * 圆环进度条
 */
public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressView";

    private int mCircleLineWidth = ScreenUnit.dip2px(4);
    private int mCircleProgressColor = Color.parseColor("#FFA200");
    private int mCircleBackgroundColor = Color.parseColor("#40000000");

    private int mMaxProgress = 1000;
    private int mProgress = 0;

    private final RectF mRectF;
    private final Paint mProgressPaint;

    private int mWidth;
    private int mHeight;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground(null);

        this.mRectF = new RectF();
        this.mProgressPaint = new Paint();
        this.mProgressPaint.setStrokeWidth(mCircleLineWidth);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setAntiAlias(true);
    }

    private void drawProgressArc(Canvas canvas, int circle) {
        if (mProgressPaint != null && mRectF != null) {
            this.mProgressPaint.setColor(mCircleProgressColor);

            int forceHalf = mCircleLineWidth / 2;
            mRectF.left = forceHalf;
            mRectF.top = forceHalf;
            mRectF.right = circle - forceHalf;
            mRectF.bottom = circle - forceHalf;
            canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mProgressPaint);
        }
    }

    private void drawBackProgressArc(Canvas canvas, int circle) {
        if (mProgressPaint != null && mRectF != null) {
            mProgressPaint.setColor(mCircleBackgroundColor);

            int forceHalf = mCircleLineWidth / 2;
            mRectF.left = forceHalf;
            mRectF.top = forceHalf;
            mRectF.right = circle - forceHalf;
            mRectF.bottom = circle - forceHalf;
            canvas.drawArc(mRectF, -90, 360, false, mProgressPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getWidth();
        this.mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth != mHeight) {
            int min = Math.min(mWidth, mHeight);
            mWidth = min;
        }

        if (mProgress >= mMaxProgress) {
            drawProgressArc(canvas, mWidth);
        } else {
            drawBackProgressArc(canvas, mWidth);
            drawProgressArc(canvas, mWidth);
        }
    }

    /**
     * 设置圆环宽度
     *
     * @param dpWidth width
     */
    public void setCircleLineWidth(int dpWidth) {
        this.mCircleLineWidth = ScreenUnit.dip2px(dpWidth);
    }

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    /**
     * 在非UIThread中更新
     */
    public void postProgress(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

}
