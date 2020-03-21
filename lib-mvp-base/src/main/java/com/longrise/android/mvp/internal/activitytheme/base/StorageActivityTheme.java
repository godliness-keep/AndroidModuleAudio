package com.longrise.android.mvp.internal.activitytheme.base;

import android.content.ComponentCallbacks2;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.longrise.android.mvp.utils.MvpLog;

import java.util.LinkedList;

/**
 * Created by godliness on 2019-07-06.
 *
 * @author godliness
 */
final class StorageActivityTheme {

    private static final int MAX_CACHE_THEME_SIZE = 3;
    private static final ArrayMap<Class<? extends BaseActivityTheme>, LinkedList<BaseActivityTheme>> THEMES = new ArrayMap<>();

    static void trimMemory(int level) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Can only be called from the main thread");
        }
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            THEMES.clear();
            return;
        }
        final int size = THEMES.size();
        for (int i = 0; i < size; i++) {
            final LinkedList<BaseActivityTheme> activityThemes = THEMES.valueAt(i);
            final int themeSize = activityThemes.size();
            if (themeSize > 1) {
                final BaseActivityTheme keep = activityThemes.removeLast();
                activityThemes.clear();
                activityThemes.add(keep);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    static <T extends BaseActivityTheme> T findTheme(Class<T> themeClass) {
        final LinkedList<BaseActivityTheme> activityThemes = THEMES.get(themeClass);
        if (activityThemes != null && activityThemes.size() > 0) {
            return (T) activityThemes.removeFirst();
        }
        try {
            return themeClass.newInstance();
        } catch (IllegalAccessException e) {
            MvpLog.print(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean recycleActivityTheme(BaseActivityTheme activityTheme) {
        final int size = THEMES.size();
        if (size >= MAX_CACHE_THEME_SIZE) {
            THEMES.clear();
        }

        final Class<? extends BaseActivityTheme> activityThemeClass = activityTheme.getClass();
        LinkedList<BaseActivityTheme> themes = THEMES.get(activityThemeClass);
        if (themes == null) {
            THEMES.put(activityThemeClass, themes = new LinkedList<>());
        }
        if (themes.size() < MAX_CACHE_THEME_SIZE) {
            return themes.add(activityTheme);
        }
        return false;
    }
}
