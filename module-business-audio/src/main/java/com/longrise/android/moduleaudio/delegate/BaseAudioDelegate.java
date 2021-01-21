package com.longrise.android.moduleaudio.delegate;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.SeekBar;

import com.longrise.android.moduleaudio.audio.listener.OnAudioProgressListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioStateListener;
import com.longrise.android.moduleaudio.audio.service.AudioBridge;
import com.longrise.android.moduleaudio.audio.service.AudioService;
import com.longrise.android.moduleaudio.audio.service.BackgroundAudioOption;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-04-07.
 *
 * @author godliness
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseAudioDelegate<T extends Activity> extends DelegateLifecycle<T> implements OnAudioProgressListener, OnAudioStateListener {

    private AudioBridge mAudioBridge;
    private ServiceConnection mServiceConnection;

    private final StringBuilder mFormatBuilder;
    private final Formatter mFormatter;
    private SeekBarChangeCallbackExtend mStateChangeCallback;

    private String mCurrentPath;
    private int mPosition;

    protected BaseAudioDelegate(T target) {
        super(target);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    @Override
    protected final void initDelegate() {
        connectToAudioService();
    }

    /**
     * Progress of current player
     *
     * @param stringForPosition current position
     * @param duration          resource duration
     * @param position          current position
     */
    protected abstract void updateAudioProgress(String stringForPosition, String duration, int position);

    /**
     * Bind {@link SeekBar} change listener
     *
     * @param callback {@link SeekBar.OnSeekBarChangeListener}
     */
    protected abstract void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener callback);

    /**
     * Can audio be played in the background
     */
    protected boolean canIntoBackground() {
        return false;
    }

    /**
     * Configuration audio options when player in the background
     */
    protected void onConfigurationForBackground(@NonNull BackgroundAudioOption audioOption) {

    }

    @Override
    public void onAudioPrepared(IMediaPlayer mp) {
        if (mStateChangeCallback != null) {
            mStateChangeCallback.updateDuration((int) mp.getDuration());
        }
    }

    public final void setAudioPath(String path) {
        setAudioPath(path, 0);
    }

    public final void setAudioPath(String path, int position) {
        if (mAudioBridge != null) {
            mAudioBridge.setAudioPath(path, position);
        } else {
            this.mCurrentPath = path;
            this.mPosition = position;
        }
    }

    public final AudioBridge getAudioBridge() {
        return mAudioBridge;
    }

    public final void start() {
        if (mAudioBridge != null) {
            mAudioBridge.start();
        }
    }

    public final void pause() {
        if (mAudioBridge != null) {
            mAudioBridge.pause();
        }
    }

    public final boolean isPlaying() {
        if (mAudioBridge != null) {
            return mAudioBridge.isPlaying();
        }
        return false;
    }

    @Override
    protected final void onAudioTargetIntoResumed() {
        if (mAudioBridge != null) {
            mAudioBridge.addAudioProgressUpdater();
        }
    }

    @Override
    protected final void onAudioTargetIntoPaused() {
        if (mAudioBridge != null) {
            mAudioBridge.removeAudioProgressUpdater();
        }
    }

    @Override
    protected final void onAudioTargetIntoFinish() {
        final boolean canIntoBackgroundOfService = mAudioBridge != null && mAudioBridge.canIntoBackground();
        if (canIntoBackground() && canIntoBackgroundOfService) {
            audioIntoBackground();
        }
        if (mServiceConnection != null) {
            getTarget().unbindService(mServiceConnection);
            mServiceConnection = null;
        }
    }

    @Override
    protected final void onAudioTargetIntoDestroy() {
        if (!canIntoBackground()) {
            connectToAudioServiceCallback(false);
        }
    }

    @Override
    public final void onAudioProgress(int position, int duration) {
        if (duration > 0) {
            updateAudioProgress(stringForTime(position), stringForTime(duration), (int) (1000L * position / duration));
        }
    }

    private void connectToAudioService() {
        final Intent audioIntent = new Intent(getTarget(), AudioService.class);
        getTarget().bindService(audioIntent, getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private void startAudioService() {
        final Intent audioIntent = new Intent(getTarget(), AudioService.class);
        getTarget().startService(audioIntent);
    }

    private void connectToAudioServiceCallback(boolean event) {
        if (mAudioBridge != null) {
            mAudioBridge.setAudioProgressListener(event ? this : null);
            mAudioBridge.setAudioStateListener(event ? this : null);
        }

        bindSeekBarChangeListener(event ? getStateChangeCallback() : null);
    }

    private String stringForTime(int timeMs) {
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
        if (canIntoBackground()) {
            final BackgroundAudioOption option = mAudioBridge.getOption();
            onConfigurationForBackground(option);
            option.runInBackground(getTarget());
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
                    if (canIntoBackground() && !mAudioBridge.onStart()) {
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
            mStateChangeCallback = new SeekBarChangeCallbackExtend() {

                private int mChangePosition;
                private int mDuration = -1;

                @Override
                public void updateDuration(int duration) {
                    this.mDuration = duration;
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) {
                        return;
                    }
                    if (mDuration <= 0) {
                        mDuration = mAudioBridge.getDuration();
                    }
                    mChangePosition = mDuration * progress / 1000;
                    onAudioProgress(mChangePosition, mDuration);
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

    private interface SeekBarChangeCallbackExtend extends SeekBar.OnSeekBarChangeListener {

        /**
         * Update duration when resource prepared
         */
        void updateDuration(int duration);
    }
}
