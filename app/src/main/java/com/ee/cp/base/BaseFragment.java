package com.ee.cp.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    private View rootView;
    private Activity mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(initLayout(), container, false);
        ButterKnife.bind(this, rootView);
        mContext = getActivity();
        initData(mContext);
        return rootView;
    }

    abstract int initLayout();

    abstract void initData(Context mContext);

    //保证同一按钮在1秒内只会响应一次点击事件
    public abstract class OnSingleClickListener implements View.OnClickListener {
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        abstract void onSingleClick(View view);

        @Override
        public void onClick(View view) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(view);
            }
        }
    }

    //同一按钮在短时间内可重复响应点击事件
    public abstract class OnMultiClickListener implements View.OnClickListener {
        abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
        ButterKnife.bind(mContext).unbind();
    }


}
