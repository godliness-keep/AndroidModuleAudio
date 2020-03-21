package com.longrise.android.mvp.internal.loadstyle;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.longrise.android.mvp.R;
import com.longrise.android.mvp.internal.loadstyle.base.BaseLoadStyle;
import com.longrise.android.mvp.utils.MvpLog;

/**
 * Created by godliness on 2019-07-05.
 *
 * @author godliness
 */
public final class LoadingStart extends BaseLoadStyle {

    private static final String TAG = "LoadingStart";

    private View mLoadView;

    public LoadingStart() {
        MvpLog.d(TAG, "new LoadingStart()");
    }

    @Override
    public void attachLoadStyle(@NonNull ViewGroup content) {
        if (mLoadView == null) {
            mLoadView = inflater(R.layout.lib_mvp_load_style_start_layout, content);
        }
        content.addView(mLoadView);
    }

    @Override
    public void detachLoadStyle() {
        if (mLoadView != null) {
            final ViewParent viewParent = mLoadView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mLoadView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //ignore
    }
}
