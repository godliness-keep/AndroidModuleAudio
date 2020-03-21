package com.longrise.android.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.longrise.android.mvp.internal.BaseMvpFragment;
import com.longrise.android.mvp.internal.LazyLoad;
import com.longrise.android.mvp.internal.loadstyle.LoadingStyleManager;
import com.longrise.android.mvp.internal.loadstyle.base.ILoadStyleListener;
import com.longrise.android.mvp.internal.mvp.BasePresenter;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
@SuppressWarnings("unused")
public abstract class BaseFragment<P extends BasePresenter> extends BaseMvpFragment<P> implements ILoadStyleListener {

    private Context mContext;
    private final LazyLoad mLazyLoad = LazyLoad.obtain();
    private View mLoadContent;

    private boolean mPerformUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(getLayoutResourceId(container, savedInstanceState), container, false);
        if (rootView instanceof FrameLayout) {
            return mLoadContent = rootView;
        }
        return generatorLoadStyleContent(rootView);
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        regEvent(true);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPerformUser) {
            this.mLazyLoad.mViewCreated = true;
            performLoad();
        } else {
            performLazyLoad();
        }
    }

    public final <V extends View> V findViewById(int resId) {
        return getView().findViewById(resId);
    }

    @NonNull
    @Override
    public View getView() {
        return mLoadContent;
    }

    @Override
    public FrameLayout returnStyleContent() {
        return (FrameLayout) mLoadContent;
    }

    /**
     * Returns the current layout resource id
     *
     * @param container          ViewGroup
     * @param savedInstanceState Bundle state
     * @return Returns the layout resource id
     */
    protected abstract int getLayoutResourceId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * Initialize the View
     */
    protected abstract void initView();

    /**
     * Perform lazy loading
     */
    protected abstract void performLazyLoad();

    /**
     * Register event
     *
     * @param regEvent Boolean
     */
    protected abstract void regEvent(boolean regEvent);

    /**
     * Fragment returns to focus and then executes {@link #performLazyLoad()}
     */
    protected final void resetLazyLoad() {
        mLazyLoad.mLoaded = false;
    }

    @Override
    public final void showLoadingStyle() {
        LoadingStyleManager.loadingStart(this);
    }

    @Override
    public final void showLoadingEmpty() {
        LoadingStyleManager.loadingEmpty(this);
    }

    @Override
    public final void showLoadingError() {
        LoadingStyleManager.loadingError(this);
    }

    @Override
    public final void dismissLoadingStyle() {
        LoadingStyleManager.dismissLoadingStyle(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mPerformUser = true;
        this.mLazyLoad.mUserVisibled = isVisibleToUser;
        performLoad();
    }

    @Override
    public void onDestroy() {
        regEvent(false);
        mLazyLoad.recycleSelf();
        LoadingStyleManager.onDestroy(this);
        super.onDestroy();
    }

    private void performLoad() {
        final LazyLoad lazyLoad = this.mLazyLoad;
        if (lazyLoad.mViewCreated && lazyLoad.mUserVisibled && !lazyLoad.mLoaded) {
            performLazyLoad();
            lazyLoad.mLoaded = true;
        }
    }

    private View generatorLoadStyleContent(View onCreateView) {
        this.mLoadContent = getContainerView();
        if (mLoadContent instanceof FrameLayout) {
            return onCreateView;
        }
        final FrameLayout container = new FrameLayout(mContext);
        container.addView(onCreateView);
        return mLoadContent = container;
    }

    private View getContainerView() {
        final Activity target = getActivity();
        if (target != null) {
            return target.findViewById(getId());
        }
        return null;
    }
}
