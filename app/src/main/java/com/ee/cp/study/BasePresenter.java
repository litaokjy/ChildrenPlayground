package com.ee.cp.study;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<T extends BaseView> {
    private Reference<T> viewRef;

    void attachView(T view) {
        viewRef = new WeakReference<>(view);
    }

    protected T getView() {
        if (isViewAttached())
            return viewRef.get();
        else
            return null;
    }

    private boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}
