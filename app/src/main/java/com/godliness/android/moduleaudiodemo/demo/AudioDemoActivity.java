package com.godliness.android.moduleaudiodemo.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.godliness.android.moduleaudiodemo.R;
import com.godliness.android.moduleaudiodemo.util.AppContext;

/**
 * Created by godliness on 2020-04-07.
 *
 * @author godliness
 */
public final class AudioDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mPause;
    private TextView mCurrent;
    private TextView mEnd;
    private SeekBar mProgress;

    private String mPath = "http://download.yxybb.com/project/INSH/video/2018/12/19/yp001.mp3";

    private AudioDelegate mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.register(this);
        setContentView(R.layout.moduleaudio_audio_demo);
        initView();
        regEvent(true);
    }

    private void initView() {
        mPause = findViewById(R.id.moduleaudio_controller_bar_iv_play_portrait);
        mCurrent = findViewById(R.id.moduleaudio_controller_bar_tv_current_portrait);
        mEnd = findViewById(R.id.moduleaudio_controller_bar_tv_end_portrait);
        mProgress = findViewById(R.id.moduleaudio_controller_bar_progress_portrait);

        mDelegate = new AudioDelegate(this);
        mDelegate.setAudioPath(mPath);
    }

    @Override
    public void onClick(View v) {
        if (mDelegate.isPlaying()) {
            mDelegate.pause();
        } else {
            mDelegate.start();
        }
    }

    public void updateAudioProgress(String position, String duration, int progress) {
        if (mCurrent != null) {
            mCurrent.setText(position);
        }
        if (mEnd != null) {
            mEnd.setText(duration);
        }
        mProgress.setProgress(progress);
    }

    public void bindSeekbarChangeListener(SeekBar.OnSeekBarChangeListener callback) {
        mProgress.setOnSeekBarChangeListener(callback);
    }

    public void updateAudioState(boolean isPlaying) {
        if (mPause != null) {
            mPause.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        regEvent(false);
        mDelegate.release();
    }

    private void regEvent(boolean regEvent) {
        mPause.setOnClickListener(regEvent ? this : null);
    }
}
