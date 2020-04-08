package com.godliness.android.moduleaudiodemo.demo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.SeekBar;

import com.longrise.android.moduleaudio.audio.service.BackgroundAudioOption;
import com.longrise.android.moduleaudio.delegate.BaseAudioDelegate;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by godliness on 2020-04-07.
 *
 * @author godliness
 */
public final class AudioDelegate extends BaseAudioDelegate<AudioDemoActivity> {

    private static final String TAG = "AudioDelegate";

    private final AudioParams mParams = new AudioParams();

    protected AudioDelegate(Activity target) {
        super(target);
    }

    @Override
    protected void regEvent(boolean isClick) {

    }

    @Override
    protected boolean canIntoBackground() {
        return true;
    }

    @Override
    protected void onConfigurationForBackground(@NonNull BackgroundAudioOption audioOption) {
        audioOption.controller(AudioController.class);
        audioOption.params(mParams);
    }

    @Override
    protected void updateAudioProgress(String stringForPosition, String duration, int position) {
        Log.e(TAG, "updateAudioProgress: " + position);
        getTarget().updateAudioProgress(stringForPosition, duration, position);
    }

    @Override
    protected void bindSeekBarChangeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        getTarget().bindSeekbarChangeListener(seekBarChangeListener);
    }

    @Override
    public void onAudioState(boolean isPlaying) {
        getTarget().updateAudioState(isPlaying);
    }

    @Override
    public void onAudioPrepared(IMediaPlayer mp) {

    }

    @Override
    public void onAudioCompletion(IMediaPlayer mp) {

    }

    @Override
    public boolean onAudioError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioBuffering(boolean buffering) {

    }

    @Override
    public void onAudioBufferingUpdate(int percent) {

    }
}
