package com.longrise.android.mvp.utils;

import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;

/**
 * Created by godliness on 2019-07-02.
 *
 * @author godliness
 */
public final class GenericUtil {

    private static final String TAG = "GenericUtil";

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getT(Object o, int i) {
        long start = SystemClock.uptimeMillis();
        final Object obj = o.getClass().getGenericSuperclass();
        if (obj instanceof ParameterizedType) {
            try {
                final Class<T> clz = (Class<T>) ((ParameterizedType) obj).getActualTypeArguments()[i];
                MvpLog.e(TAG, "delay: " + (SystemClock.uptimeMillis() - start));
                return clz.newInstance();
            } catch (Exception e) {
                MvpLog.print(e);
            }
        }
        return null;
    }
}
