package com.longrise.android.mvp.utils;

import android.util.Log;

import com.longrise.android.mvp.BuildConfig;

/**
 * Created by godliness on 2019-07-06.
 *
 * @author godliness
 */
public final class MvpLog {

    private static Logger sLogger;

    static {
        if (BuildConfig.DEBUG) {
            sLogger = new DefaultLogger();
        }
    }

    public interface Logger {

        void d(String tag, String msg);

        void w(String tag, String msg);

        void e(String tag, String msg);

        void printer(Throwable e);

    }

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public static void d(String tag, String msg) {
        final Logger logger = sLogger;
        if (logger != null) {
            logger.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        final Logger logger = sLogger;
        if (logger != null) {
            logger.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        final Logger logger = sLogger;
        if (logger != null) {
            logger.e(tag, msg);
        }
    }

    public static void print(Throwable e) {
        final Logger logger = sLogger;
        if (logger != null) {
            logger.printer(e);
        }
    }

    private static final class DefaultLogger implements Logger {

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        @Override
        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        @Override
        public void printer(Throwable e) {
            e.printStackTrace();
        }
    }
}
