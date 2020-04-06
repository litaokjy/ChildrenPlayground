package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ee.cp.R;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CustomImageView extends ImageView {

    public static final int SHAPE_CIRCLE = 0;  //圆形
    public static final int SHAPE_ROUND = 1;   //圆角
    private final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private int borderWidth;//边框宽
    private int borderColor;//边框颜色
    private int roundRadius;//圆角半径
    private int shapeType; //形状

    private int width;
    private int height;
    private Paint bitmapPaint = new Paint();
    private Paint borderPaint = new Paint();
    private RectF contentRect = new RectF();

    private float contentRadius;
    private float borderRadius;
    private Bitmap bitmap;

    //在Java代码中new时调用
    public CustomImageView(Context context) {
        this(context, null);
    }

    //在xml布局文件中使用时自动调用
    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CustomImageView_border_width, 0);
        borderColor = typedArray.getColor(R.styleable.CustomImageView_border_color, Color.BLACK);
        shapeType = typedArray.getInt(R.styleable.CustomImageView_shape_type, SHAPE_CIRCLE);
        roundRadius = typedArray.getDimensionPixelSize(R.styleable.CustomImageView_round_radius, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        bitmap = getBitmapFormDrawable();
        updateSetup();
        if (bitmap == null) {
            return;
        }
        if (shapeType == SHAPE_CIRCLE) {
            drawCircle(canvas);
        } else if (shapeType == SHAPE_ROUND) {
            drawRoundRect(canvas);
        }
    }

    //从Drawable中取出Bitmap
    private Bitmap getBitmapFormDrawable() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return null;
        //是BitmapDrawable直接取出Bitmap
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        try {
            //在ColorDrawable中getIntrinsicWidth为-1，所以给了个默认值2
            int DEFAULT_DRAWABLE_DIMENSION = 2;
            int width = drawable.getIntrinsicWidth() <= 0 ? DEFAULT_DRAWABLE_DIMENSION : drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight() <= 0 ? DEFAULT_DRAWABLE_DIMENSION : drawable.getIntrinsicHeight();
            //创建Bitmap画布，并绘制
            Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateSetup() {
        if (width == 0 && height == 0) {
            return;
        }
        if (bitmap == null) {
            super.invalidate();
            return;
        }
        //设置BitmapShader使用拉伸模式
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置BitmapPaint
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setShader(bitmapShader);
        //设置边框画笔
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        //复制src坐标
        contentRect.set(calculateRect());
        //设置图片边框
        if (borderWidth > 0) {
            //图片真实大小
            contentRect.inset(borderWidth - 0.0f, borderWidth - 0.0f);
        }
        //圆形
        if (shapeType == SHAPE_CIRCLE) {
            //边框半径
            borderRadius = Math.min((contentRect.width() - borderWidth) / 2.0f, (contentRect.height() - borderWidth) / 2.0f);
            //内容半径
            contentRadius = Math.min(contentRect.width() / 2.0f, contentRect.height() / 2.0f);
        }
        updateMatrix(bitmapShader);
    }

    //计算矩形大小
    private RectF calculateRect() {
        int rWidth = width - getPaddingLeft() - getPaddingRight();
        int rHeight = height - getPaddingTop() - getPaddingBottom();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        return new RectF(left, top, left + rWidth, top + rHeight);
    }

    //更新矩阵
    private void updateMatrix(BitmapShader bitmapShader) {
        float scale;
        float dx = 0, dy = 0;
        final int bHeight = bitmap.getHeight();
        final int bWidth = bitmap.getWidth();
        final float cWidth = contentRect.width();
        final float cHeight = contentRect.height();
        //计算缩放比例 平移距离
        if (bWidth * cHeight > cWidth * bHeight) {
            //宽度比 > 高度比 取高度比缩放
            scale = cHeight / (float) bHeight;
            //计算横向移动距离
            dx = (cWidth - bWidth * scale) * 0.5f;
        } else {
            scale = cWidth / (float) bWidth;
            dy = (cHeight - bHeight * scale) * 0.5f;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postTranslate(Math.round(dx) + contentRect.left, Math.round(dy) + contentRect.left);
        bitmapShader.setLocalMatrix(matrix);
    }

    //圆角矩形
    private void drawRoundRect(Canvas canvas) {
        //绘制图片
        canvas.drawRoundRect(contentRect, roundRadius, roundRadius, bitmapPaint);
        //绘制边框线
        if (borderWidth > 0) {
            canvas.drawRoundRect(contentRect, roundRadius, roundRadius, borderPaint);
        }

    }

    //圆形
    private void drawCircle(Canvas canvas) {
        //绘制图片
        canvas.drawCircle(contentRect.centerX(), contentRect.centerY(), contentRadius, bitmapPaint);
        //绘制边框线
        if (borderWidth > 0) {
            canvas.drawCircle(contentRect.centerX(), contentRect.centerY(), borderRadius, borderPaint);
        }
    }
}
