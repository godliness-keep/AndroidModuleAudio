package com.longrise.android.mvp.internal.mvp;

import android.support.annotation.NonNull;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
public interface BaseModel {

    /**
     * The Model is initialized，Callback in the {@link BasePresenterEx#init()} after
     */
    void init();

    /**
     * Default data loading
     *
     * @param loadingListener {@link ILoadingListener}
     */
    void loadData(@NonNull ILoadingListener loadingListener);

    @SuppressWarnings("unchecked")
    interface ILoadingListener<P> {

        /**
         * Data load completed
         */
        void onCompleted(P params);

        /**
         * Data load failed
         *
         * @param desc failed desc
         */
        void onFailed(String desc);
    }
}
