package com.longrise.android.moduleaudio.audio.service;

import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import com.longrise.android.moduleaudio.audio.listener.OnAudioProgressListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioServiceStateListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioStateListener;

/**
 * Created by godliness on 2020-03-16.
 *
 * @author godliness
 */
public final class AudioBridge extends Binder {

    private static final String TAG = "AudioBridge";

    private AudioService mAudioService;
    private BackgroundAudioOption mOption;

    private boolean mStartService;
    private String mCurrentPath;

    public AudioBridge(AudioService audioService) {
        this.mAudioService = audioService;
    }

    /**
     * 设置音频地址
     */
    public void setAudioPath(String path) {
        setAudioPath(path, 0);
    }

    public void setAudioPath(String path, int position) {
        if (TextUtils.equals(path, mCurrentPath)) {
            return;
        }
        if (mAudioService != null) {
            mAudioService.play(path, position);
        }
        mCurrentPath = path;
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        if (mAudioService != null) {
            return mAudioService.isPlaying();
        }
        return false;
    }

    /**
     * 获取当前资源总时长 单位 ms
     */
    public int getDuration() {
        if (mAudioService != null) {
            return mAudioService.getDuration();
        }
        return -1;
    }

    /**
     * 判断音频是否进入后台
     */
    public boolean canIntoBackground() {
        if (mAudioService != null) {
            return mAudioService.audioIntoBackground();
        }
        return false;
    }

    /**
     * 播放
     */
    public void start() {
        if (mAudioService != null) {
            mAudioService.startBefore();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mAudioService != null) {
            mAudioService.pauseBefore();
        }
    }

    /**
     * 设置断点位置
     */
    public void seekTo(int position) {
        if (mAudioService != null) {
            mAudioService.seekTo(position);
        }
    }

    /**
     * 设置倍速
     */
    public void setSpeed(float speed) {
        if (mAudioService != null) {
            mAudioService.setSpeed(speed);
        }
    }

    /**
     * 移除播放进度监听
     */
    public void removeAudioProgressUpdater() {
        if (mAudioService != null) {
            mAudioService.removeProgressListener();
        }
    }

    /**
     * 添加播放进度监听
     */
    public void addAudioProgressUpdater() {
        if (mAudioService != null) {
            mAudioService.addProgressListener();
        }
    }

    public void setAudioProgressListener(OnAudioProgressListener audioProgressCallback) {
        if (mAudioService != null) {
            mAudioService.setAudioProgressListener(audioProgressCallback);
        }
    }

    public void setAudioStateListener(OnAudioStateListener audioStateCallback) {
        if (mAudioService != null) {
            mAudioService.setAudioStateListener(audioStateCallback);
        }
    }

    public void setAudioServiceStateListener(OnAudioServiceStateListener audioServiceStateCallback) {
        if (mAudioService != null) {
            mAudioService.setAudioServiceStateListener(audioServiceStateCallback);
        }
    }

    public void stopAudioService() {
        if (mAudioService != null) {
            mAudioService.stopSelf();
        }
    }

    public BackgroundAudioOption getOption() {
        if (mOption == null) {
            return mOption = new BackgroundAudioOption(this);
        }
        return mOption;
    }

    public boolean onStart() {
        return mStartService;
    }

    void onStart(boolean onStart) {
        this.mStartService = onStart;
    }

    void notifyAudioIntoBackground() {

    }

    void release() {
        mAudioService = null;
    }
}
