package com.example.customcontrollibs;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class AgeSelector extends Selector {
    private ImageView ivIcon;
    private ImageView ivSelector;
    private ValueAnimator valueAnimator;

    public AgeSelector(Context context) {
        super(context);
    }

    public AgeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(int iconResId, int indicatorResId) {
        //在这里将自定义布局中的控件和自定义属性值绑定
        if (ivIcon != null) {
            ivIcon.setImageResource(iconResId);
        }
        if (ivSelector != null) {
            ivSelector.setImageResource(indicatorResId);
            ivSelector.setAlpha(0);
        }
    }

    @Override
    protected View onCreateView() {
        //在这里定义你想要的布局
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.selector, null);
        ivIcon = view.findViewById(R.id.iv_icon);
        ivSelector = view.findViewById(R.id.iv_selector);
        return view;
    }

    @Override
    protected void onSwitchSelected(boolean isSelect) {
        //单选按钮状态变化时做动画
        if (isSelect) {
            playSelectedAnimation();
        } else {
            playUnselectedAnimation();
        }
    }

    private void playUnselectedAnimation() {
        if (ivSelector == null) {
            return;
        }
        if (valueAnimator != null) {
            valueAnimator.reverse();
        }
    }

    private void playSelectedAnimation() {
        if (ivSelector == null) {
            return;
        }
        valueAnimator = ValueAnimator.ofInt(0, 255);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ivSelector.setAlpha((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}