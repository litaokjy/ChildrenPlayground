package com.ee.cp.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    public Context context;
    private boolean isShowTitle = true;
    private boolean isShowStatusBar = true;
    private boolean isAllowScreenRotate = true;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityCollector.addActivity(this);
        if (!isShowTitle)
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isShowStatusBar)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(initLayout());
        if (!isAllowScreenRotate)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        initData();
    }

    protected abstract int initLayout();

    protected abstract void initData();

    //是否显示标题栏
    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    //是否显示状态栏
    public void setShowStatusBar(boolean showStatusBar) {
        isShowStatusBar = showStatusBar;
    }

    //是否允许旋转屏幕
    public void setAllowScreenRotate(boolean allowScreenRoate) {
        isAllowScreenRotate = isAllowScreenRotate;
    }

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

    //隐藏软键盘
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && null != imm)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    //显示软键盘
    public void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && null != imm)
            imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        ActivityCollector.removeActivity(this);
    }
}
