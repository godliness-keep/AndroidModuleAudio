package com.longrise.android.mvp.internal.activitytheme;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.longrise.android.mvp.BaseActivity;
import com.longrise.android.mvp.R;
import com.longrise.android.mvp.internal.activitytheme.base.BaseActivityTheme;
import com.longrise.android.mvp.internal.activitytheme.base.IActivityThemeListener;
import com.longrise.android.mvp.utils.MvpLog;


/**
 * Created by godliness on 2019-07-02.
 *
 * @author godliness
 * The default theme in BaseActivity
 */
public final class ActivityTheme extends BaseActivityTheme<BaseActivity> implements View.OnClickListener {

    private static final String TAG = "ActivityTheme";

    private View mThemeView;
    private FrameLayout mThemeContent;

    private ImageButton mBackView;
    private TextView mTvTitle;
    private View mRightView;
    private ImageView mIvRight;
    private TextView mTvRight;

    private IActivityThemeListener mThemeCallback;

    public ActivityTheme() {
        MvpLog.d(TAG, "new ActivityTheme()");
    }

    public void requestThemeFeature(@DrawableRes int backIconRes, String title, @DrawableRes int hasRightIconRes, String hasRightText) {
        if (backIconRes != BaseActivityTheme.NONE) {
            bindBackIconRes(backIconRes);
        }
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
        if (hasRightIconRes != BaseActivityTheme.NONE) {
            bindRightIcon(hasRightIconRes);
        }
        if (hasRightText != null) {
            bindRightText(hasRightText);
        }
    }

    @Override
    protected void bindTarget(BaseActivity target) {
        this.mThemeCallback = target;
    }

    @Override
    protected void unBindTarget() {
        this.mThemeCallback = null;
    }

    @Override
    protected void createThemeLayout(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        mThemeView = inflater.inflate(R.layout.lib_mvp_theme_activity_layout, container, false);
        mBackView = mThemeView.findViewById(R.id.ib_left_lr_mvp);
        mBackView.setOnClickListener(this);
        mThemeContent = mThemeView.findViewById(R.id.content_theme_lib_mvp);
        mTvTitle = mThemeView.findViewById(R.id.tv_title_lr_mvp);
    }

    @NonNull
    @Override
    protected View getThemeView() {
        return mThemeView;
    }

    @NonNull
    @Override
    public FrameLayout getThemeContent() {
        return mThemeContent;
    }

    @Override
    protected boolean hasRecycle() {
        return true;
    }

    @Override
    protected void recycled() {
        if (mIvRight != null) {
            mIvRight.setVisibility(View.GONE);
        }
        if (mTvRight != null) {
            mTvRight.setVisibility(View.GONE);
        }
        if (mTvTitle != null) {
            mTvTitle.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        if (mThemeCallback != null) {
            final int id = v.getId();
            if (id == R.id.tv_right_lib_mvp) {
                mThemeCallback.themeRightTextClick();
            } else if (id == R.id.iv_right_lib_mvp) {
                mThemeCallback.themeRightIconClick();
            } else if (id == R.id.ib_left_lr_mvp) {
                mThemeCallback.themeBackClick();
            }
        }
    }

    private void bindRightIcon(@DrawableRes int hasRightIcon) {
        if (mIvRight == null) {
            mIvRight = inflaterRightButtonView(R.id.iv_right_lib_mvp);
            mIvRight.setOnClickListener(this);
        }
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setImageResource(hasRightIcon);
    }

    private void bindRightText(String hasRightText) {
        if (mTvRight == null) {
            mTvRight = inflaterRightButtonView(R.id.tv_right_lib_mvp);
            mTvRight.setOnClickListener(this);
        }
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(hasRightText);
    }

    private void bindBackIconRes(int backIconRes) {
        if (mBackView != null) {
            mBackView.setImageResource(backIconRes);
        }
    }

    private <T extends View> T inflaterRightButtonView(@IdRes int resId) {
        if (mRightView == null) {
            final ViewStub viewStub = mThemeView.findViewById(R.id.vs_right_button_title_bar);
            mRightView = viewStub.inflate();
        }
        return mRightView.findViewById(resId);
    }

}
