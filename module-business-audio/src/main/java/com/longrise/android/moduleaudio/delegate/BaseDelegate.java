package com.longrise.android.moduleaudio.delegate;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by godliness on 2020-04-08.
 *
 * @author godliness
 */
abstract class BaseDelegate<T extends Activity> {

    private T mTarget;

    protected BaseDelegate(T target) {
        this.mTarget = target;
        initDelegate();
        regEvent(true);
    }

    protected abstract void initDelegate();

    protected abstract void regEvent(boolean isClick);

    public void release() {
        regEvent(false);
    }

    @NonNull
    protected final T getTarget() {
        return mTarget;
    }

    protected final Resources getResources() {
        return mTarget.getResources();
    }

    protected final <V extends View> V findViewById(@IdRes int id) {
        return mTarget.findViewById(id);
    }
}
