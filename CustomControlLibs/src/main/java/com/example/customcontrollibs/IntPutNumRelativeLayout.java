package com.example.customcontrollibs;

import android.content.Context;
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

    public IntPutNumRelativeLayout(Context context) {
        this(context, null);
    }

    public IntPutNumRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntPutNumRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setUpEvent();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.int_put_num_view, this, true);
        Mreduce_num = findViewById(R.id.reduce_num);
        Med_num = findViewById(R.id.edi_num);
        Mplus_num = findViewById(R.id.plus_num);
    }

    private void setUpEvent() {
        Mreduce_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MCurrentNum--;
                DataRefresh();
            }
        });
        Mplus_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MCurrentNum++;
                DataRefresh();
            }
        });
    }

    private void DataRefresh() {
        Med_num.setText(String.valueOf(MCurrentNum));
        if (mOnNumberChangerListener != null) {
            mOnNumberChangerListener.onNumberChanger(this.MCurrentNum);
        }
    }

    public int getNum() {
        return MCurrentNum;
    }

    public void setNum(int value) {
        this.MCurrentNum = value;
        DataRefresh();
    }

    public void setOnNumberChangerListener(OnNumberChangerListener listener) {
        this.mOnNumberChangerListener = listener;
    }

    public interface OnNumberChangerListener {
        void onNumberChanger(int value);
    }
}
