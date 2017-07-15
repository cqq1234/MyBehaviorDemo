package com.example.administrator.mybehaviordemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.mybehaviordemo.R;

/**
 * Created by 崔琦 on 2017/7/6 0006.
 * Describe : 仿IOS控制中心进度条
 */

public class RectProgress extends View{

    //progress进度方向
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;

    //默认的背景颜色和进度条颜色
    private final int defaultBgColor = 0xFf000000;
    private final int defaultProgressColor = 0xFFFF4081;
    private int bgColor = defaultBgColor;
    private int progressColor = defaultProgressColor;

    //矩形4个角的弧度
    private float rectRadius = 18f;
    //用来画背景的RectF
    private RectF bgRect = new RectF();
    //用来画进度的RectF
    private RectF progressRect = new RectF();
    //背景画笔
    private Paint bgPaint;
    //进度画笔
    private Paint progressPaint;
    //进度方向
    private int orientation = VERTICAL;
    //进度条最大值
    private int max = 100;
    //progress最初进度
    private int progress = 15;
    //将图片转为bitmap
    private Bitmap bitmap;
    //图标显示的区域
    private Rect srcRect;
    //图标显示的坐标位置
    private Rect dstRect;
    //图标内边距
    private float iconPadding;
    //progress进度百分比
    private int percent = 0;

    public RectProgress(Context context) {
        this(context,null);
    }

    public RectProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RectProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectProgress);
        bgColor = typedArray.getColor(R.styleable.RectProgress_bgColor,defaultBgColor);
        progressColor = typedArray.getColor(R.styleable.RectProgress_progressColor,defaultProgressColor);
        progress = typedArray.getInteger(R.styleable.RectProgress_progressValue,progress);
        max = typedArray.getInteger(R.styleable.RectProgress_progressMax,max);
        if (max <= 0)
            throw new RuntimeException("Max 必须大于 0");
        rectRadius = typedArray.getDimension(R.styleable.RectProgress_rectRadius,18);
        iconPadding = typedArray.getDimension(R.styleable.RectProgress_iconPadding,10);
        int imgSrc = typedArray.getResourceId(R.styleable.RectProgress_iconSrc,0);
        orientation = typedArray.getInteger(R.styleable.RectProgress_progressOrientation,VERTICAL);
        if (max < progress){
            progress = max;
        }
        typedArray.recycle();
        if (imgSrc != 0){
            bitmap = ((BitmapDrawable)getResources().getDrawable(imgSrc)).getBitmap();
        }
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        progressPaint.setColor(progressColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgRect.set(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        //计算progress进度
        computeProgressRect();
        if (bitmap != null){
            srcRect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
            int iconSideLength;
            if (orientation == VERTICAL){
                iconSideLength = (int) (bgRect.width() - iconPadding * 2);
                dstRect = new Rect((int) bgRect.left + (int) iconPadding
                        , (int) (bgRect.bottom - iconSideLength - iconPadding)
                        , (int) bgRect.right - (int) iconPadding
                        , (int) bgRect.bottom - (int) iconPadding);
            }else {
                iconSideLength = (int) (bgRect.height() - iconPadding * 2);
                dstRect = new Rect((int) bgRect.left + (int) iconPadding
                        , (int) (bgRect.bottom - iconPadding - iconSideLength)
                        , (int) (bgRect.left + iconPadding + iconSideLength)
                        , (int) (bgRect.bottom - iconPadding));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layerId = canvas.saveLayer(0,0,canvasWidth,canvasHeight,null,Canvas.ALL_SAVE_FLAG);
        {
            bgPaint.setColor(bgColor);
            canvas.drawRoundRect(bgRect, rectRadius, rectRadius, bgPaint);
            canvas.drawRect(progressRect,progressPaint);
            bgPaint.setXfermode(null);
            if (bitmap != null){
                canvas.drawBitmap(bitmap, srcRect, dstRect, bgPaint);
            }
        }
        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (bgRect.contains(event.getX(),event.getY())){
                    //按下时,在进度内才执行操作
                    handleTouch(event);
                    invalidate();
                    return true;
                }

        }
        return super.onTouchEvent(event);

    }

    /**
     * 触摸事件处理
     * */
    private void handleTouch(MotionEvent event){
        if (orientation == VERTICAL) {
            if (event.getY() < bgRect.top) {
                //触点超出Progress顶部
                progressRect.top = bgRect.top;
            } else if (event.getY() > bgRect.bottom) {
                //触点超过Progress底部
                progressRect.top = bgRect.bottom;
            } else {
                progressRect.top = event.getY();
            }
            int tmp = (int) ((progressRect.height() / bgRect.height()) * 100);
            if (percent != tmp) {
                percent = tmp;
                progress = percent * max / 100;
                if (changedListener != null)
                    changedListener.onProgressChanged(progress, percent);
            }
        } else {
            if (event.getX() > bgRect.right) {
                //触点超出Progress右端
                progressRect.right = bgRect.right;
            } else if (event.getX() < bgRect.left) {
                //触点超出Progress左端
                progressRect.right = bgRect.left;
            } else {
                progressRect.right = event.getX();
            }
            int tmp = (int) ((progressRect.width() / bgRect.width()) * 100);
            if (percent != tmp) {
                percent = tmp;
                progress = percent * max / 100;
                if (changedListener != null)
                    changedListener.onProgressChanged(progress, percent);
            }
        }
    }

    private OnProgressChangedListener changedListener;

    public void setChangedListener(OnProgressChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public interface OnProgressChangedListener{
        void onProgressChanged(int currentValue, int percent);
    }

    public void setMax(int m) {
        if (max <= 0)
            throw new RuntimeException("Max 必须大于 0");
        max = m;
    }

    public void setProgress(int p) {
        int oldProgress = progress;
        progress = p;
        if (max < progress) {
            progress = max;
        } else if (progress < 0)
            progress = 0;

        startProgressAnim(oldProgress);
    }

    private ValueAnimator valueAnimator;

    private void startProgressAnim(int oldProgress){
        if (valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(oldProgress,progress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (int)valueAnimator.getAnimatedValue();
                computeProgressRect();
                invalidate();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }
    /**
     * 计算progress进度
     * */
    private void computeProgressRect(){
        if (orientation == VERTICAL){
            progressRect.set(bgRect.left,
                    bgRect.bottom - progress * bgRect.height() / max,
                    bgRect.right,
                    bgRect.bottom);
        }else {
            progressRect.set(bgRect.left
                    , bgRect.top
                    , bgRect.left + progress * bgRect.width() / max
                    , bgRect.bottom);
        }
    }
}
