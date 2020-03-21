package com.longrise.android.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import com.longrise.android.mvp.internal.mvp.BasePresenter;
import com.longrise.android.mvp.internal.mvp.BaseView;
import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseDialog<P extends BasePresenter> extends AppCompatDialog {

    protected P mPresenter;
    private Context mContext;
    private View mDecor;

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private boolean mContentChanged;

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        if (window != null) {
            beforeSetContentView(window);
            this.mDecor = window.getDecorView();
        }
        setContentView(getLayoutResourceId(savedInstanceState));
    }

    @Override
    public final void onContentChanged() {
        if (!mContentChanged) {
            initMvpFrame();
            mContentChanged = true;
        }
    }

    /**
     * Returns the current layout resource id
     *
     * @param savedInstanceState Bundle state
     * @return Returns the layout resource id
     */
    protected abstract int getLayoutResourceId(Bundle savedInstanceState);

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

    protected Context getActivity(){
        return mContext;
    }

    protected void beforeSetContentView(@NonNull Window window) {

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
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            if (DEBUG) {
                throw e;
            }
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            if (DEBUG) {
                throw e;
            }
        }
        if (mPresenter != null) {
            mPresenter.detachTarget();
        }
        regEvent(false);
        if (mDecor != null) {
            removeTreeView();
        }
    }

    @SuppressWarnings("unchecked")
    private void initMvpFrame() {
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

    private void removeTreeView() {
        final ViewParent viewParent = mDecor.getParent();
        if (viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(mDecor);
        }
    }
}
