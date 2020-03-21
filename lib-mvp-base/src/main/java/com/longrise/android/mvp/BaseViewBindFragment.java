package com.longrise.android.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longrise.android.mvp.internal.mvp.BasePresenter;
import com.longrise.android.mvp.internal.viewbind.BaseViewBinding;
import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
public abstract class BaseViewBindFragment<P extends BasePresenter, ViewBinding extends BaseViewBinding> extends BaseFragment<P> {

    @Nullable
    protected ViewBinding mViewBinder;

    /**
     * Initialize the View
     */
    @SuppressWarnings("unchecked")
    @Override
    protected final void initView() {
        this.mViewBinder = GenericUtil.getT(this, 1);
        if (mViewBinder != null) {
            mViewBinder.attachTarget(this);
        }
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewBinder != null) {
            mViewBinder.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewBinder != null) {
            mViewBinder.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewBinder != null) {
            mViewBinder.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mViewBinder != null) {
            mViewBinder.onRestoreInstanceState(savedInstanceState);
        }
    }

    /**
     * Initialize the
     */
    protected abstract void init();

    @Override
    public void onDestroy() {
        if (mViewBinder != null) {
            mViewBinder.onDestroy();
        }
        super.onDestroy();
    }
}
