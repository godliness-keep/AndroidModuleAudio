package com.longrise.android.moduleaudio.audio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.longrise.android.moduleaudio.audio.listener.OnAudioProgressListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioServiceStateListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioStateListener;

import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by godliness on 2020-03-15.
 *
 * @author godliness
 */
public final class AudioService extends Service implements Handler.Callback, IjkMediaPlayer.OnPreparedListener, IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnErrorListener, IjkMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = "AudioService";

    private AudioBridge mAudioBridge;
    private AudioManager mAudioManager;
    private Handler mAudioHandler;

    private IjkMediaPlayer mPlayer;

    /**
     * all possible internal states
     */
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState = STATE_IDLE;

    private static final int UPDATE_PROGRESS_MSG = 0;

    private OnAudioProgressListener mAudioProgressCallback;
    private OnAudioStateListener mAudioStateCallback;
    private OnAudioServiceStateListener mAudioServiceStateCallback;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeCallback;

    private int mDuration = -1;
    private int mPosition;
    private boolean mHandPause;

    private boolean mAlreadyBind;

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == UPDATE_PROGRESS_MSG) {
            int pos = calcProgress();
            msg = mAudioHandler.obtainMessage(UPDATE_PROGRESS_MSG);
            mAudioHandler.sendMessageDelayed(msg, 1000 - (pos % 1000));
            return true;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAudioBridge == null) {
            mAudioBridge = new AudioBridge(this);
        }
        if (mAudioHandler == null) {
            mAudioHandler = new Handler(this);
        }
        createMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAudioBridge.onStart(true);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mAlreadyBind = true;
        Log.e(TAG, "onBind");
        return mAudioBridge;
    }

    @Override
    public void onRebind(Intent intent) {
        mAlreadyBind = true;
        if (isPlaying()) {
            sendUpdateProgressMessage();
        }
        //request audio focus
        requestAudioFocus();
        super.onRebind(intent);

        Log.e(TAG, "onRebind");
    }

    public boolean audioIntoBackground() {
        return (mCurrentState < STATE_PREPARED) || isPlaying();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mAlreadyBind = false;
        if (!mAudioBridge.onStart()) {
            removeUpdateProgressMessage();
            unBindMediaPlayerClient();
        } else {
            if (audioIntoBackground()) {
                mAudioBridge.notifyAudioIntoBackground();
            } else {
                stopSelf();
            }
        }
        Log.e(TAG, "onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        if (mAudioServiceStateCallback != null) {
            mAudioServiceStateCallback.onAudioServiceDestroy();
        }
        removeUpdateProgressMessage();
        if (mAudioBridge != null) {
            mAudioBridge.onStart(false);
            mAudioBridge.release();
        }
        unBindMediaPlayerClient();
        destroyMediaPlayer();
        super.onDestroy();

        Log.e(TAG, "onDestroy");
    }

    boolean isPlaying() {
        if (mCurrentState <= STATE_PREPARING) {
            return false;
        }
        return mPlayer != null && mPlayer.isPlaying();
    }

    void play(String audioPath, int position) {
        if (mPlayer == null) {
            createMediaPlayer();
        }
        try {
            removeUpdateProgressMessage();
            mPlayer.reset();
            mCurrentState = STATE_IDLE;
//            mPlayer.setDataSource(this, Uri.parse(audioPath), createRequestHeaders());
            mPlayer.setDataSource(this, Uri.parse(audioPath));
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            this.mPosition = position;
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (mAudioStateCallback != null) {
                mAudioStateCallback.onAudioError(mPlayer, -1, -1);
            }
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    void startBefore() {
        if (mCurrentState <= STATE_PREPARING) {
            return;
        }
        start();
        mHandPause = false;
    }

    void pauseBefore() {
        if (mCurrentState <= STATE_PREPARING) {
            return;
        }
        pause();
        mHandPause = true;
    }

    void seekTo(long position) {
        if (mCurrentState <= STATE_PREPARING) {
            return;
        }
        if (mPlayer != null) {
            final long duration = mPlayer.getDuration();
            if (position > duration) {
                position = duration;
            }
            mPlayer.seekTo(position);
        }
    }

    void setSpeed(float value) {
        mPlayer.setSpeed(value);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1);
    }

    int getDuration() {
        return mPlayer != null ? (int) mPlayer.getDuration() : -1;
    }

    int getPosition() {
        return mPlayer != null ? (int) mPlayer.getCurrentPosition() : 0;
    }

    void setAudioProgressListener(OnAudioProgressListener audioProgressCallback) {
        this.mAudioProgressCallback = audioProgressCallback;
        if (mCurrentState >= STATE_PREPARED) {
            calcProgress();
        }
    }

    void setAudioStateListener(OnAudioStateListener audioStateCallback) {
        this.mAudioStateCallback = audioStateCallback;
        if (audioStateCallback != null) {
            if (mCurrentState > STATE_PREPARING) {
                audioStateCallback.onAudioState(isPlaying());
            }
        }
    }

    void setAudioServiceStateListener(OnAudioServiceStateListener audioServiceStateCallback) {
        this.mAudioServiceStateCallback = audioServiceStateCallback;
    }

    void removeProgressListener() {
        removeUpdateProgressMessage();
    }

    void addProgressListener() {
        if (isPlaying()) {
            sendUpdateProgressMessage();
        }
    }

    private void start() {
        if (!isPlaying() && requestAudioFocus()) {
            mPlayer.start();
            mCurrentState = STATE_PLAYING;
            sendUpdateProgressMessage();
            if (mAudioStateCallback != null) {
                mAudioStateCallback.onAudioState(true);
            }
        }
    }

    private void pause() {
        if (isPlaying()) {
            mPlayer.pause();
            mCurrentState = STATE_PAUSED;
            removeUpdateProgressMessage();
            if (mAudioStateCallback != null) {
                mAudioStateCallback.onAudioState(false);
            }
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {
        mediaPlayer.pause();
        mCurrentState = STATE_PREPARED;
        mDuration = (int) mediaPlayer.getDuration();
        if (mAudioStateCallback != null) {
            mAudioStateCallback.onAudioPrepared(mediaPlayer);
        }
        if (mPosition > 0) {
            seekTo(mPosition);
            mPosition = 0;
        }
        if (requestAudioFocus()) {
            start();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        mCurrentState = STATE_PLAYBACK_COMPLETED;
        if (mAudioStateCallback != null) {
            mAudioStateCallback.onAudioState(false);
            mAudioStateCallback.onAudioCompletion(iMediaPlayer);
        }
        removeUpdateProgressMessage();
        if (mAudioProgressCallback != null) {
            mAudioProgressCallback.onAudioProgress(mDuration, mDuration);
        }
        mHandPause = true;

        if (!mAlreadyBind) {
            stopSelf();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        mCurrentState = STATE_ERROR;
        return mAudioStateCallback != null && mAudioStateCallback.onAudioError(iMediaPlayer, what, extra);
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mAudioStateCallback != null) {
                    mAudioStateCallback.onAudioBuffering(true);
                }
                return true;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (mAudioStateCallback != null) {
                    mAudioStateCallback.onAudioBuffering(false);
                }
                return true;

            default:
                return false;

        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        if (mAudioStateCallback != null) {
            mAudioStateCallback.onAudioBufferingUpdate(percent);
        }
    }

    private boolean requestAudioFocus() {
        if (mAudioManager != null) {
            int result = mAudioManager.requestAudioFocus(getAudioFocusChangeCallback(), AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return false;
    }

    private int calcProgress() {
        final int position = getPosition();
        if (mAudioProgressCallback != null) {
            if (position > mDuration) {
                mDuration = getDuration();
            }
            mAudioProgressCallback.onAudioProgress(position, mDuration);
        } else {
            //remove msg in Handler
            removeUpdateProgressMessage();
        }
        return position;
    }

    private void createMediaPlayer() {
        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 10);
            regEvent(true);
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
        mCurrentState = STATE_IDLE;
    }

    private void regEvent(boolean event) {
        mPlayer.setOnPreparedListener(event ? this : null);
        mPlayer.setOnCompletionListener(event ? this : null);
        mPlayer.setOnErrorListener(event ? this : null);
        mPlayer.setOnInfoListener(event ? this : null);
        mPlayer.setOnBufferingUpdateListener(event ? this : null);
    }

    private void destroyMediaPlayer() {
        if (mAudioManager != null && mAudioFocusChangeCallback != null) {
            mAudioManager.abandonAudioFocus(getAudioFocusChangeCallback());
        }
        if (mAudioHandler != null) {
            mAudioHandler.removeCallbacksAndMessages(null);
        }
        if (mPlayer != null) {
            mCurrentState = STATE_IDLE;
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
//        regEvent(false);
    }

    private void unBindMediaPlayerClient() {
        mAudioProgressCallback = null;
        mAudioStateCallback = null;
    }

    private boolean hasUpdateProgressMessage() {
        return mAudioHandler.hasMessages(UPDATE_PROGRESS_MSG);
    }

    private void sendUpdateProgressMessage() {
        if (!hasUpdateProgressMessage()) {
            if (mAudioHandler != null) {
                mAudioHandler.sendEmptyMessage(UPDATE_PROGRESS_MSG);
            }
        }
    }

    private void removeUpdateProgressMessage() {
        if (hasUpdateProgressMessage()) {
            if (mAudioHandler != null) {
                mAudioHandler.removeMessages(UPDATE_PROGRESS_MSG);
            }
        }
    }

    @SuppressWarnings("unused")
    private boolean isInPlaybackState() {
        return mPlayer != null
                && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE
                && mCurrentState != STATE_PREPARING;
    }

    private AudioManager.OnAudioFocusChangeListener getAudioFocusChangeCallback() {
        if (mAudioFocusChangeCallback == null) {
            mAudioFocusChangeCallback = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (!mHandPause) {
                                start();
                            }
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            pause();
                            break;

                        default:
                            break;
                    }
                }
            };
        }
        return mAudioFocusChangeCallback;
    }

//    private Map<String, String> createRequestHeaders() {
//        // change content type if necessary
//        Map<String, String> headers = new ArrayMap<>();
//        headers.put("Content-Type", "audio/*");
//        headers.put("Accept-Ranges", "bytes");
//        headers.put("Status", "206");
//        headers.put("Cache-control", "no-cache");
//        return headers;
//    }
}
