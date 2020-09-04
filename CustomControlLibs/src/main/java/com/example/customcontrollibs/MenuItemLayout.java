package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MenuItemLayout extends FrameLayout {
    private static final String TAG = "MenuItemLayout";
    private Context mcontext;
    private View view;
    private TextView main_text, hint_text;
    private ImageView text_img, more, more_right;
    private OnClickListener onClickListener;
    private String titleText;
    private String hintText;
    private int textImgId;
    private String onclickId;
    private LinearLayout linearLayout;
    public static final int NO_LINE = 0;
    public static final int DIVIDE_LINE = 1;
    public static final int DIVIDE_AREA = 2;
    public int divideLineStyle = NO_LINE;
    private boolean isShowRedHintImg = false;

    public MenuItemLayout(@NonNull Context context) {
        this(context, null);
    }

    public MenuItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mcontext = context;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_menu_layout, this, true);
        main_text = view.findViewById(R.id.text);
        hint_text = view.findViewById(R.id.text_hint);
        text_img = view.findViewById(R.id.text_img);
        more = view.findViewById(R.id.more);
        more_right = view.findViewById(R.id.more_right);
        linearLayout = view.findViewById(R.id.main_lin);

        TypedArray a = mcontext.obtainStyledAttributes(attrs, R.styleable.MenuItemLayout);
        setTitleText(a.getString(R.styleable.MenuItemLayout_TitleText));
        setHint_text(a.getString(R.styleable.MenuItemLayout_PromptText));
        setIconImgId(a.getResourceId(R.styleable.MenuItemLayout_TitleImg, 10000));
        isSwitchmore(a.getBoolean(R.styleable.MenuItemLayout_isSwitch, false));
        setLinBackground(a.getResourceId(R.styleable.MenuItemLayout_Background, -1));
        a.recycle();
    }

    public int getIconImgId() {
        return textImgId;
    }

    public void setIconImgId(int iconImgId) {
        if (iconImgId != 10000) {
            this.textImgId = iconImgId;
            text_img.setImageResource(iconImgId);
        } else {
            text_img.setVisibility(View.GONE);
        }
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        if (titleText != null) {
            this.titleText = titleText;
            main_text.setText(titleText);
        }
    }

    public String getHintText() {
        return hintText;
    }

    public void setHint_text(String text) {
        if (text != null) {
            this.hintText = text;
            hint_text.setText(text);
        }
    }

    public void isSwitchmore(boolean is) {
        if (is) {
            more.setVisibility(View.VISIBLE);
            more_right.setVisibility(View.GONE);
        } else {
            more.setVisibility(View.GONE);
            more_right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        linearLayout.setBackground(background);
    }

    public void setLinBackground(int reference) {
        if (reference == -1) {
            return;
        }
        linearLayout.setBackgroundResource(reference);
    }


    public void setViewOnlickListener(OnClickListener onlickListener) {
        this.onClickListener = onlickListener;
        view.setOnClickListener(onlickListener);
    }

    public TextView getTitleTv() {
        return main_text;
    }

    public TextView getHintTv() {
        return hint_text;
    }
}
