package com.longrise.android.mvp.internal.loadstyle.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by godliness on 2019-07-05.
 *
 * @author godliness
 */
public abstract class BaseLoadStyle<LoadListener extends ILoadStyleListener> implements View.OnClickListener{

    /**
     * Save the loading style currently in use
     */
    private static final ArrayMap<ViewGroup, BaseLoadStyle> CURRENT_LOAD_STYLES = new ArrayMap<>();

    private LayoutInflater mInflater;
    private LoadListener mCallback;

    protected BaseLoadStyle() {

    }

    @Nullable
    public static <LoadStyle extends BaseLoadStyle> LoadStyle obtainLoadStyle(Class<LoadStyle> clzT) {
        return StorageLoadStyle.obtainLoadStyle(clzT);
    }

    public static <T extends ILoadStyleListener> void recycleLoadStyle(T loadListener) {
        final FrameLayout content = loadListener.returnStyleContent();
        final BaseLoadStyle loadStyle = CURRENT_LOAD_STYLES.remove(content);
        if (loadStyle != null) {
            loadStyle.detachUser();
        }
    }

    /**
     * Binding loading style
     *
     * @param loadListener {@link ILoadStyleListener}
     */
    public final void bindLoadStyle(LoadListener loadListener) {
        this.mCallback = loadListener;
        final FrameLayout content = loadListener.returnStyleContent();
        attachUser(content);
        final BaseLoadStyle old = CURRENT_LOAD_STYLES.put(content, this);
        if (old != null) {
            old.detachUser();
        }
    }

    /**
     * Inflater load style layout
     */
    protected final View inflater(@LayoutRes int layoutId, @Nullable ViewGroup container) {
        if (mInflater == null) {
            this.mInflater = LayoutInflater.from(getContext().getApplicationContext());
        }
        return mInflater.inflate(layoutId, container, false);
    }

    /**
     * Notify reload
     */
    protected final void callReload() {
        if (mCallback != null) {
            mCallback.onReload();
        }
    }

    /**
     * Binding loading style
     *
     * @param content Need to bind the container with the loading style
     */
    protected abstract void attachLoadStyle(@NonNull ViewGroup content);

    /**
     * Remove load style
     */
    protected abstract void detachLoadStyle();

    private Context getContext() {
        return mCallback.returnStyleContent().getContext();
    }

    private void attachUser(ViewGroup content) {
        attachLoadStyle(content);
    }

    private void detachUser() {
        this.mCallback = null;
        detachLoadStyle();
        StorageLoadStyle.recycleLoadStyle(this);
    }

}
