package com.godliness.android.moduleaudiodemo.util;

import android.content.Context;

/**
 * Created by godliness on 2020-03-19.
 *
 * @author godliness
 */
public final class AppContext {

    private static Context sContext;

    public static Context get() {
        return sContext;
    }

    public static void register(Context context) {
        sContext = context;
    }
}
