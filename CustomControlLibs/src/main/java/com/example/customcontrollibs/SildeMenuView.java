package com.example.customcontrollibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class SildeMenuView extends ViewGroup implements View.OnClickListener {
    private Context mContext;
    private View mContextView;
    private View mEditView;
    private OnEditViewClickListener mOnEditViewClickListener;
    private TextView mReadView;
    private TextView mTopView;
    private TextView mDeleteView;
    private float mDownx;
    private float mDowny;
    private Scroller mScroller;
    private Boolean isOpen = false;
    private Direction mCurrentDirect = Direction.NONE;
    private int mDirection = 500;
    private float mPressx;
    private float mPressy;

    enum Direction {
        LEFT, RIGHT, NONE
    }

    public SildeMenuView(Context context) {
        this(context, null);
    }

    public SildeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SildeMenuView);
        int function = a.getInt(R.styleable.SildeMenuView_function, 0x30);
        a.recycle();
        mScroller = new Scroller(context);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {//只能允许有一个子类
            throw new IllegalArgumentException("There can only be 1 subclass");
        }
        mContextView = getChildAt(0);
        //获取编辑区域
        mEditView = LayoutInflater.from(mContext).inflate(R.layout.itme_silde_action, this, false);
        initEditView();
        this.addView(mEditView);
    }

    private void initEditView() {
        mReadView = mEditView.findViewById(R.id.read_tv);
        mTopView = mEditView.findViewById(R.id.top_tv);
        mDeleteView = mEditView.findViewById(R.id.delete_tv);

        mReadView.setOnClickListener(this);
        mTopView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contextHeightMeasure;
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//内容部分的高度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取内容部分的宽度
        LayoutParams mContextViewLayoutParams = mContextView.getLayoutParams();
        int contextHeight = mContextViewLayoutParams.height;//获取内容部分View的高度模式
        if (contextHeight == LayoutParams.MATCH_PARENT) {
            contextHeightMeasure = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (contextHeight == LayoutParams.WRAP_CONTENT) {
            contextHeightMeasure = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        } else {
            contextHeightMeasure = MeasureSpec.makeMeasureSpec(contextHeight, MeasureSpec.EXACTLY);
        }
        //测量内容部分的高度和宽度
        mContextView.measure(widthMeasureSpec, contextHeightMeasure);
        //最后获取总的高度
        int measuredHeight = mContextView.getMeasuredHeight();
        //获取编辑部分的宽度（为内容部分的3/5）
        int functionWidth = widthSize * 3 / 5;
        //测量编辑部分的高度和宽度
        mEditView.measure(MeasureSpec.makeMeasureSpec(functionWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
        //测量本身
        setMeasuredDimension(widthSize + functionWidth, measuredHeight);

    }

    /**
     * 拦截点击事件，如果在滑动就拦，给自己消费
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case ACTION_DOWN:
                mPressx = ev.getX();
                mPressy = ev.getY();
                break;
            case ACTION_MOVE:
                float x = ev.getX();
                if (Math.abs(x - mPressx) > 0) {
                    //只要移动了，就拦截事件，给自己消费
                    return true;
                }
                break;
            case ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case ACTION_DOWN:
                mDownx = event.getX();
                mDowny = event.getY();
                break;
            case ACTION_MOVE:
                int scrollX = getScrollX();
                float MoveX = event.getX();
                float MoveY = event.getY();
                int dx = (int) (MoveX - mDownx);
                if (dx > 0) {//但是dx向左移动为整数，向右移动为负数，所以相反（与滑动方向相反）
                    mCurrentDirect = Direction.RIGHT;
                } else {
                    mCurrentDirect = Direction.LEFT;
                }
                //判断边界值（向左移动是负数，向右移动为整数）
                //判断向左是否超过0
                if ((-dx + scrollX) <= 0) {
                    scrollTo(0, 0);
                    //判断向右是否超过 编辑区域的宽度
                } else if ((-dx + scrollX) > mEditView.getMeasuredWidth()) {
                    //设置屏幕位置
                    scrollTo(mEditView.getMeasuredWidth(), 0);
                } else {
                    //移动屏幕
                    scrollBy(-dx, 0);
                }
                mDownx = MoveX;
                mDowny = MoveY;
                break;
            case ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                int mScrollX = getScrollX();
                if (isOpen) {
                    //打开模式
                    if (mCurrentDirect == Direction.RIGHT) {
                        //向右滑动
                        if (mScrollX < mEditView.getMeasuredWidth() * 3 / 4) {
                            closeView();
                        } else {
                            openView();
                        }
                    } else if (mCurrentDirect == Direction.LEFT) {
                        openView();
                    }
                } else {
                    //关闭模式
                    if (mCurrentDirect == Direction.LEFT) {
                        //向左滑动
                        if (mScrollX > mEditView.getMeasuredWidth() / 4) {
                            openView();
                        } else {
                            closeView();
                        }

                    } else if (mCurrentDirect == Direction.RIGHT) {
                        //向右滑动
                        closeView();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 打开编辑区域
     */
    public void openView() {
        int dx = mEditView.getMeasuredWidth() - getScrollX();
        int duration = (int) (dx / (mEditView.getMeasuredWidth() * 4 / 5f) * mDirection);
        //开始进行长度插值（初始X，初始Y，当前X，当前Y，持续时间）
        mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(duration));
        invalidate();
        isOpen = true;
    }

    /**
     * 关闭编辑区域
     */
    public void closeView() {
        int dx = -getScrollX();
        int duration = (int) (dx / (mEditView.getMeasuredWidth() * 4 / 5f) * mDirection);
        //开始进行长度插值（初始X，初始Y，当前X，当前Y，持续时间）
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, Math.abs(duration));
        invalidate();
        isOpen = false;
    }

    /**
     * 所有的插值都会传到这边
     * 插值就是指：假设100的距离被平均分为（10，20，30，40...）传递过来
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //更具插值来移动屏幕
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //获取内容高宽（就是确认4点坐标）
        int mContextLeft = 0;
        int contextTop = 0;
        int contextRight = mContextLeft + mContextView.getMeasuredWidth();
        int contextBottom = contextTop + mContextView.getMeasuredHeight();
        //设置内容的部分（就是确认4点坐标）
        mContextView.layout(mContextLeft, contextTop, contextRight, contextBottom);
        int editViewLeft = contextRight;
        int editViewTop = contextTop;
        int editViewRight = editViewLeft + mEditView.getMeasuredWidth();
        int editViewBottom = editViewTop + mEditView.getMeasuredHeight();
        //设置编辑区域的部分
        mEditView.layout(editViewLeft, editViewTop, editViewRight, editViewBottom);

    }

    public void setEditViewOnClickListener(OnEditViewClickListener listener) {
        mOnEditViewClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mOnEditViewClickListener == null) {
            return;
        }
        if (view == mReadView) {
            mOnEditViewClickListener.onReadClick();
        } else if (view == mTopView) {
            mOnEditViewClickListener.onTopClick();
        } else if (view == mDeleteView) {
            mOnEditViewClickListener.onDeleteClick();
        }
        closeView();
    }

    public interface OnEditViewClickListener {
        void onReadClick();

        void onTopClick();

        void onDeleteClick();

    }

    /**
     * 获取当前function的打开情况
     *
     * @return
     */
    public Boolean getOpen() {
        return isOpen;
    }

    /**
     * 获取当前动画的持续时间
     *
     * @return
     */
    public int getAnimationTime() {
        return mDirection;
    }

    /**
     * 设置动画的持续时间
     *
     * @param value
     */
    public void setAnimationTime(int value) {
        this.mDirection = value;
    }

    /**
     * 设置EditView的布局
     *
     * @param v
     */
    public void setEditView(View v) {
        mEditView = v;
        initEditView();
        this.addView(mEditView);
    }
}
