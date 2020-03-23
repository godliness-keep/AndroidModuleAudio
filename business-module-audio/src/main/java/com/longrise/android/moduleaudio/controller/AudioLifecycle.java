package com.longrise.android.moduleaudio.controller;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.longrise.android.moduleaudio.audio.listener.OnAudioServiceStateListener;

/**
 * Created by godliness on 2020-03-19.
 *
 * @author godliness
 */
abstract class AudioLifecycle implements Application.ActivityLifecycleCallbacks, OnAudioServiceStateListener {

    private final Application mApp;

    public AudioLifecycle(Application app) {
        this.mApp = app;
        app.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onAudioServiceDestroy() {
        if (mApp != null) {
            mApp.unregisterActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
