package com.longrise.android.moduleaudio.audio.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2019/3/5.
 * From the BaoBao project
 *
 * @author godliness
 * 播放状态监听
 */

public interface OnAudioStateListener {

    /**
     * Play state of current player
     */
    void onAudioState(boolean isPlaying);

    /**
     * Prepared of current player
     */
    void onAudioPrepared(IMediaPlayer mp);

    /**
     * Completion of current player
     */
    void onAudioCompletion(IMediaPlayer mp);

    /**
     * Error of current player
     */
    boolean onAudioError(IMediaPlayer mp, int what, int extra);

    /**
     * Buffering of current player
     */
    void onAudioBuffering(boolean buffering);

    /**
     * Buffering progress of current player
     */
    void onAudioBufferingUpdate(int percent);
}
