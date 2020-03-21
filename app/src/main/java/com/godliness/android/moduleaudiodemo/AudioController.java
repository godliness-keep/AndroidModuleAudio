package com.godliness.android.moduleaudiodemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.godliness.android.moduleaudiodemo.util.ScreenUnit;
import com.godliness.android.moduleaudiodemo.weight.AudioSlideView;
import com.godliness.android.moduleaudiodemo.weight.view.CircleProgressView;
import com.longrise.android.moduleaudio.controller.BaseAudioController;
import com.longrise.android.moduleaudio.params.BaseParams;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-03-18.
 *
 * @author godliness
 */
public final class AudioController extends BaseAudioController implements View.OnClickListener {

    private static final String TAG = "AudioController";

    private static final int DEFAULT_SIZE = ScreenUnit.dip2px(50);

    private AudioSlideView mSlideView;
    private CircleProgressView mProgressView;

    private AudioSlideView.IMovePositionListener mMovePositionCallback;
    private FrameLayout.LayoutParams mFloatLayoutParams;

    private AudioParams mParams;

    public AudioController(Context cxt) {
        super(cxt);
        Log.e(TAG, "new AudioController");
    }

    @Override
    protected int getControllerLayoutId() {
        return R.layout.moduleaudio_sound_chart_view;
    }

    @Override
    protected void initControllerView() {
        mSlideView = findViewById(R.id.audio_slide_view_modulestudy);
        mProgressView = findViewById(R.id.audio_circle_progress_view_modulestudy);
        adjustControllerLayoutParams();
    }

    @Override
    protected void regEvent(boolean event) {
        if (mSlideView != null) {
            mSlideView.setMovePostionListener(event ? getMovePositionCallback() : null);
            mSlideView.setOnClickListener(event ? this : null);
        }
    }

    @Override
    protected <T extends BaseParams> void bindAudioParams(T params) {
        this.mParams = (AudioParams) params;
    }

    @Override
    protected boolean showController(Activity host) {
        return !(host instanceof AudioDemoActivity);
    }

    @Override
    protected void onAudioDestroy() {

    }

    @Override
    public void onAudioProgress(int position, int duration) {
        if (mProgressView != null) {
            mProgressView.setProgress(1000 * position / duration);
        }
        Log.e(TAG, "onAudioProgress:" + position + " duration: " + duration);
    }

    @Override
    public void onAudioState(boolean isPlaying) {
        Log.e(TAG, "onAudioState: " + isPlaying);
    }

    @Override
    public void onAudioPrepared(IMediaPlayer mp) {
    }

    @Override
    public void onAudioCompletion(IMediaPlayer mp) {
    }

    @Override
    public boolean onAudioError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioBuffering(boolean buffering) {
    }

    @Override
    public void onAudioBufferingUpdate(int percent) {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), AudioDemoActivity.class);
        v.getContext().startActivity(intent);
    }

    private void adjustControllerLayoutParams() {
        mFloatLayoutParams = (FrameLayout.LayoutParams) mSlideView.getLayoutParams();
        mFloatLayoutParams.leftMargin = 0;
        mFloatLayoutParams.topMargin = ScreenUnit.getHeight() - DEFAULT_SIZE * 4;
    }

    private AudioSlideView.IMovePositionListener getMovePositionCallback() {
        if (mMovePositionCallback == null) {
            mMovePositionCallback = new AudioSlideView.IMovePositionListener() {
                @Override
                public void layoutPosition(int left, int top, int right, int bottom) {
                    if (mFloatLayoutParams != null) {
                        mFloatLayoutParams.leftMargin = left;
                        mFloatLayoutParams.topMargin = top;
                    }
                }
            };
        }
        return mMovePositionCallback;
    }
}
