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
public final class LoadingNonNet extends BaseLoadStyle {

    private static final String TAG = "LoadingNonNet";

    private View mNonNetWork;

    public LoadingNonNet() {
        MvpLog.d(TAG, "new LoadingNonNet()");
    }

    @Override
    public void attachLoadStyle(@NonNull ViewGroup content) {
        if (mNonNetWork == null) {
            mNonNetWork = inflater(R.layout.lib_mvp_load_style_no_network_layout, content);
            mNonNetWork.setOnClickListener(this);
            final View viewReload = mNonNetWork.findViewById(R.id.view_reset_load_style);
            viewReload.setOnClickListener(this);
        }
        content.addView(mNonNetWork);
    }

    @Override
    public void detachLoadStyle() {
        if (mNonNetWork != null) {
            final ViewParent viewParent = mNonNetWork.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mNonNetWork);
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
