package com.longrise.android.mvp.internal;

import java.util.LinkedList;

/**
 * Created by godliness on 2019-07-04.
 *
 * @author godliness
 */
public final class LazyLoad {

    private static final int MAX_CACHE_SIZE = 3;
    private static final LinkedList<LazyLoad> LAZY_LOADS = new LinkedList<>();

    public boolean mViewCreated;
    public boolean mUserVisibled;
    public boolean mLoaded;

    public static LazyLoad obtain() {
        if (LAZY_LOADS.size() > 0) {
            return LAZY_LOADS.removeFirst();
        }
        return new LazyLoad();
    }

    public void recycleSelf() {
        if (LAZY_LOADS.size() < MAX_CACHE_SIZE) {
            LAZY_LOADS.add(this);
            init();
        }
    }

    @Override
    public String toString() {
        return "LazyLoad{" +
                "mViewCreated=" + mViewCreated +
                ", mUserVisibled=" + mUserVisibled +
                ", mLoaded=" + mLoaded +
                '}';
    }

    private void init() {
        this.mViewCreated = false;
        this.mUserVisibled = false;
        this.mLoaded = false;
    }
}
