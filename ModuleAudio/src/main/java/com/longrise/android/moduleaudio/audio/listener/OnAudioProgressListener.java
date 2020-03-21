package com.longrise.android.moduleaudio.audio.listener;

/**
 * Created by godliness on 2019/3/5.
 * From the BaoBao project
 *
 * @author godliness
 *         音频播放进度
 */

public interface OnAudioProgressListener {

    /**
     * 播放进度
     *
     * @param position 播放进度
     * @param duration 总时长
     */
    void onAudioProgress(int position, int duration);
}
