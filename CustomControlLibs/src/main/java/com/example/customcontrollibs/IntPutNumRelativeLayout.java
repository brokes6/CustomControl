package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class IntPutNumRelativeLayout extends RelativeLayout {
    private int MCurrentNum = 0;
    private TextView Mreduce_num;
    private EditText Med_num;
    private TextView Mplus_num;
    private OnNumberChangerListener mOnNumberChangerListener = null;
    private int mMax;
    private int mMin;
    private int mStep;
    private boolean mDisable;
    private int mBtnBgRes;

    public IntPutNumRelativeLayout(Context context) {
        this(context, null);
    }

    public IntPutNumRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntPutNumRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArres(context, attrs);
        initView(context);
        setUpEvent();
    }

    /**
     * get Arres
     *
     * @param context
     * @param attrs
     */
    private void initArres(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IntPutNumRelativeLayout);
        mMax = a.getInt(R.styleable.IntPutNumRelativeLayout_max, 0);
        mMin = a.getInt(R.styleable.IntPutNumRelativeLayout_min, 0);
        mStep = a.getInt(R.styleable.IntPutNumRelativeLayout_step, 1);
        mDisable = a.getBoolean(R.styleable.IntPutNumRelativeLayout_disable, false);
        mBtnBgRes = a.getResourceId(R.styleable.IntPutNumRelativeLayout_BgColoer, -1);
        a.recycle();
    }

    /**
     * Initialize control
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.int_put_num_view, this, true);
        Mreduce_num = findViewById(R.id.reduce_num);
        Med_num = findViewById(R.id.edi_num);
        Mplus_num = findViewById(R.id.plus_num);

        Mreduce_num.setEnabled(!mDisable);
        Mplus_num.setEnabled(!mDisable);
    }

    private void setUpEvent() {
        Mreduce_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Mplus_num.setEnabled(true);
                MCurrentNum -= mStep;
                if (MCurrentNum <= mMin) {
                    view.setEnabled(false);
                    MCurrentNum = mMin;
                }
                DataRefresh();
            }
        });
        Mplus_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Mreduce_num.setEnabled(true);
                MCurrentNum += mStep;
                if (MCurrentNum >= mMax) {
                    view.setEnabled(false);
                    MCurrentNum = mMax;
                }
                DataRefresh();
            }
        });
    }

    /**
     * Refresh data, notify changes
     */
    private void DataRefresh() {
        Med_num.setText(String.valueOf(MCurrentNum));
        if (mOnNumberChangerListener != null) {
            mOnNumberChangerListener.onNumberChanger(this.MCurrentNum);
        }
    }


    public void setOnNumberChangerListener(OnNumberChangerListener listener) {
        this.mOnNumberChangerListener = listener;
    }

    public interface OnNumberChangerListener {
        void onNumberChanger(int value);
    }

    /**
     * getData and setData
     *
     * @return
     */
    public int getNum() {
        return MCurrentNum;
    }

    public void setNum(int value) {
        this.MCurrentNum = value;
        DataRefresh();
    }

    public int getCurrentNumMax() {
        return mMax;
    }

    public void setCurrentNumMax(int value) {
        this.mMax = value;
    }

    public int getCurrentNumMin() {
        return mMin;
    }

    public void setCurrentNumMin(int value) {
        this.mMin = value;
    }

    public int getInputNumStep() {
        return mStep;
    }

    public void setInputNumStep(int value) {
        this.mStep = value;
    }

    public boolean isInputNumDisable() {
        return mDisable;
    }

    public void setInputNumDisable(boolean mDisable) {
        this.mDisable = mDisable;

    }

    public int getInputNumBtnBgRes() {
        return mBtnBgRes;
    }

    public void setInputNumBtnBgRes(int reference) {
        this.mBtnBgRes = reference;
    }
}
