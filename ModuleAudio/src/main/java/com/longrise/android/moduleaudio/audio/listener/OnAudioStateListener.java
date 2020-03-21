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
     * 是否正在播放
     *
     * @param isPlaying true 正在播放
     */
    void onAudioState(boolean isPlaying);

    /**
     * 初始化完成
     */
    void onAudioPrepared(IMediaPlayer mp);

    /**
     * 播放完成
     */
    void onAudioCompletion(IMediaPlayer mp);

    /**
     * 播放出错
     */
    boolean onAudioError(IMediaPlayer mp, int what, int extra);

    /**
     * 缓冲状态
     *
     * @param buffering true 正在缓冲
     */
    void onAudioBuffering(boolean buffering);

    /**
     * 缓冲进度
     *
     * @param percent 缓冲进度
     */
    void onAudioBufferingUpdate(int percent);
}
