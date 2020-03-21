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
public final class LoadingEmpty extends BaseLoadStyle {

    private static final String TAG = "LoadingEmpty";

    private View mEmptyView;

    public LoadingEmpty() {
        MvpLog.d(TAG, "new LoadingEmpty()");
    }

    @Override
    public void attachLoadStyle(@NonNull ViewGroup content) {
        if (mEmptyView == null) {
            mEmptyView = inflater(R.layout.lib_mvp_load_style_empty_layout, content);
            mEmptyView.setOnClickListener(this);
        }
        content.addView(mEmptyView);
    }

    @Override
    protected void detachLoadStyle() {
        if (mEmptyView != null) {
            final ViewParent viewParent = mEmptyView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mEmptyView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //ignore
    }
}
