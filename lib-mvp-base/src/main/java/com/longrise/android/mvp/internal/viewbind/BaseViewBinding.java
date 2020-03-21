package com.longrise.android.mvp.internal.viewbind;

import android.os.Bundle;

import com.longrise.android.mvp.BaseViewBindActivity;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public abstract class BaseViewBinding<T> {

    private T mTarget;
    private boolean mFinished;

    public BaseViewBinding() {

    }

    public final void attachTarget(T target) {
        this.mFinished = false;
        this.mTarget = target;
        bindView(target);
        regEvent(true);
    }

    public final void detachTarget() {
        this.mFinished = false;
        regEvent(false);
        onDestroy();
    }

    protected final T getTarget() {
        return mTarget;
    }

    protected final boolean isFinish() {
        return mFinished;
    }

    /**
     * Binding the View
     *
     * @param target T
     */
    protected abstract void bindView(T target);

    /**
     * Register event
     *
     * @param regEvent Boolean
     */
    protected abstract void regEvent(boolean regEvent);

    /**
     * The target has been destroy
     */
    public abstract void onDestroy();

    /**
     * @see BaseViewBindActivity#onResume()
     */
    public void onResume() {

    }

    /**
     * @see BaseViewBindActivity#onPause()
     */
    public void onPause() {

    }

    /**
     * @see BaseViewBindActivity#onSaveInstanceState(Bundle)
     */
    public void onSaveInstanceState(Bundle outState) {

    }

    /**
     * @see BaseViewBindActivity#onRestoreInstanceState(Bundle)
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

}
