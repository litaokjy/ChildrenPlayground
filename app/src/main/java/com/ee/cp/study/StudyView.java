package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ee.cp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class StudyView extends View {

    private String text;
    private int textSize;
    private int textColor;

    private Paint paint;
    private Rect textBranch;
    private int drawTextHeight;
    private List<String> textList = new ArrayList<>();

    //在Java代码中new时调用
    public StudyView(Context context) {
        this(context, null);
    }

    //在xml布局文件中使用时自动调用
    public StudyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        //获取自定义属性的值
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.study_view);
        text = typedArray.getString(R.styleable.study_view_text);
        textColor = typedArray.getColor(R.styleable.study_view_textColor, Color.BLACK);
        textSize = (int) typedArray.getDimension(R.styleable.study_view_textSize, 100);
        typedArray.recycle();//回收typedArray
    }

    private void init() {
        //初始化画笔
        paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);

        //获得绘制文本的宽高
        textBranch = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBranch);

        //计算各线在位置
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //每行文字的绘制高度，建议低于基线的距离 - 建议距基线以上的距离
        drawTextHeight = (int) (fontMetrics.descent - fontMetrics.ascent);
    }


    //MeasureSpec是View的静态内部类。用来描述父控件对子控件尺寸的约束
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置文本自动分行
        setBranch(widthMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private void setBranch(int widthMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取宽的尺寸
        if (textList.size() == 0) {
            //显示文本最大宽度
            float specMaxWidth = widthSize - getPaddingLeft() - getPaddingRight();
            //实际显示的行数
            int lineNum;
            if (textBranch.width() > specMaxWidth) {
                //获取带小数的行数
                float lineNumF = textBranch.width() * 1.0f / specMaxWidth;
                //如果有小数就进1
                if ((lineNumF + "").contains(".")) {
                    lineNum = (int) (lineNumF + 0.5);
                } else {
                    lineNum = (int) lineNumF;
                }
                //每行展示文字的长度
                int lineLength = (int) (text.length() / lineNumF);
                for (int i = 0; i < lineNum; i++) {
                    String lineStr;
                    //判断是否可以一行显示
                    if (text.length() < lineLength) {
                        lineStr = text;
                    } else {
                        lineStr = text.substring(0, lineLength);
                    }
                    textList.add(lineStr);
                    //重新赋值text
                    if (!TextUtils.isEmpty(text)) {
                        if (text.length() > lineLength) {
                            text = text.substring(lineLength);
                        }
                    } else {
                        break;
                    }
                }
            } else {
                textList.add(text);
            }
        }
    }

    /**
     * @return 控件的宽度
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取宽的尺寸
        if (widthMode == MeasureSpec.EXACTLY) {//match_parent、具体值
            return widthSize;//控件的宽度
        } else {//wrap_content
            //获取文本的宽度
            float textWidth;
            if (textList.size() > 1) {
                textWidth = widthSize;
            } else {
                textWidth = textBranch.width();
            }
            return (int) (getPaddingLeft() + textWidth + getPaddingRight());//控件的宽度
        }
    }

    /**
     * @return 控件的高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取高的模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//获取高的尺寸
        if (heightMode == MeasureSpec.EXACTLY) {//match_parent、具体值
            return heightSize;
        } else {//wrap_content
            //获取文本的高度
            float textHeight = drawTextHeight * textList.size();
            return (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < textList.size(); i++) {
            paint.getTextBounds(textList.get(i), 0, textList.get(i).length(), textBranch);
            canvas.drawText(textList.get(i), getPaddingLeft(), (getPaddingTop() + drawTextHeight * (i + 1)), paint);
        }

    }
}
