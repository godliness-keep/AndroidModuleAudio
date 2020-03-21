package com.longrise.android.mvp.internal.loadstyle.base;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

/**
 * Created by godliness on 2019-07-05.
 *
 * @author godliness
 */
final class StorageLoadStyle {

    @SuppressWarnings("unchecked")
    private static final ArrayMap<Class<BaseLoadStyle>, BaseLoadStyle> LOAD_STYLES = new ArrayMap();

    @Nullable
    @SuppressWarnings("unchecked")
    static <LoadStyle extends BaseLoadStyle> LoadStyle obtainLoadStyle(Class<LoadStyle> loadStyleClass) {
        final LoadStyle loadingStyle = (LoadStyle) LOAD_STYLES.remove(loadStyleClass);
        if (loadingStyle != null) {
            return loadingStyle;
        }
        try {
            return loadStyleClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static void recycleLoadStyle(BaseLoadStyle loadStyle) {
        LOAD_STYLES.put((Class<BaseLoadStyle>) loadStyle.getClass(), loadStyle);
    }

}
