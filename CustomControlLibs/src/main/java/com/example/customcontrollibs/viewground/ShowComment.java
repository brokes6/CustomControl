package com.example.customcontrollibs.viewground;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.customcontrollibs.DensityUtil;
import com.example.customcontrollibs.R;

public class ShowComment extends LinearLayout {
    private View view;
    private Context mContext;
    private ImageView message;
    private TextView userName, userComment;
    private boolean judge;

    public ShowComment(Context context) {
        this(context, null);
    }

    public ShowComment(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowComment(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initAttribteSet(attrs);
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_show_comment, this, true);
        message = view.findViewById(R.id.message);
        userName = view.findViewById(R.id.message_user);
        userComment = view.findViewById(R.id.message_comment);
    }

    private void initAttribteSet(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ShowComment);
        isNetWorkUrl(array.getBoolean(R.styleable.ShowComment_isNetWorkUrl, true));
        array.recycle();
    }

    public void setImageSize(int value) {
        int dp = DensityUtil.px2dip(mContext, value);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) message.getLayoutParams();
        linearParams.width = dp;
        linearParams.height = dp;
        message.setLayoutParams(linearParams);
    }

    public void isNetWorkUrl(boolean judge) {
        this.judge = judge;
    }

    public void setMessageImage(String url) {
        if (judge) {
            Glide.with(mContext).load(url).into(message);
        } else {
            message.setImageResource(R.drawable.ic_message);
        }
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    public void setUserComment(String comment) {
        userComment.setText(comment);
    }

    public void setUserNameSize(float value) {
        userName.setTextSize(value);
    }

    public void setUserCommentSize(float value) {
        userComment.setTextSize(value);
    }

    public void setUserNameColor(@ColorInt int color) {
        userName.setTextColor(color);
    }

    public void setUserCommentColor(@ColorInt int color) {
        userComment.setTextColor(color);
    }
}
