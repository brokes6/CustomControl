package com.example.customcontrollibs.bottom.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.customcontrollibs.R
import com.example.customcontrollibs.extensions.*
import com.juguo.libbasecoreui.mvvm.listener.OnTabClickListener
import com.juguo.libbasecoreui.mvvm.model.AnimateState
import com.juguo.libbasecoreui.mvvm.model.TabModel
import com.juguo.libbasecoreui.mvvm.model.TextDisPlayModel
import java.lang.ref.SoftReference

/**
 * Author: fuxinbo

 * Date: 2021/9/27

 * Description: 底部导航栏

 * 1.自带动画缩放效果(可关闭)

 * 2.自带重复点击回调

 * 3.自带点击截流

 * 4.自带文本加粗与设置背景效果

 * 5.支持中间大图效果

 * 6.支持重复点击播放刷新动画(已删除,需要配合Lottie使用)

 * 7.支持红点提醒与文本提醒(可配置)
 */
class BottomTabLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attributes, defStyleAttr) {

    private var TAG = BottomTabLayout::class.java.simpleName

    private var mContext: SoftReference<Context>? = null

    private var tabList: MutableList<TabModel> = mutableListOf()

    private var mViewPager: ViewPager2? = null

    private var currentTabView: View? = null // 当前展示的Tab

    private var currentIndex: Int = 0// 当前显示的item索引

    private var icon: Int = -1 // 当前Tab的Icon

    private var interceptTime: Long = 500// 多次点击之间的拦截时间

    private var ifChangeSpecification = false// 是否在中间显示比例更大的图片item

    private var onClickListener: OnTabClickListener? = null// item点击回调

    private var refreshState: Int = AnimateState.ANIMATE_STATE.ON_REFRESH_END // 当前item的刷新状态

    private var isEnableSwitchPage: Boolean = true// 刷新时是否可以切换item

    private var enabledRefresh: MutableList<Int>? = mutableListOf(0)// 可以重复点击刷新的item项

    private var refreshIndex: Int = 0// 当前正在动画的索引

    private var scalingRatio: Float = 0.2f// 缩放比例

    private var defaultDisplayPage: Int = 0// 默认展示页面索引

    private var textBold: Boolean = false // Tab文字是否加粗

    private var textAnimator: ViewPropertyAnimator? = null // 当前文本的属性动画

    private var imageAnimator: ViewPropertyAnimator? = null // 当前图片的属性动画

    private var viewWeight: Float = 1.3f // 中间大图效果的权重

    private var informationItem: Int = -1 // 红点模式的索引值

    private var mTextDisplayModel: Int = TextDisPlayModel.TEXT_DISPLAY.NOR // 当前Tab的展示效果

    private var mAnimationEffect: Boolean = true // 是否开启文本动画

    private var mNormalColor: Int = -1 // 未选中文本颜色

    private var mSelectColor: Int = -1 // 选中文本颜色

    private var mIsUserIcon: Boolean = false // 是否使用了Icon

    private var mLayoutId: Int = R.layout.layout_item_tab

    init {
        mContext = SoftReference(context)
    }

    /**
     * 设置Tab数据
     *
     * @param tabList tab数据
     * @return
     */
    fun setTabData(tabList: MutableList<TabModel>) {
        orientation = HORIZONTAL
        this.tabList = tabList
    }

    /**
     * 设置ViewPager2
     *
     * @param viewPager
     */
    fun setViewPager(viewPager: ViewPager2) {
        mViewPager = viewPager
    }

    /**
     * 设置用于中间大图模式下的权重
     *
     * @param value Tab权重
     */
    fun setViewWeight(value: Float) {
        viewWeight = value
    }

    /**
     * 设置item点击的间隔时间，用于防止多次点击
     *
     * @param intercept 默认500ms
     * @return
     */
    fun setInterceptTime(intercept: Long) {
        interceptTime = intercept
    }

    /**
     * 设置点击文字时的缩放比例
     *
     * @param value 默认0.2f
     * @return
     */
    fun setScalingRatio(value: Float) {
        scalingRatio = value
    }

    /**
     * 设置能够再次点击刷新的item项
     *
     * @param list 可以被重复点击刷新的item项(默认索引0)
     * @param isEnableSwitchPage 是否允许刷新状态中能够切换页面(依据refreshState来判断,默认打开)
     * @return
     */
    fun setEnabledRefreshItem(
        list: MutableList<Int>?,
        isEnableSwitchPage: Boolean = true,
    ) {
        if (list?.isNullOrEmpty() == true) {// 表示不需要点击重复刷新
            enabledRefresh?.clear()
            return
        }
        enabledRefresh = list
        this.isEnableSwitchPage = isEnableSwitchPage
    }

    /**
     * 设置点击监听
     *
     * @param onClickListener 监听
     * @return
     */
    fun setOnClickListener(onClickListener: OnTabClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * 设置Tab使用的布局(布局内ID请保持一致)
     *
     * @param layout
     */
    fun setLayoutId(layout: Int) {
        mLayoutId = layout
    }

    /**
     * 设置刷新图标
     *
     * @param res Drawable
     * @return
     */
    fun setRefreshIcon(@DrawableRes res: Int) {
        icon = res
    }

    /**
     * 设置默认页面
     *
     * @param value 默认展示页面的索引
     * @return
     */
    fun setDefaultPage(value: Int) {
        defaultDisplayPage = value
    }

    /**
     * 设置是否开启中间的活动图片，
     * 需要在setTabData()传入图片信息，
     * 会将中间的item变为图片展示
     *
     * @return
     */
    fun setActivityTab() {
        ifChangeSpecification = true
    }

    /**
     * 设置底部导航栏文字是否加粗
     *
     * @param textBold 文字是否加粗
     * @return
     */
    fun setTextBoldStyle(textBold: Boolean) {
        this.textBold = textBold
    }

    /**
     * 是否显示文字的动画效果
     *
     * @param isShow
     */
    fun setAnimationEffect(isShow: Boolean) {
        mAnimationEffect = isShow
    }

    /**
     * 设置文字额外的提醒模式
     * /ps:目前不能与中间活动图片模式共存
     *
     * @param item 给哪一个item设置
     * @param model 设置的显示模式（TextDisPlayModel）
     * @return
     */
    fun setItemInformation(item: Int, model: Int) {
        informationItem = item
        mTextDisplayModel = model
    }

    /**
     * 设置背景颜色
     */
    fun setBgColor(@ColorInt color: Int) {
        this.setBackgroundColor(color)
    }

    /**
     * 设置背景图片
     *
     * @param resource
     */
    fun setBgResource(resource: Int) {
        this.setBackgroundResource(resource)
    }

    /**
     * 设置文字颜色
     *
     * @param normalColor 未选中的颜色
     * @param selectColor 选中的颜色
     */
    fun setTextColor(@ColorInt normalColor: Int, @ColorInt selectColor: Int) {
        mNormalColor = normalColor
        mSelectColor = selectColor
    }

    /**
     * 切换Tab
     *
     * @param position 需要切换到Tab的索引
     */
    fun switchTab(position: Int) {
        if (position >= tabList.size || position < 0) {
            Log.e(TAG, "传入的Position无效，请检查后再试")
            return
        }
        switchStatus(position)
    }

    /**
     * 获取当前item的索引
     *
     * @return
     */
    fun getIndex(): Int {
        return currentIndex
    }

    /**
     * 构建,最后必须调用
     *
     */
    fun build() {
        check(tabList.isNotEmpty()) {
            NullPointerException("TabView为空，无法初始化！")
        }
        initColor()

        var index = 0
        tabList.forEach { data ->
            val tabView =
                LayoutInflater.from(mContext?.get()).inflate(R.layout.layout_item_tab, this, false)
            tabView.apply {
                id = index
                layout_width = 0
                layout_height = ViewGroup.LayoutParams.MATCH_PARENT
                layout_weight = 1f
                // 展示图片
                if (data.iconNormal != null) {
                    mIsUserIcon = true
                    findViewById<ImageView>(R.id.iv_top_icon)?.run {
                        visibility = VISIBLE
                        setImageDrawable(data.iconNormal)
                    }
                }
                // 展示文字
                findViewById<TextView>(R.id.tab_name)?.run {
                    text = data.title
                    textStyleBold = textBold
                    setTextColor(mNormalColor)
                }
                // 中间大图效果
                if (index == tabList.size / 2 && ifChangeSpecification && data.iconNormal != null) {
                    layout_weight = viewWeight
                    findViewById<ImageView>(R.id.tab_activity)?.run {
                        visibility = VISIBLE
                        setImageDrawable(data.iconNormal)
                    }
                }
            }
            // 额外的提醒模式
            if (index == informationItem && !ifChangeSpecification) {
                tabView.findViewById<BadgeTextView>(R.id.tab_name).apply {
                    setDisPlayModel(mTextDisplayModel)
                    setShow(true)
                }
                if (mTextDisplayModel == TextDisPlayModel.TEXT_DISPLAY.HINT_TEXT) {
                    tabView.layout_weight = viewWeight
                }
            }
            // 设置限流的点击事件
            tabView.onClick({
                switchStatus(tabView.id)
            })
            addView(tabView)
            index++
        }

        // 默认选中项
        defaultDisplayPage.takeIf {
            it <= tabList.size - 1
        }?.run {
            getChildAt(defaultDisplayPage).run {
                currentIndex = defaultDisplayPage
                currentTabView = this
                isSelected = true
                if (mIsUserIcon) {
                    findViewById<ImageView>(R.id.iv_top_icon)?.setImageDrawable(tabList[defaultDisplayPage].iconChecked)
                }
                findViewById<TextView>(R.id.tab_name)?.setTextColor(
                    mSelectColor
                )
            }
        }
        mViewPager?.setCurrentItem(defaultDisplayPage, false)
    }

    /**
     * 当没有设置文字颜色时,将自动进行颜色初始化
     *
     */
    private fun initColor() {
        if (mNormalColor == -1 || mSelectColor == -1) {
            mContext?.get()?.let {
                mNormalColor = ContextCompat.getColor(it, R.color.bt_white_2)
                mSelectColor = ContextCompat.getColor(it, R.color.bt_black_2)
            }
        }
    }

    /**
     * 内部响应点击事件，用于控制UI
     *
     * @param viewId
     */
    private fun switchStatus(viewId: Int) {
        currentTabView?.isSelected = false
        if (!isEnableSwitchPage) {// 用于点击刷新时，禁止切换页面的拦截
            if (refreshState == AnimateState.ANIMATE_STATE.ON_REFRESH_START) {// 判断是否在刷新
                Log.i(TAG, "正在刷新，禁止切换页面")
                return
            }
        }
        onClickListener?.onClick(currentIndex)
        if (viewId == informationItem && !ifChangeSpecification) {
            getChildAt(informationItem).layout_weight = 1f
            getChildAt(informationItem).findViewById<BadgeTextView>(R.id.tab_name).setShow(false)
        }

        // 上次选中的item是本身，那么直接回调刷新接口
        if (currentTabView?.id == viewId) {
            if (enabledRefresh?.contains(currentTabView?.id) == true && refreshState == AnimateState.ANIMATE_STATE.ON_REFRESH_END) {
                onClickListener?.onRefresh(currentIndex)
            }
        } else {
            // 不是本身，就代表着需要将之前的item通过动画恢复颜色，还需要将当前选中的item通过动画，改变颜色
            changeIcon(viewId)
            changTextColor(currentTabView?.findViewById(R.id.tab_name), false)
            changTextColor(findViewById<View>(viewId).findViewById(R.id.tab_name), true)
            mViewPager?.setCurrentItem(viewId, false)
            currentTabView = findViewById(viewId)
        }
        currentTabView?.isSelected = true
        Log.d(TAG, "当前页面索引为${viewId}")
        currentIndex = viewId
    }

    /**
     * 改变TabView的图标
     */
    private fun changeIcon(viewId: Int) {
        if (!mIsUserIcon) {
            Log.d(TAG, "未使用图标，无法进行更换")
            return
        }
        // 将新选中的Image改为使用选中图标
        findViewById<View>(viewId).findViewById<ImageView>(R.id.iv_top_icon)
            .setImageDrawable(tabList[viewId].iconChecked)
        // 将之前的Image改为正常图标
        currentTabView?.findViewById<ImageView>(R.id.iv_top_icon)
            ?.setImageDrawable(tabList[currentIndex].iconNormal)
    }

    /**
     * 开始刷新(供外部调用)
     *
     */
    fun onRefresh() {
        // 只有在刷新结束的情况下才可以重新进行刷新

    }

    /**
     * 刷新完成(供外部调用)
     *
     */
    fun refreshSuccess() {

    }

    /**
     * 通过动画改变字体颜色
     *
     * @param textView
     * @param isSelect
     */
    private fun changTextColor(textView: TextView?, isSelect: Boolean) {
        if (textView == null) {
            Log.e(TAG, "未找到指定的View")
            return
        }
        var animators: ObjectAnimator? = null
        if (!isSelect) {
            if (mAnimationEffect) {
                animators = ObjectAnimator.ofArgb(
                    textView,
                    "textColor",
                    mSelectColor,
                    mNormalColor
                )
            } else {
                textView.setTextColor(mNormalColor)
            }
        } else {
            if (mAnimationEffect) {
                enlarge(textView)
                animators = ObjectAnimator.ofArgb(
                    textView,
                    "textColor",
                    mNormalColor,
                    mSelectColor
                )
            } else {
                textView.setTextColor(mSelectColor)
            }
        }
        animators?.start()
    }

    /**
     * 通过动画将文字放大再缩小
     *
     * @param textView
     */
    private fun enlarge(textView: TextView?) {
        textView?.let {
            it.animate().scaleYBy(scalingRatio).scaleXBy(scalingRatio).withLayer().withEndAction {
                it.animate().scaleYBy(-scalingRatio).scaleXBy(-scalingRatio).withLayer()
            }
        }
    }
}