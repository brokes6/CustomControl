package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gcssloop.widget.RCRelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 其实就是对几个控件的组合，方便使用而已
 */
public class CustomChannelView extends RelativeLayout {
    private static final String TAG = "CustomChannelView";
    private Context mContext;
    private View view;
    private ImageView topImage;
    private CircleImageView middleImage;
    private TextView title, time;
    private RCRelativeLayout CC_Image;

    public CustomChannelView(Context context) {
        this(context, null);
    }

    public CustomChannelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomChannelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_channel_view, this, true);
        topImage = view.findViewById(R.id.CC_topImage);
        middleImage = view.findViewById(R.id.CC_middleImage);
        title = view.findViewById(R.id.CC_Title);
        time = view.findViewById(R.id.CC_Time);
        CC_Image = view.findViewById(R.id.CC_Image);

        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CustomChannelView);
        setTopUrl(array.getString(R.styleable.CustomChannelView_Ch_TopUrl));
        setThemeText(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Text));
        setThemeTime(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Time));
        setMiddleUrl(array.getString(R.styleable.CustomChannelView_Ch_middle_Url));
        setImageRound(array.getInt(R.styleable.CustomChannelView_Ch_ImageRadius, 15));
        array.recycle();
    }

    public void setTopUrl(String url) {
        if (url == null) {
            topImage.setBackgroundColor(getResources().getColor(R.color.Grey_9));
            Log.e(TAG, "当前url为空,自动设置纯色背景 ");
        } else {
            Glide.with(mContext).load(url).into(topImage);
        }
    }

    public void setThemeText(String text) {
        title.setText(text);
    }

    public void setThemeTime(String textTime) {
        time.setText(textTime);
    }

    public void setMiddleUrl(String url) {
        if (url != null) {
            Glide.with(mContext).load(url).into(middleImage);
        }
    }


    public void setImageRound(int value) {
        CC_Image.setRadius(value);
    }
}
