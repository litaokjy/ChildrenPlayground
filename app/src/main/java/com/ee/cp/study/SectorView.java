package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SectorView extends View {
    private float[] endAngles = new float[6];
    private float[] startAngles = new float[6];
    private int[] colors = {Color.RED, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.GREEN, Color.BLUE};

    private Paint paint;
    private RectF rectF;

    private int select = -1;
    private float radius;

    public SectorView(Context context) {
        this(context, null);
    }

    public SectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        rectF = new RectF();
    }

    private float centerX;
    private float centerY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //饼状图的起始角度
        float startC = 0;
        //获取圆心坐标
        centerX = getPivotX();
        centerY = getPivotY();
        //获取圆的半径
        if (radius == 0)
            radius = (float) (Math.min(getWidth(), getHeight()) / 2) / 2;
        //绘制每个扇形
        for (int i = 0; i < colors.length; i++) {
            //设置矩形区域
            rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
            //当前扇形的颜色
            paint.setColor(colors[i]);
            //当前扇形的角度
            float sweep = 360.0f * (1.0f / colors.length);
            //保存起始角度
            startAngles[i] = startC;
            //保存结束角度
            endAngles[i] = startC + sweep;
            //计算文字的中心点位置
            float textAngle = startC + sweep / 2;
            float x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180));
            float y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180));
            //获取绘制的文字
            String text = (int) (sweep / 360.0f * 100) + "%";
            //j计算文字的起始点位
            if (textAngle >= 0 && textAngle < 90) {
                if (select == i) {
                    int top = (int) (Math.sin(Math.toRadians(textAngle)) * 25);
                    int left = (int) (Math.cos(Math.toRadians(textAngle)) * 25);
                    rectF.left += left;
                    rectF.right += left;
                    rectF.top += top;
                    rectF.bottom += top;
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180)) + left;
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180)) + top;
                } else {
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180));
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180));
                }
                y += paint.getTextSize();
            } else if (textAngle >= 90 && textAngle < 180) {
                if (select == i) {
                    int top = (int) (Math.sin(Math.toRadians(180 - textAngle)) * 25);
                    int left = (int) (Math.cos(Math.toRadians(180 - textAngle)) * 25);
                    rectF.left -= left;
                    rectF.right -= left;
                    rectF.top += top;
                    rectF.bottom += top;
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180)) - left;
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180)) + top;
                } else {
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180));
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180));
                }
                x -= paint.getTextSize();
                y += paint.getTextSize();
            } else if (textAngle >= 180 && textAngle <= 270) {
                if (select == i) {
                    int top = (int) (Math.sin(Math.toRadians(270 - textAngle)) * 25);
                    int left = (int) (Math.cos(Math.toRadians(270 - textAngle)) * 25);
                    rectF.left -= left;
                    rectF.right -= left;
                    rectF.top -= top;
                    rectF.bottom -= top;
                    //获取扇形弧度的中心点坐标
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180)) - left;
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180)) - top;
                } else {
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180));
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180));
                }
                x -= paint.getTextSize();
            } else if (textAngle > 270 && textAngle < 360) {
                if (select == i) {
                    int top = (int) (Math.sin(Math.toRadians(360 - textAngle)) * 25);
                    int left = (int) (Math.cos(Math.toRadians(360 - textAngle)) * 25);
                    rectF.left += left;
                    rectF.right += left;
                    rectF.top -= top;
                    rectF.bottom -= top;
                    //获取扇形弧度的中心点坐标
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180)) + left;
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180)) - top;
                } else {
                    x = (float) (centerX + radius / 2 * Math.cos(textAngle * Math.PI / 180));
                    y = (float) (centerY + radius / 2 * Math.sin(textAngle * Math.PI / 180));
                }
            }
            if (i == 3){
                rectF.left -= 30;
                rectF.right += 30;
                rectF.top -= 30;
                rectF.bottom += 30;
            }
            //绘制圆弧
            canvas.drawArc(rectF, startC, sweep, true, paint);
            paint.setColor(Color.WHITE);
            //绘制百分比文字
            canvas.drawText(text, x, y, paint);
            //下一个扇形的开始角度
            startC += sweep;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //获取点击位置的坐标
            float x = event.getX();
            float y = event.getY();
            //获取点击角度
            float angle = 0;
            //判断当前点击位置在第几象限
            if (x >= centerX && y >= centerY)
                angle = (float) (Math.atan((y - centerY) / (x - centerX)) * 180 / Math.PI);
            else if (x <= centerX && y >= centerY)
                angle = (float) (Math.atan((centerX - x) / (y - centerY)) * 180 / Math.PI + 90);
            else if (x <= centerX && y <= centerY)
                angle = (float) (Math.atan((centerY - y) / (centerX - x)) * 180 / Math.PI + 180);
            else if (x >= centerX && y <= centerY)
                angle = (float) (Math.atan((x - centerX) / (centerY - y)) * 180 / Math.PI + 270);
            for (int i = 0; i < startAngles.length; i++) {
                if (startAngles[i] <= angle && endAngles[i] >= angle) {
                    select = i;
                    invalidate();
                    return true;
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
