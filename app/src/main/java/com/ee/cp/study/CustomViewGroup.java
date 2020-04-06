package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class CustomViewGroup extends TextView {

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.v("MainActivity", "dispatchTouchEvent3");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MainActivity", "onTouchEvent3");
        return super.onTouchEvent(event);
    }
}