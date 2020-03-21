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
public final class LoadingError extends BaseLoadStyle {

    private static final String TAG = "LoadingError";

    private View mErrorView;

    public LoadingError() {
        MvpLog.d(TAG, "new LoadingError()");
    }

    @Override
    public void attachLoadStyle(@NonNull ViewGroup content) {
        if (mErrorView == null) {
            mErrorView = inflater(R.layout.lib_mvp_load_style_error_layout, content);
            mErrorView.setOnClickListener(this);
            final View viewReload = mErrorView.findViewById(R.id.view_reset_load_style);
            viewReload.setOnClickListener(this);
        }
        content.addView(mErrorView);
    }

    @Override
    public void detachLoadStyle() {
        if (mErrorView != null) {
            final ViewParent viewParent = mErrorView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mErrorView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_reset_load_style) {
            callReload();
        }
    }
}
