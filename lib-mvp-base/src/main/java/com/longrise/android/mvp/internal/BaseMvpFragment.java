package com.longrise.android.mvp.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.longrise.android.mvp.internal.mvp.BasePresenter;
import com.longrise.android.mvp.internal.mvp.BaseView;
import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public class BaseMvpFragment<P extends BasePresenter> extends Fragment {

    public P mPresenter;
    private boolean mDestroy;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof BaseView) {
            mPresenter = GenericUtil.getT(this, 0);
        }
        if (mPresenter != null) {
            mPresenter.attachV(this);
        }
        mDestroy = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.init();
        }
    }

    /**
     * {@link Activity#isFinishing()}
     */
    protected final boolean isFinishing() {
        final Activity target = getActivity();
        if (target != null) {
            return target.isFinishing();
        }
        return mDestroy;
    }

    /**
     * Fragment lifecycle is destroy
     */
    protected final boolean isDestroy() {
        return mDestroy;
    }

    /**
     * Start loading {@link BaseView#showLoadingStyle()}
     */
    public void showLoadingStyle() {
        //ignore
    }

    /**
     * Error loading {@link BaseView#showLoadingError()}
     */
    public void showLoadingError() {
        //ignore
    }

    /**
     * Loading is empty {@link BaseView#showLoadingEmpty()}
     */
    public void showLoadingEmpty() {
        //ignore
    }

    /**
     * loaded {@link BaseView#dismissLoadingStyle()}
     */
    public void dismissLoadingStyle() {
        //ignore
    }

    @Override
    public void onStop() {
        final Activity target = getActivity();
        if (target != null && target.isFinishing()) {
            notifyPresenterFinish();
        }
        super.onStop();

    }

    @Override
    public void onDestroy() {
        notifyPresenterFinish();
        if (mPresenter != null) {
            mPresenter.detachTarget();
        }
        mDestroy = true;
        super.onDestroy();
    }

    private void notifyPresenterFinish() {
        if (!mDestroy && mPresenter != null) {
            mPresenter.notifyFinish();
        }
    }
}
