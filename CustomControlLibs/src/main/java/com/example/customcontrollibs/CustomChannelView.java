package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

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
    private CardView CC_Image;
    private View ImageView;
    private int mTopImageHeight;
    private int mPx;

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

        topImage.post(new Runnable() {
            @Override
            public void run() {
                mTopImageHeight = topImage.getMeasuredHeight();
                setMiddleImagePosition(mTopImageHeight);
            }
        });
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CustomChannelView);
        setTopUrl(array.getString(R.styleable.CustomChannelView_Ch_TopUrl));
        setThemeText(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Text));
        setThemeTime(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Time));
        setMiddleUrl(array.getString(R.styleable.CustomChannelView_Ch_middle_Url));
        setImageRound(array.getInt(R.styleable.CustomChannelView_Ch_ImageRadius, 15));
        setMiddleImageSize(array.getInt(R.styleable.CustomChannelView_Ch_Middle_Image_Size,50));
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

    public void setMiddleImageSize(int value){
        mPx = DensityUtil.dip2px(mContext, value);
        //将用户输入的数据转换为dp
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) middleImage.getLayoutParams();
        //取控件imageView当前的布局参数 linearParams.height/width = value;// 控件的高强制设成用户设置的
        linearParams.width = mPx;
        linearParams.height = mPx;
        // 控件的宽强制设成mPx
        middleImage.setLayoutParams(linearParams);
    }

    private void setMiddleImagePosition(int value){
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) middleImage.getLayoutParams();
//        Log.e(TAG, "setMiddleImagePosition: 图像大小为:"+ mPx+"topImage大小为"+value+"图像一半为"+(mPx/2)+"最后结果为:"+(value-(mPx/2)));
        linearParams.topMargin = (value-(mPx/2));
        middleImage.setLayoutParams(linearParams);
    }

    public void setImageRound(int value) {
        CC_Image.setRadius(value);
    }
}
