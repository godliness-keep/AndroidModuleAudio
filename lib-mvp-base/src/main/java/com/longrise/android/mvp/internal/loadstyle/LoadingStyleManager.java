package com.longrise.android.mvp.internal.loadstyle;


import android.view.ViewGroup;

import com.longrise.android.mvp.internal.loadstyle.base.BaseLoadStyle;
import com.longrise.android.mvp.internal.loadstyle.base.ILoadStyleListener;

/**
 * Created by godliness on 2019-07-04.
 *
 * @author godliness
 */
@SuppressWarnings("unchecked")
public final class LoadingStyleManager {

    /**
     * Loading style start
     */
    public static <T extends ILoadStyleListener> void loadingStart(T target) {
        final BaseLoadStyle loadStyle = BaseLoadStyle.obtainLoadStyle(LoadingStart.class);
        if (loadStyle != null) {
            loadStyle.bindLoadStyle(target);
        }
    }

    /**
     * Loading style error
     */
    public static <T extends ILoadStyleListener> void loadingError(T target) {
        final BaseLoadStyle loadStyle = BaseLoadStyle.obtainLoadStyle(LoadingError.class);
        if (loadStyle != null) {
            loadStyle.bindLoadStyle(target);
        }
    }

    /**
     * Loading style empty
     */
    public static <T extends ILoadStyleListener> void loadingEmpty(T target) {
        final BaseLoadStyle loadStyle = BaseLoadStyle.obtainLoadStyle(LoadingEmpty.class);
        if (loadStyle != null) {
            loadStyle.bindLoadStyle(target);
        }
    }

    /**
     * Loading style Non Network
     */
    public static <T extends ILoadStyleListener> void loadingNonNet(T target) {
        final BaseLoadStyle loadStyle = BaseLoadStyle.obtainLoadStyle(LoadingNonNet.class);
        if (loadStyle != null) {
            loadStyle.bindLoadStyle(target);
        }
    }

    /**
     * Loading style normal
     */
    public static <T extends ILoadStyleListener> void dismissLoadingStyle(T target) {
        BaseLoadStyle.recycleLoadStyle(target);
    }

    /**
     * Loading style destroy
     */
    public static <T extends ILoadStyleListener> void onDestroy(T target) {
        BaseLoadStyle.recycleLoadStyle(target);
    }

}
