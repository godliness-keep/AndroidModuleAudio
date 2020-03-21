package com.godliness.android.moduleaudiodemo.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


import com.godliness.android.moduleaudiodemo.util.ScreenUnit;

import java.util.Random;

/**
 * Created by godliness on 2019/05/23
 *
 * @author godliness
 * 音谱
 */
public final class SoundChartView extends View {

    private static final int DEFAULT_ITEM_WIDTH = ScreenUnit.dip2px(3);

    private Paint paint;
    /**
     * 矩形条目的数目，默认4条目
     */
    private int mChartCount = 4;
    private int mWidth;
    private int mHeight;
    /**
     * 定义每一个条目的宽度
     */
    private int mChartItemWidth = DEFAULT_ITEM_WIDTH;
    /**
     * 每个item的偏移量
     */
    private int mItemOffset = 6;
    private final Random mRandom = new Random();

    public SoundChartView(Context context) {
        this(context, null);
    }

    public SoundChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setItemCount(int count) {
        this.mChartCount = count;
    }

    public void setItemWidth(int itemWidthDp) {
        this.mChartItemWidth = ScreenUnit.dip2px(itemWidthDp);
    }

    public void setItemOffset(int itemOffset) {
        this.mItemOffset = itemOffset;
    }

    public void stopChartBeat() {

    }

    private void initView() {
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mChartCount; i++) {
//            mRandom = Math.random();
//            float rectItemHeight = (float) (mHeight * mRandom);
            float rectItemHeight = mRandom.nextInt(mHeight);
            canvas.drawRect((float) (mWidth * 0.3 / 2 + mChartItemWidth * i + mItemOffset), rectItemHeight,
                    (float) (mWidth * 0.3 / 2 + mChartItemWidth * (i + 1)), mHeight, paint);
        }
        postInvalidateDelayed(200);
    }

    public void removeInvalidate() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeMessages(1);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mChartItemWidth = (int) ((mWidth * 0.6) / mChartCount);


        // 给paint设置LinearGradient属性
//        LinearGradient linearGradient = new LinearGradient(0, 0,
//                rectItemWidth, height,
//                Color.BLUE, Color.GREEN,
//                Shader.TileMode.CLAMP);
//        paint.setShader(linearGradient);

    }
}
