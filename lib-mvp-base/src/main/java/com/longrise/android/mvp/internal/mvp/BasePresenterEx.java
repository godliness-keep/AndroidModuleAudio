package com.longrise.android.mvp.internal.mvp;

import com.longrise.android.mvp.utils.GenericUtil;

/**
 * Created by godliness on 2019-06-29.
 *
 * @author godliness
 */
public abstract class BasePresenterEx<V, M> extends BasePresenter<V> {

    protected final M mModal;

    public BasePresenterEx() {
        this.mModal = GenericUtil.getT(this, 1);
    }

    @Override
    public final void init() {
        exInit();

        if (mModal != null) {
            ((BaseModel) mModal).init();
        }
    }

    /**
     * The BasePresenterEx is initialized
     */
    protected abstract void exInit();
}
