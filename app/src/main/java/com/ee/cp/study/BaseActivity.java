package com.ee.cp.study;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<V extends BaseView, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView((V) this);
        }
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected abstract int setLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract T createPresenter();
}
