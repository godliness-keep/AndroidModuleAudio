package com.longrise.android.mvp.internal.activitytheme.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.longrise.android.mvp.internal.BaseMvpActivity;

/**
 * Created by godliness on 2019-07-02.
 *
 * @author godliness
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseActivityTheme<T extends BaseMvpActivity> {

    private static final String TAG = "BaseActivityTheme";
    public static final int NONE = 0;

    private boolean mCached;

    public BaseActivityTheme() {

    }

    @Nullable
    public static <T extends BaseActivityTheme> T findTheme(Class<T> themeClass) {
        return StorageActivityTheme.findTheme(themeClass);
    }

    public final void bindThemeView(@NonNull T target, @LayoutRes int layoutId) {
        final View decorView = target.getWindow().getDecorView();
        bindThemeView(target, LayoutInflater.from(target).inflate(layoutId, (ViewGroup) decorView, false));
    }

    public final void bindThemeView(@NonNull T target, View view) {
        final FrameLayout themeContent = getThemeContent();
        themeContent.addView(view);
        //It has to be at the end
        //Response onContentChanged
        target.setContentView(getThemeView());
    }

    public final void attachTheme(T target) {
        if (!mCached) {
            final View decorView = target.getWindow().getDecorView();
            createThemeLayout(LayoutInflater.from(target.getApplicationContext()), (ViewGroup) decorView);
        }
        bindTarget(target);
    }

    public final void detachTheme() {
        final View themeView = getThemeView();
        final ViewParent viewParent = themeView.getParent();
        if (viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(themeView);
        }
        final FrameLayout content = getThemeContent();
        content.removeAllViews();
        if (hasRecycle()) {
            mCached = StorageActivityTheme.recycleActivityTheme(this);
            if (mCached) {
                recycled();
            }
        }
        unBindTarget();
    }

    /**
     * bind to the target
     *
     * @param target T extends BaseMvpActivity
     */
    protected abstract void bindTarget(T target);

    /**
     * unbind to the target,Beware of memory leaks
     */
    protected abstract void unBindTarget();

    /**
     * Create the Theme layout
     *
     * @param inflater  LayoutInflater
     * @param container Theme view in container
     */
    protected abstract void createThemeLayout(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

    /**
     * Returns the current Theme view
     * Just return your Theme View and do nothing else
     *
     * @return View
     */
    @NonNull
    protected abstract View getThemeView();

    /**
     * Returns the current view container
     * Just return your Theme Content View and do nothing else
     *
     * @return Theme Content
     */
    @NonNull
    public abstract FrameLayout getThemeContent();

    /**
     * Whether you need reuse
     * If true, Call back when destroy {@link #recycled()}
     *
     * @return Boolean
     */
    protected abstract boolean hasRecycle();

    /**
     * Callback after being recycled {@link #hasRecycle()}
     */
    protected void recycled() {

    }

}
