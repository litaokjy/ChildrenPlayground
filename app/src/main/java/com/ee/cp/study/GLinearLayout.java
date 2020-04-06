package com.ee.cp.study;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class GLinearLayout extends LinearLayout {
    public GLinearLayout(Context context) {
        super(context);
    }

    public GLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.v("MainActivity", "dispatchTouchEvent2");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("MainActivity", "onInterceptTouchEvent2");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MainActivity", "onTouchEvent2");
        return super.onTouchEvent(event);
    }
}
