package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    private Paint paint;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        paint.setColor(Color.RED);
        //绘制方形背景
        @SuppressLint("DrawAllocation")
        Rect rect = new Rect(0, 0, width, height);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(0, 0, width, height, paint);
        canvas.drawLine(width, 0, 0, height, paint);
    }
}
