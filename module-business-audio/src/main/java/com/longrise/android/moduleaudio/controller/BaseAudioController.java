package com.longrise.android.moduleaudio.controller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.longrise.android.moduleaudio.audio.listener.OnAudioProgressListener;
import com.longrise.android.moduleaudio.audio.listener.OnAudioStateListener;
import com.longrise.android.moduleaudio.audio.service.AudioBridge;
import com.longrise.android.moduleaudio.params.BaseParams;

/**
 * Created by godliness on 2020-03-18.
 *
 * @author godliness
 */
@SuppressWarnings({"unused, unchecked"})
public abstract class BaseAudioController extends AudioLifecycle implements OnAudioProgressListener, OnAudioStateListener {

    private static final String TAG = "BaseAudioController";

    private final LayoutInflater mInflater;
    private View mControllerView;
    private AudioBridge mBridge;

    private int mLastHost;

    public BaseAudioController(Context cxt) {
        super((Application) cxt.getApplicationContext());
        this.mInflater = LayoutInflater.from(cxt);
    }

    /**
     * 返回Controller控制器布局资源id
     *
     * @return Controller layout resource id
     */
    protected abstract int getControllerLayoutId();

    /**
     * 初始化控制器View {@link #findViewById(int)}
     */
    protected abstract void initControllerView();

    /**
     * 注册相关事件
     *
     * @param event register/unregister
     */
    protected abstract void regEvent(boolean event);

    /**
     * 绑定更多参数
     *
     * @param params {@link BaseParams}
     */
    protected <T extends BaseParams> void bindAudioParams(T params) {
    }

    /**
     * 是否需要显示控制器View，开发人员根据业务需求定制逻辑
     *
     * @return true 表示显示
     */
    protected boolean showController(Activity host) {
        return true;
    }

    /**
     * 控制器生命周期结束
     */
    protected abstract void onAudioDestroy();

    @Override
    public final void onActivityResumed(Activity host) {
        if (!showController(host)) {
            return;
        }

        final int hash = host.hashCode();
        if (hash == mLastHost) {
            return;
        }
        mLastHost = hash;
        attachToResumedActivity(host);
    }

    @Override
    public final void onAudioServiceDestroy() {
        super.onAudioServiceDestroy();
        ControllerFactory.removeController(this);
        connectToAudioService(false);
        removeSelf();
        regEvent(false);
        onAudioDestroy();
        mBridge = null;
    }

    public final <T extends BaseParams> void bindAudioBridge(AudioBridge bridge, T params) {
        this.mBridge = bridge;
        connectToAudioService(true);
        bindAudioParams(params);
    }

    @Nullable
    protected final AudioBridge getAudioBridge() {
        return mBridge;
    }

    protected final <T extends View> T getControllerView() {
        return (T) mControllerView;
    }

    protected final View inflater(@LayoutRes int layoutId, ViewGroup parent, boolean attachToRoot) {
        return mInflater.inflate(layoutId, parent, attachToRoot);
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mControllerView.findViewById(id);
    }

    protected final LayoutInflater getInflater() {
        return mInflater;
    }

    private void connectToAudioService(boolean connect) {
        if (mBridge != null) {
            mBridge.setAudioServiceStateListener(connect ? this : null);
            mBridge.setAudioProgressListener(connect ? this : null);
            mBridge.setAudioStateListener(connect ? this : null);
        }
    }

    private void inflaterController(ViewGroup parent) {
        if (mControllerView == null) {
            mControllerView = inflater(getControllerLayoutId(), parent, false);
            initControllerView();
            regEvent(true);
        } else {
            removeSelf();
        }
    }

    private void removeSelf() {
        if (mControllerView != null) {
            ViewParent viewParent = mControllerView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mControllerView);
            }
        }
    }

    private void attachToResumedActivity(Activity host) {
        final ViewGroup parent = host.findViewById(android.R.id.content);
        inflaterController(parent);
        parent.addView(mControllerView);
    }
}
