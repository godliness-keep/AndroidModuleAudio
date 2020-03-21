package com.longrise.android.mvp.internal.loadstyle.base;

import android.widget.FrameLayout;

/**
 * Created by godliness on 2019-07-04.
 *
 * @author godliness
 */
public interface ILoadStyleListener {

    /**
     * Retry after loading error
     */
    void onReload();

    /**
     * Returns the View for which the loading style needs to be added
     *
     * @return FrameLayout
     */
    FrameLayout returnStyleContent();

}
