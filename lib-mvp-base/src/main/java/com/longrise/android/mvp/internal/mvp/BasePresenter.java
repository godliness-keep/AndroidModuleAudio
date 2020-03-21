package com.longrise.android.mvp.internal.mvp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.longrise.android.mvp.internal.BaseMvpActivity;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public abstract class BasePresenter<V> {

    private static final String TAG = "BasePresenter";

    @Nullable
    protected Context mContext;
    @Nullable
    protected V mView;

    private boolean mFinished;

    /**
     * Presenter initialization is complete
     * This method is called after {@link BaseMvpActivity#initView()}
     */
    public abstract void init();

    /**
     * Presenter has destroy
     */
    protected abstract void destroy();

    public final void attachV(V v) {
        this.mView = v;
        if (v instanceof Activity) {
            this.mContext = (Context) v;
        }
    }

    public final void detachTarget() {
        this.mContext = null;
        this.mView = null;
        destroy();
    }

    public final void notifyFinish() {
        this.mFinished = true;
    }

    /**
     * {@link Activity#isFinishing()}
     */
    protected final boolean isFinish() {
        return mFinished;
    }

    /**
     * {@link com.longrise.android.mvp.BaseActivity#showLoadingStyle()}
     */
    public final void showLoadingStyle() {
        if (mView != null) {
            ((BaseView) mView).showLoadingStyle();
        }
    }

    /**
     * {@link com.longrise.android.mvp.BaseActivity#showLoadingError()}
     */
    public final void showLoadingError() {
        if (mView != null) {
            ((BaseView) mView).showLoadingError();
        }
    }

    /**
     * {@link com.longrise.android.mvp.BaseActivity#showLoadingEmpty()}
     */
    public final void showLoadingEmpty() {
        if (mView != null) {
            ((BaseView) mView).showLoadingEmpty();
        }
    }

    /**
     * {@link com.longrise.android.mvp.BaseActivity#dismissLoadingStyle()}
     */
    public final void dimissLoadingStyle() {
        if (mView != null) {
            ((BaseView) mView).dismissLoadingStyle();
        }
    }

}
