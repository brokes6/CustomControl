package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 其实就是对几个控件的组合，方便使用而已
 */
public class CustomChannelView extends RelativeLayout {
    private static final String TAG = "CustomChannelView";
    private Context mContext;
    private View view;
    private ImageView topImage,ImageBaffle;
    private CircleImageView middleImage;
    private TextView title, time;
    private CardView CC_Image;
    private int mTopImageHeight;
    private int mPx;
    private boolean mIsOcclusion;

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
        ImageBaffle = view.findViewById(R.id.baffle);
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
        setTopDrawable(array.getDrawable(R.styleable.CustomChannelView_Ch_Top_Drawable));
        IsOcclusion_on(array.getBoolean(R.styleable.CustomChannelView_CH_Is_occlusion_on,true));
        setThemeText(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Text));
        setThemeTime(array.getString(R.styleable.CustomChannelView_Ch_Bottom_Time));
        setImageRound(array.getInt(R.styleable.CustomChannelView_Ch_ImageRadius, 15));
        setMiddleImageSize(array.getInt(R.styleable.CustomChannelView_Ch_Middle_Image_Size,50));
        setBottomTextSize(array.getDimension(R.styleable.CustomChannelView_CH_Bottom_Size,8));
        setBottomTimeSize(array.getDimension(R.styleable.CustomChannelView_CH_Bottom_TimeSize,6));
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

    public void setTopDrawable(Drawable d){
        topImage.setImageDrawable(d);
    }

    public void setThemeText(String text) {
        title.setText(text);
    }

    public void setThemeTime(String textTime) {
        time.setText(textTime);
    }

    public void IsOcclusion_on(boolean value){
        mIsOcclusion = value;
    }

    public void setBottomTextSize(float value) {
        title.setTextSize(value);
    }

    public void setBottomTimeSize(float value) {
        time.setTextSize(value);
    }

    public boolean IsOcclusion(){
        return mIsOcclusion;
    }

    public void setMiddleUrl(String url) {
        if (url != null) {
            Glide.with(mContext).load(url).into(middleImage);
            if (mIsOcclusion){
                Glide.with(mContext)
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                PaletteHelper.setPaletteColor(resource, ImageBaffle);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
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
