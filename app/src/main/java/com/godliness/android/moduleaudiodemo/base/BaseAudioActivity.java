package com.godliness.android.moduleaudiodemo.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.SeekBar;

import com.longrise.android.moduleaudio.audio.listener.OnAudioProgressListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioStateListener;
import com.longrise.android.moduleaudio.audio.service.AudioBridge;
import com.longrise.android.moduleaudio.audio.service.AudioService;
import com.longrise.android.moduleaudio.audio.service.BackgroundAudioOption;
import com.longrise.android.mvp.internal.BaseMvpActivity;
import com.longrise.android.mvp.internal.mvp.BasePresenter;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by godliness on 2020-03-16.
 *
 * @author godliness
 * <p>
 * Audio管理基础Activity
 */
public abstract class BaseAudioActivity<P extends BasePresenter> extends BaseMvpActivity<P> implements
        OnAudioProgressListener, OnAudioStateListener {

    private AudioBridge mAudioBridge;
    private String mCurrentPath;
    private int mPosition;

    private SeekBar.OnSeekBarChangeListener mStateChangeCallback;
    private ServiceConnection mServiceConnection;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToAudioService();
    }

    /**
     * 当前播放进度，该方法相关单位已作转化，如果需要原生进度{@link #onAudioProgress(int, int)}
     *
     * @param position 当前进度
     * @param duration 总长度
     * @param progress 进度条进度
     */
    protected abstract void onAudioProgress(String position, String duration, int progress);

    /**
     * 绑定进度条
     *
     * @param seekBarChangeListener SeekBar listener
     */
    protected abstract void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener);

    /**
     * 设置音频地址
     *
     * @param path 音频地址
     */
    protected void setAudioPath(String path) {
        setAudioPath(path, 0);
    }

    /**
     * 设置音频地址
     *
     * @param path     音频地址
     * @param position 设置断点位置
     */
    protected void setAudioPath(String path, int position) {
        if (mAudioBridge != null) {
            mAudioBridge.setAudioPath(path, position);
        } else {
            this.mCurrentPath = path;
            this.mPosition = position;
        }
    }

    /**
     * 是否需要进入后台音频管理
     */
    protected boolean runIntoBackground() {
        return false;
    }

    /**
     * 配置后台音频管理{@link BackgroundAudioOption}
     */
    protected void runInBackgroundFromConfiguration(@NonNull BackgroundAudioOption option) {
    }

    /**
     * 返回音频控制器桥梁 {@link AudioBridge}
     */
    protected final AudioBridge getAudioBridge() {
        return mAudioBridge;
    }

    /**
     * 音频播放进度
     */
    @Override
    public void onAudioProgress(int position, int duration) {
        if (duration != 0) {
            onAudioProgress(stringForTime(position), stringForTime(duration), (int) (1000L * position / duration));
        }
    }

    /**
     * 音频缓冲状态
     */
    @Override
    public void onAudioBuffering(boolean buffering) {
    }

    /**
     * 音频缓冲进度
     */
    @Override
    public void onAudioBufferingUpdate(int percent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAudioBridge != null) {
            mAudioBridge.addAudioProgressUpdater();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinishing()) {
            if (mAudioBridge != null) {
                mAudioBridge.removeAudioProgressUpdater();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mAudioBridge != null) {
            if (runIntoBackground() && mAudioBridge.canIntoBackground()) {
                audioIntoBackground();
            }
        }

        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
            mServiceConnection = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!runIntoBackground()) {
            connectToAudioServiceCallback(false);
        }
        mAudioBridge = null;
    }

    private void connectToAudioService() {
        final Intent audioIntent = new Intent(this, AudioService.class);
        bindService(audioIntent, getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private void startAudioService() {
        final Intent audioIntent = new Intent(this, AudioService.class);
        startService(audioIntent);
    }

    private void connectToAudioServiceCallback(boolean event) {
        if (mAudioBridge != null) {
            mAudioBridge.setAudioProgressListener(event ? this : null);
            mAudioBridge.setAudioStateListener(event ? this : null);
        }
        bindSeekBarChangeListener(event ? getStateChangeCallback() : null);
    }

    private String stringForTime(int timeMs) {
        initStringForTime();
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void audioIntoBackground() {
        if (runIntoBackground()) {
            final BackgroundAudioOption option = mAudioBridge.getOption();
            runInBackgroundFromConfiguration(option);
            option.runInBackground(this);
        }
    }

    private void initStringForTime() {
        if (mFormatBuilder == null) {
            mFormatBuilder = new StringBuilder();
        }
        if (mFormatter == null) {
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        }
    }

    private ServiceConnection getServiceConnection() {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mAudioBridge = (AudioBridge) service;
                    if (!TextUtils.isEmpty(mCurrentPath)) {
                        mAudioBridge.setAudioPath(mCurrentPath, mPosition);
                    }
                    connectToAudioServiceCallback(true);
                    if (runIntoBackground() && !mAudioBridge.onStart()) {
                        startAudioService();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
        }
        return mServiceConnection;
    }

    private SeekBar.OnSeekBarChangeListener getStateChangeCallback() {
        if (mStateChangeCallback == null) {
            mStateChangeCallback = new SeekBar.OnSeekBarChangeListener() {

                private int mChangePosition;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) {
                        return;
                    }
                    if (mAudioBridge != null) {
                        final int duration = mAudioBridge.getDuration();
                        mChangePosition = duration * progress / 1000;
                        onAudioProgress(mChangePosition, duration);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mAudioBridge != null) {
                        mAudioBridge.removeAudioProgressUpdater();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mAudioBridge != null) {
                        mAudioBridge.seekTo(mChangePosition);
                        mAudioBridge.addAudioProgressUpdater();
                    }
                }
            };
        }
        return mStateChangeCallback;
    }
}
