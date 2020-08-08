package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class ImageTopView extends LinearLayout {
    private Context mContext;
    private View view;
    private TextView text;
    private ImageView img;
    private int mSpacing;
    private String mText;

    public ImageTopView(Context context) {
        this(context, null);
    }

    public ImageTopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        LayoutInflater la = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = la.inflate(R.layout.image_top_view, this, true);
        text = view.findViewById(R.id.I_text);
        img = view.findViewById(R.id.I_image);

        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ImageTopView);
//        final Drawable d = array.getDrawable(R.styleable.ImageTopView_Src);
//        if (d != null) {
//            setImageDrawable(d);
//        }
        setDrawable(array.getDrawable(R.styleable.ImageTopView_Image_Drawable));
        setText(array.getString(R.styleable.ImageTopView_Image_Text));
        setTextSpacing(array.getInt(R.styleable.ImageTopView_Image_Text_Spacing, 0));
        setTextSize(array.getInt(R.styleable.ImageTopView_Text_Size, 16));
        setImageSize(array.getInt(R.styleable.ImageTopView_Image_Size, 30));
        setTextColor(array.getInt(R.styleable.ImageTopView_Text_Color,-1));
        array.recycle();
    }

    public void setTextSpacing(int spacing) {
        mSpacing = spacing;
        LinearLayout.LayoutParams lp = (LayoutParams) text.getLayoutParams();
        lp.setMargins(0, spacing, 0, 0);
        text.setLayoutParams(lp);
    }

    public void setImageSize(int value) {
        int px = DensityUtil.dip2px(mContext, value);
        //将用户输入的数据转换为dp
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) img.getLayoutParams();
        //取控件imageView当前的布局参数 linearParams.height/width = value;// 控件的高强制设成用户设置的
        linearParams.width = px;
        linearParams.height = px;
        // 控件的宽强制设成30
        img.setLayoutParams(linearParams);
        //使设置好的布局参数应用到控件
    }

    public void setText(String value) {
        this.mText = value;
        text.setText(mText);
    }

    public void setTextSize(int value) {
        text.setTextSize(value);
    }

    public void setDrawable(Drawable d) {
        if (d != null) {
            setImageDrawable(d);
        }
    }

    public void setUrl(String url) {
        if (url == null) {
            Glide.with(mContext).load(url).into(img);
        }
    }

    private void setImageDrawable(Drawable drawable) {
        img.setImageDrawable(drawable);
    }

    public void setTextColor(int color){
        text.setTextColor(color);
    }
}
