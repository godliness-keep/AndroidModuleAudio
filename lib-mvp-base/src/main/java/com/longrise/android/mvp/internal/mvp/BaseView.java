package com.longrise.android.mvp.internal.mvp;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
public interface BaseView<Data> {

    /**
     * Show start loading
     */
    void showLoadingStyle();

    /**
     * Show error loading
     */
    void showLoadingError();

    /**
     * Show loading empty
     */
    void showLoadingEmpty();

    /**
     * Dismiss loading style
     */
    void dismissLoadingStyle();
}
