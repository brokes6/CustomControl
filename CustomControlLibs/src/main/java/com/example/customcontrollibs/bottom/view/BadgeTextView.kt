package com.example.customcontrollibs.bottom.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.customcontrollibs.R
import com.example.customcontrollibs.extensions.pxf
import com.example.customcontrollibs.extensions.sp
import com.juguo.libbasecoreui.mvvm.model.TextDisPlayModel

/**
 * Author: 付鑫博
 * Version: 1.18.0
 * Date: 2021/10/11
 * Mender:
 * Modify:
 * Description:
 */
class BadgeTextView : androidx.appcompat.widget.AppCompatTextView {

    private var mPaint: Paint = Paint()

    private var mHintPaint: Paint = Paint()

    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var redDotRadius: Float = 6f // 红点半径

    private var textSpacing: Float = 3f // 红点距离文字的距离

    private var isShow: Boolean = false // 统一的点击控制显示与隐藏

    private var currentDisplayModel = TextDisPlayModel.TEXT_DISPLAY.NOR // 当前展示模式

    private var hintBg: Int = -1 // 提示文字背景颜色

    private var hintText: String = "神魔恋" // 提醒文字

    private val mRect: Rect = Rect()

    private var textMargin: Float = 10f // 文字边距

    private var isMeasurement: Boolean = false // 是否已经测量过了文字大小

    private val mRoundRadius: Float = 20f // 圆角矩形的圆角半径

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 设置显示模式为提醒文字时的圆角矩形背景，需要在设置模式之后使用
     *
     * @param color
     */
    fun setHintBg(@ColorInt color: Int) {
        check(currentDisplayModel != TextDisPlayModel.TEXT_DISPLAY.HINT_TEXT) {
            "需要将模式设置为提醒文字模式"
        }
        hintBg = color
    }

    /**
     * 设置显示模式
     *
     * @param model 显示模式
     */
    fun setDisPlayModel(model: Int) {
        currentDisplayModel = model
        when (currentDisplayModel) {
            TextDisPlayModel.TEXT_DISPLAY.HINT_TEXT -> {
                mHintPaint.color = ContextCompat.getColor(context, R.color.white)
                mHintPaint.style = Paint.Style.FILL
                mTextPaint.textAlign = Paint.Align.CENTER
                mTextPaint.color = ContextCompat.getColor(context, R.color.black)
                mTextPaint.textSize = (8).sp.toFloat()
                mTextPaint.strokeWidth = (1).pxf
            }
            TextDisPlayModel.TEXT_DISPLAY.RED_DOT -> {
                mPaint.color = ContextCompat.getColor(context, R.color.red)
            }
        }
    }

    /**
     * 统一的控制显示开关
     *
     * @param value 是否显示
     */
    fun setShow(value: Boolean) {
        if (isShow == value) {
            return
        }
        isShow = value
        if (!isShow) {
            currentDisplayModel = TextDisPlayModel.TEXT_DISPLAY.NOR
        }
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        when (currentDisplayModel) {
            TextDisPlayModel.TEXT_DISPLAY.RED_DOT -> { // 绘制红点
                if (!isShow) {
                    return
                }
                canvas?.drawCircle(
                    getTextWidth() + textSpacing,
                    redDotRadius,
                    redDotRadius,
                    mPaint
                )
            }
            TextDisPlayModel.TEXT_DISPLAY.HINT_TEXT -> { // 绘制提醒文字+圆角边框
                if (!isShow) {
                    return
                }
                measurementTextWidth()
                val ref = RectF(
                    getTextWidth() - textMargin,
                    0f,
                    getTextWidth() + mRect.width().toFloat(),
                    mRect.height().toFloat() + textMargin
                )
                canvas?.drawRoundRect(ref, mRoundRadius, mRoundRadius, mHintPaint)
                canvas?.drawText(
                    hintText,
                    (ref.width() / 2) + getTextWidth() - textMargin,
                    ref.height() / 2 + 6, // 为什么要+6？我也不知道
                    mTextPaint
                )
            }
        }
    }

    /**
     * 获取文字的绘制宽度
     *
     * @return
     */
    private fun getTextWidth(): Float {
        val tPaint = Paint()
        tPaint.textSize = textSize
        return tPaint.measureText(text.toString())
    }

    /**
     * 根据显示模式不同，来改变测量大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (currentDisplayModel) {
            TextDisPlayModel.TEXT_DISPLAY.RED_DOT -> {
                setMeasuredDimension((measuredWidth + redDotRadius * 2).toInt(), measuredHeight)
            }
            TextDisPlayModel.TEXT_DISPLAY.HINT_TEXT -> {
                measurementTextWidth()
                setMeasuredDimension(
                    (measuredWidth + mRect.width()),
                    (measuredHeight + textMargin).toInt()
                )
            }
        }
    }

    /**
     * 获取文字绘制范围
     *
     */
    private fun measurementTextWidth() {
        if (!isMeasurement) {
            isMeasurement = true
            mTextPaint.getTextBounds(hintText, 0, hintText.length, mRect)
        }
    }

}