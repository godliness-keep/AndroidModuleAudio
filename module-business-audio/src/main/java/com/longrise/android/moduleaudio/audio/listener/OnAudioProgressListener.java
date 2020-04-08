package com.longrise.android.moduleaudio.audio.listener;

/**
 * Created by godliness on 2019/3/5.
 * From the BaoBao project
 *
 * @author godliness
 * 音频播放进度
 */

public interface OnAudioProgressListener {

    /**
     * progress of current player
     *
     * @param position current position
     * @param duration total progress
     */
    void onAudioProgress(int position, int duration);
}
