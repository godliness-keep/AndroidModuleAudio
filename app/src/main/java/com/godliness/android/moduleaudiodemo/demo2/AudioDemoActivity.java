package com.godliness.android.moduleaudiodemo.demo2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.godliness.android.moduleaudiodemo.R;
import com.godliness.android.moduleaudiodemo.demo.AudioController;
import com.godliness.android.moduleaudiodemo.demo.AudioParams;
import com.godliness.android.moduleaudiodemo.util.AppContext;
import com.longrise.android.moduleaudio.audio.service.AudioBridge;
import com.longrise.android.moduleaudio.audio.service.BackgroundAudioOption;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-03-16.
 *
 * @author godliness
 */
public final class AudioDemoActivity extends BaseAudioActivity implements View.OnClickListener {

    private static final String TAG = "AudioDemoActivity";

    private ImageView mPause;
    private TextView mCurrent;
    private TextView mEnd;
    private SeekBar mProgress;

    @Override
    protected int getLayoutResourceId(@Nullable Bundle savedInstanceState) {
        AppContext.register(this);
        return R.layout.moduleaudio_audio_demo;
    }

    @Override
    protected void initView() {
        mPause = findViewById(R.id.moduleaudio_controller_bar_iv_play_portrait);
        mCurrent = findViewById(R.id.moduleaudio_controller_bar_tv_current_portrait);
        mEnd = findViewById(R.id.moduleaudio_controller_bar_tv_end_portrait);
        mProgress = findViewById(R.id.moduleaudio_controller_bar_progress_portrait);


        setAudioPath("http://download.yxybb.com/project/INSH/video/2018/12/19/yp001.mp3");
    }

    @Override
    protected void regEvent(boolean regEvent) {
        mPause.setOnClickListener(regEvent ? this : null);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moduleaudio_controller_bar_iv_play_portrait) {
            final AudioBridge bridge = getAudioBridge();
            if (bridge.isPlaying()) {
                bridge.pause();
            } else {
                bridge.start();
            }
        }
    }

    @Override
    protected boolean runIntoBackground() {
        return true;
    }

    private final AudioParams mAudioParams = new AudioParams();

    @Override
    protected void runInBackgroundFromConfiguration(BackgroundAudioOption option) {
        option.controller(AudioController.class);
        option.params(mAudioParams);
    }

    @Override
    protected void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        mProgress.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    @Override
    protected void onAudioProgress(String position, String duration, int progress) {
        if (mCurrent != null) {
            mCurrent.setText(position);
        }
        if (mEnd != null) {
            mEnd.setText(duration);
        }
        mProgress.setProgress(progress);
    }

    @Override
    public void onAudioState(boolean isPlaying) {
        if (mPause != null) {
            mPause.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void onAudioPrepared(IMediaPlayer mp) {
        Log.e(TAG, "onAudioPrepared");
    }

    @Override
    public void onAudioCompletion(IMediaPlayer mp) {
        Log.e(TAG, "onCompletion");
    }

    @Override
    public boolean onAudioError(IMediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onAudioError");
        return false;
    }

    @Override
    public void onAudioBufferingUpdate(int percent) {
        mProgress.setSecondaryProgress(percent);
    }

}
