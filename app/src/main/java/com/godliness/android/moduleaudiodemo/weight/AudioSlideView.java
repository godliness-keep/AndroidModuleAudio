package com.godliness.android.moduleaudiodemo.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.godliness.android.moduleaudiodemo.util.ScreenUnit;


/**
 * Created by godliness on 2019/05/22
 *
 * @author godliness
 * 滑动View
 */
public final class AudioSlideView extends FrameLayout {

    private static final int MAX_WIDTH = ScreenUnit.getWidth();
    private static final int MAX_HEIGHT = ScreenUnit.getHeight() - ScreenUnit.getStatusBarHeight();

    private int mStartX;
    private int mStartY;

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    private int mMoveLeft;
    private int mMoveTop;
    private int mMoveRight;
    private int mMoveBottom;

    private int mWidth;
    private int mHeight;

    private IMovePositionListener mPositionListener;

    public AudioSlideView(Context context) {
        this(context, null);
    }

    public AudioSlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                mLeft = getLeft();
                mTop = getTop();
                mRight = getRight();
                mBottom = getBottom();
                break;

            case MotionEvent.ACTION_MOVE:
                int stopX = (int) event.getRawX();
                int stopY = (int) event.getRawY();

                //滑动距离
                int deltaX = stopX - mStartX;
                int deltaY = stopY - mStartY;

                mMoveLeft = mLeft + deltaX;
                mMoveTop = mTop + deltaY;
                mMoveRight = mRight + deltaX;
                mMoveBottom = mBottom + deltaY;

                //避免超出屏幕范围
                if (mMoveLeft < 0) {
                    mMoveLeft = 0;
                    mMoveRight = mMoveLeft + mWidth;
                } else if (mMoveRight > MAX_WIDTH) {
                    mMoveRight = MAX_WIDTH;
                    mMoveLeft = mMoveRight - mWidth;
                }
                if (mMoveTop < 0) {
                    mMoveTop = 0;
                    mMoveBottom = mMoveTop + mHeight;
                } else if (mMoveBottom > MAX_HEIGHT) {
                    mMoveBottom = MAX_HEIGHT;
                    mMoveTop = mMoveBottom - mHeight;
                }
                layout(mMoveLeft, mMoveTop, mMoveRight, mMoveBottom);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                int upX = (int) event.getRawX();
                int upY = (int) event.getRawY();
                if (Math.abs(upX - mStartX) > 0 || Math.abs(upY - mStartY) > 0) {
                    if (mPositionListener != null) {
                        mPositionListener.layoutPosition(mMoveLeft, mMoveTop, mMoveRight, mMoveBottom);
                    }
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    /**
     * 设置监听View移动位置回调
     *
     * @param positionListener IMovePositionListener
     */
    public void setMovePostionListener(IMovePositionListener positionListener) {
        this.mPositionListener = positionListener;
    }

    /**
     * 监听手势移动距离
     */
    public interface IMovePositionListener {

        /**
         * 移动距离回调
         *
         * @param left   left
         * @param top    top
         * @param right  right
         * @param bottom bottom
         */
        void layoutPosition(int left, int top, int right, int bottom);
    }

    @Override
    public void invalidate() {
        postInvalidate();
    }
}
