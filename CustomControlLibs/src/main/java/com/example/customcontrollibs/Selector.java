package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class Selector extends FrameLayout implements View.OnClickListener {
    private OnSelectorStateListener stateListener;
    private String tag;
    private SelectorGroup selectorGroup;

    public Selector(Context context) {
        super(context);
        initView(context, null);
    }

    public Selector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public Selector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = onCreateView();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(view, params);
        this.setOnClickListener(this);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Selector);
            int iconResId = typedArray.getResourceId(R.styleable.Selector_img, 0);
            int selectorResId = typedArray.getResourceId(R.styleable.Selector_indicator, 0);
            tag = typedArray.getString(R.styleable.Selector_tag);
            onBindView(iconResId,selectorResId);
            typedArray.recycle();
        }
    }

    public Selector setSelectorGroup(SelectorGroup selectorGroup) {
        this.selectorGroup = selectorGroup;
        selectorGroup.addSelector(this);
        return this;
    }

    protected abstract void onBindView( int iconResId, int indicatorResId);

    protected abstract View onCreateView();

    public String getTag() {
        return tag;
    }

    public Selector setOnSelectorStateListener(OnSelectorStateListener stateListener) {
        this.stateListener = stateListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        boolean isSelect = switchSelector();
        //单选按钮将选中状态告诉单选组
        if (selectorGroup != null) {
            selectorGroup.setSelected(this);
        }
        if (stateListener != null) {
            stateListener.onStateChange(this, isSelect);
        }
    }

    public boolean switchSelector() {
        boolean isSelect = this.isSelected();
        this.setSelected(!isSelect);
        onSwitchSelected(!isSelect);
        return !isSelect;
    }

    protected abstract void onSwitchSelected(boolean isSelect);

    //利用tag生成哈希码，遂每个单选按钮的tag需保证唯一
    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Selector) {
            return ((Selector) obj).tag.equals(this.tag);
        }
        return false;
    }

    public interface OnSelectorStateListener {
        void onStateChange(Selector selector, boolean isSelect);
    }
}