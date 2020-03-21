package com.longrise.android.mvp;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.longrise.android.mvp.internal.BaseMvpActivity;
import com.longrise.android.mvp.internal.activitytheme.ActivityTheme;
import com.longrise.android.mvp.internal.activitytheme.base.BaseActivityTheme;
import com.longrise.android.mvp.internal.activitytheme.base.IActivityThemeListener;
import com.longrise.android.mvp.internal.loadstyle.LoadingStyleManager;
import com.longrise.android.mvp.internal.loadstyle.base.ILoadStyleListener;
import com.longrise.android.mvp.internal.mvp.BasePresenter;

/**
 * Created by godliness on 2019-07-02.
 *
 * @author godliness
 * NOTE:Provides a standard style of Activity, If not need, please @see BaseMvpActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends BaseMvpActivity<P> implements IActivityThemeListener, ILoadStyleListener {

    @Nullable
    private ActivityTheme mTheme;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        createActivityTheme();
        super.onCreate(savedInstanceState);
    }

    @Override
    public final void setContentView(int layoutResID) {
        if (mTheme != null) {
            mTheme.bindThemeView(this, layoutResID);
        }
    }

    /**
     * click back on ActivityTheme {@link ActivityTheme}
     */
    @Override
    public void themeBackClick() {
        finish();
    }

    /**
     * Return the back picture button resource {@link ActivityTheme}
     *
     * @return Drawable resource
     */
    @DrawableRes
    protected int overrideBackIcon() {
        return BaseActivityTheme.NONE;
    }

    /**
     * Return the right picture button resource {@link ActivityTheme}
     *
     * @return Drawable resource
     */
    @DrawableRes
    protected int overrideRightIcon() {
        return BaseActivityTheme.NONE;
    }

    /**
     * click right text button on ActivityTheme {@link ActivityTheme}
     */
    @Override
    public void themeRightIconClick() {
        Toast.makeText(this, "please override themeRightIconClick() method", Toast.LENGTH_SHORT).show();
    }

    @Override
    public FrameLayout returnStyleContent() {
        if (mTheme != null) {
            return mTheme.getThemeContent();
        }
        return null;
    }

    /**
     * Return to the right text button resource {@link ActivityTheme}
     *
     * @return String resource
     */
    @StringRes
    protected int overrideRightText() {
        return BaseActivityTheme.NONE;
    }

    /**
     * click right icon button on ActivityTheme {@link ActivityTheme}
     */
    @Override
    public void themeRightTextClick() {
        Toast.makeText(this, "please override themeRightTextClick() method ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void showLoadingStyle() {
        LoadingStyleManager.loadingStart(this);
    }

    @Override
    public final void showLoadingError() {
        LoadingStyleManager.loadingError(this);
    }

    @Override
    public final void showLoadingEmpty() {
        LoadingStyleManager.loadingEmpty(this);
    }

    @Override
    public final void dismissLoadingStyle() {
        LoadingStyleManager.dismissLoadingStyle(this);
    }

    /**
     * Returns the current page title
     *
     * @return String resource
     */
    @StringRes
    protected abstract int returnTitle();

    @Override
    protected void onDestroy() {
        if (mTheme != null) {
            mTheme.detachTheme();
        }
        LoadingStyleManager.onDestroy(this);
        super.onDestroy();
    }

    private void createActivityTheme() {
        final ActivityTheme theme = BaseActivityTheme.findTheme(ActivityTheme.class);
        if (theme != null) {
            theme.attachTheme(this);
            theme.requestThemeFeature(overrideBackIcon(), getHasTitle(), overrideRightIcon(), getOverrideRightText());
        }
        this.mTheme = theme;
    }

    private String getHasTitle() {
        final int titleRes = returnTitle();
        if (titleRes != BaseActivityTheme.NONE) {
            return getString(titleRes);
        }
        return null;
    }

    private String getOverrideRightText() {
        final int rightTextRes = overrideRightText();
        if (rightTextRes != BaseActivityTheme.NONE) {
            return getString(rightTextRes);
        }
        return null;
    }

}
