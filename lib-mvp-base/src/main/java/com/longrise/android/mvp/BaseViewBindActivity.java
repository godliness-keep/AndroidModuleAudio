package com.longrise.android.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.longrise.android.mvp.internal.mvp.BasePresenter;
import com.longrise.android.mvp.internal.viewbind.BaseViewBinding;
import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
public abstract class BaseViewBindActivity<P extends BasePresenter, ViewBind extends BaseViewBinding> extends BaseActivity<P> {

    @Nullable
    protected ViewBind mViewBind;

    @SuppressWarnings("unchecked")
    @Override
    protected final void initView() {
        mViewBind = GenericUtil.getT(this, 1);
        if (mViewBind != null) {
            mViewBind.attachTarget(this);
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewBind != null) {
            mViewBind.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewBind != null) {
            mViewBind.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewBind != null) {
            mViewBind.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mViewBind != null) {
            mViewBind.onRestoreInstanceState(savedInstanceState);
        }
    }

    /**
     * Initialize the
     */
    protected abstract void init();

    @Override
    protected void onDestroy() {
        if (mViewBind != null) {
            mViewBind.detachTarget();
        }
        super.onDestroy();
    }
}
