package com.ee.cp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseLazyFragment extends BaseFragment {
    //是否初始化过布局
    private boolean isViewInitiated;
    //当前界面是否可见
    private boolean isVisibleToUser;
    //是否加载过数据
    private boolean isDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser)
            prepareFetchData();
    }

    private void prepareFetchData() {
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            loadData();
            isDataInitiated = true;
        }
    }

    abstract void loadData();
}
