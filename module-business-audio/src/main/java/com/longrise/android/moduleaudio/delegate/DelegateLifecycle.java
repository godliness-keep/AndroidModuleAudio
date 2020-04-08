package com.longrise.android.moduleaudio.delegate;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by godliness on 2020-04-07.
 *
 * @author godliness
 */
abstract class DelegateLifecycle extends BaseDelegate implements Application.ActivityLifecycleCallbacks {

    private final Application mApp;
    private boolean mRemovedLifecycle;

    DelegateLifecycle(Activity target) {
        super(target);
        final Application app = target.getApplication();
        app.registerActivityLifecycleCallbacks(this);
        this.mApp = app;
    }

    protected abstract void onAudioTargetIntoResumed();

    protected abstract void onAudioTargetIntoPaused();

    protected abstract void onAudioTargetIntoFinish();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isAudioTarget(activity)) {
            onAudioTargetIntoResumed();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (isAudioTarget(activity)) {
            if (activity.isFinishing()) {
                onAudioTargetIntoFinish();
                unregisterActivityLifecycleCallbacks();
            } else {
                onAudioTargetIntoPaused();
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (!mRemovedLifecycle && isAudioTarget(activity)) {
            unregisterActivityLifecycleCallbacks();
        }
        release();
    }

    private boolean isAudioTarget(Activity current) {
        return getActivity() == current;
    }

    private void unregisterActivityLifecycleCallbacks() {
        if (mApp != null) {
            mApp.unregisterActivityLifecycleCallbacks(this);
            mRemovedLifecycle = true;
        }
    }
}
