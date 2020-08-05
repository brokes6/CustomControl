package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TopTextView extends LinearLayout {
    private Context mContext;
    private View view;
    private TextView Top_Text, Bottom_Text;
    private String mTopText;
    private String mBottomText;
    private int mSpacing;

    public TopTextView(Context context) {
        this(context, null);
    }

    public TopTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.top_text_view, this, true);
        Top_Text = view.findViewById(R.id.Text_num);
        Bottom_Text = view.findViewById(R.id.Text_text);

        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.TopTextView);
        setTop_Text(array.getString(R.styleable.TopTextView_Top_Text));
        setBottom_Text(array.getString(R.styleable.TopTextView_Bottom_Text));
        array.recycle();
    }
    public void setTop_Text(String topText){
        this.mTopText = topText;
        Top_Text.setText(topText);
    }
    public void setBottom_Text(String bottomText){
        this.mBottomText = bottomText;
        Bottom_Text.setText(bottomText);
    }
//    public void setTextSpacing(int spacing){
//        mSpacing = spacing;
//        Bottom_Text.setMa
//    }
    public void setTopTextSize(int value){
        Top_Text.setTextSize(value);
    }
    public void setBottomTextSize(int value){
        Bottom_Text.setTextSize(value);
    }

    public String getTopText() {
        return mTopText;
    }

    public String getBottomText() {
        return mBottomText;
    }
}
