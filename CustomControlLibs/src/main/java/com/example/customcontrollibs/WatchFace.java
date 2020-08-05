package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class WatchFace extends View {

    private int mSecondColor;
    private int mMinColor;
    private int mHourColor;
    private int mScaleColor;
    private boolean mScaleShow;
    private int mBgResId;
    private Paint mSecondpaint;
    private Paint mMinpaint;
    private Paint mHourpaint;
    private Paint mScalepaint;
    private Paint mScaleBgpaint;
    private int Inner_radius;
    private int mHeight;
    private int mWidth;

    public WatchFace(Context context) {
        this(context, null);
    }

    public WatchFace(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchFace(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaints();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WatchFace);
        mSecondColor = a.getColor(R.styleable.WatchFace_secondHandColor, getResources().getColor(R.color.secondColor));
        mMinColor = a.getColor(R.styleable.WatchFace_minuteHandColor, getResources().getColor(R.color.minColor));
        mHourColor = a.getColor(R.styleable.WatchFace_hourHandColor, getResources().getColor(R.color.hourColor));
        mScaleColor = a.getColor(R.styleable.WatchFace_scaleColor, getResources().getColor(R.color.scaleColor));
        mBgResId = a.getColor(R.styleable.WatchFace_faceBackground, getResources().getColor(R.color.grey));
        mScaleShow = a.getBoolean(R.styleable.WatchFace_scaleShow, true);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取真实高，宽
        int widthTargetSize = widthSize - getPaddingLeft() - getPaddingRight();
        int heightTargetSize = heightSize - getPaddingTop() - getPaddingBottom();
        //获取长度小的那一方
        int mTargetSize = Math.min(widthTargetSize, heightTargetSize);
        //测量出一个正方形
        setMeasuredDimension(mTargetSize, mTargetSize);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    /**
     * 创建相关画笔
     */
    private void initPaints() {
        //秒针
        mSecondpaint = new Paint();
        mSecondpaint.setColor(mSecondColor);
        mSecondpaint.setStyle(Paint.Style.STROKE);
        mSecondpaint.setStrokeWidth(2f);
        mSecondpaint.setAntiAlias(true);
        //分针
        mMinpaint = new Paint();
        mMinpaint.setColor(mMinColor);
        mMinpaint.setStyle(Paint.Style.STROKE);
        mMinpaint.setStrokeWidth(3f);
        mMinpaint.setAntiAlias(true);
        //时针
        mHourpaint = new Paint();
        mHourpaint.setColor(mHourColor);
        mHourpaint.setStyle(Paint.Style.STROKE);
        mHourpaint.setStrokeWidth(4f);
        mHourpaint.setAntiAlias(true);
        //刻度
        mScalepaint = new Paint();
        mScalepaint.setColor(mScaleColor);
        mScalepaint.setStyle(Paint.Style.FILL);
        mScalepaint.setStrokeWidth(3f);
        mScalepaint.setAntiAlias(true);
        //刻度边框
        mScaleBgpaint = new Paint();
        mScaleBgpaint.setColor(mBgResId);
        mScaleBgpaint.setStyle(Paint.Style.FILL);
        mScaleBgpaint.setStrokeWidth(5f);
        mScaleBgpaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //外环半径
        int Outer_radius = (int) (mWidth / 2);
        //内环半径
        Inner_radius = (int) (mWidth / 2 * 0.9f);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, Outer_radius, mScaleBgpaint);
        //内环刻度
        for (int i = 0; i < 12; i++) {
            double thPi = Math.PI * 2 / 12 * i;
            //内环刻度X,Y坐标
            int IParameterB = (int) (Inner_radius * Math.cos(thPi));
            int IParameterX = mHeight / 2 - IParameterB;
            int IParameterA = (int) (Math.sin(thPi) * Inner_radius);
            int IParemeterY = mWidth / 2 + IParameterA;
            //外环刻度
            //外环刻度X,Y坐标
            int OParameterB = (int) (Outer_radius * Math.cos(thPi));
            int OParameterX = mHeight / 2 - OParameterB;
            int OParameterA = (int) (Math.sin(thPi) * Outer_radius);
            int OParemeterY = mWidth / 2 + OParameterA;

            canvas.drawLine(IParameterX, IParemeterY, OParameterX, OParemeterY, mScalepaint);
        }
    }

}
