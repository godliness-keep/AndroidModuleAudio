package com.longrise.android.mvp.internal;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.longrise.android.mvp.internal.activitytheme.base.BaseActivityTheme;
import com.longrise.android.mvp.internal.mvp.BasePresenter;
import com.longrise.android.mvp.internal.mvp.BaseView;
import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseSuperActivity {

    public P mPresenter;
    private boolean mContentChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int layoutResourse = getLayoutResourceId(savedInstanceState);
        if (layoutResourse != BaseActivityTheme.NONE) {
            setContentView(getLayoutResourceId(savedInstanceState));
        } else {
            createAndInitMvpFrame();
        }
    }

    @Override
    public final void onContentChanged() {
        if (!mContentChanged) {
            createAndInitMvpFrame();
        }
    }

    /**
     * Returns the current layout resource id
     *
     * @param savedInstanceState Bundle state
     * @return Returns the layout resource id
     */
    @LayoutRes
    protected abstract int getLayoutResourceId(@Nullable Bundle savedInstanceState);

    /**
     * Initialize the View
     */
    protected abstract void initView();

    /**
     * Register event
     *
     * @param regEvent boolean
     */
    protected abstract void regEvent(boolean regEvent);

    /**
     * {@link BaseView#showLoadingStyle()}
     */
    public void showLoadingStyle() {
        //ignore
    }

    /**
     * {@link BaseView#showLoadingError()}
     */
    public void showLoadingError() {
        //ignore
    }

    /**
     * {@link BaseView#showLoadingEmpty()}
     */
    public void showLoadingEmpty() {
        //ignore
    }

    /**
     * {@link BaseView#dismissLoadingStyle()}
     */
    public void dismissLoadingStyle() {
        //ignore
    }

    @Override
    public void finish() {
        if (mPresenter != null) {
            mPresenter.notifyFinish();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachTarget();
            mPresenter = null;
        }
        regEvent(false);
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    void createAndInitMvpFrame() {
        mContentChanged = true;
        if (this instanceof BaseView) {
            mPresenter = GenericUtil.getT(this, 0);
        }
        if (mPresenter != null) {
            mPresenter.attachV(this);
        }
        initView();
        if (mPresenter != null) {
            mPresenter.init();
        }
        regEvent(true);
    }
}
