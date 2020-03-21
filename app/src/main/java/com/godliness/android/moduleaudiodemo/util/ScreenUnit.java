package com.godliness.android.moduleaudiodemo.util;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * Created by sujizhong on 15/12/11.
 * 屏幕尺寸相关工具
 */
public class ScreenUnit {

    /**
     * 根据手机的分辨率from dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return dip2px(AppContext.get(), dpValue);
    }

    /**
     * 根据手机分辨率from px转成dp
     */
    public static int px2dip(float pxValue) {
        return px2dip(AppContext.get(), pxValue);
    }

    /**
     * 获取宽度px
     */
    public static int getWidth() {
        return getWidth(AppContext.get().getApplicationContext());
    }

    /**
     * 获取高度px
     */
    public static int getHeight() {
        return getHeight(AppContext.get().getApplicationContext());
    }

    /**
     * px转换成sp
     */
    public static int px2sp(float pxValue) {
        return px2sp(AppContext.get(), pxValue);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(float spValue) {
        return sp2px(AppContext.get(), spValue);
    }

    /**
     * 获取屏幕相关信息
     */
    public static ArrayMap<String, Integer> getDensity() {
        return getDensity(AppContext.get());
    }

    /**
     * 根据手机的分辨率from dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 屏幕宽度
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 屏幕高度
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕的     像素密度dpi，高度px 宽度px
     */
    public static ArrayMap<String, Integer> getDensity(Context cxt) {
        DisplayMetrics displayMetrics = cxt.getApplicationContext().getResources().getDisplayMetrics();
        StringBuilder builder = new StringBuilder();
        ArrayMap<String, Integer> map = new ArrayMap<>(4);
        map.put(DensityConts.DENSITY, (int) displayMetrics.density);
        map.put(DensityConts.DENSITY_DPI, displayMetrics.densityDpi);
        map.put(DensityConts.HEIGHT_PIXELS, displayMetrics.heightPixels);
        map.put(DensityConts.WIDTH_PIXELS, displayMetrics.widthPixels);
        return map;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getStatusBarHeight() {
        return getStatusBarHeight(AppContext.get());
    }

    public static int getStatusBarHeight(Context cxt) {
        int statusBarHeight = -1;
        int resourceId = cxt.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = AppContext.get().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static class DensityConts {
        public static final String DENSITY = "density";
        public static final String DENSITY_DPI = "density_dpi";
        public static final String HEIGHT_PIXELS = "height_pixels";
        public static final String WIDTH_PIXELS = "width_pixels";
    }
}
