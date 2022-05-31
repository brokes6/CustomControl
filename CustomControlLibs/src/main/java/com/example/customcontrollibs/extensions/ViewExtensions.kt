package com.example.customcontrollibs.extensions

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * Author: 付鑫博
 * Date: 2021/8/9
 * Description:
 */

/**
 * 设置控件Margins
 * @param l 左边距
 * @param t 上边距
 * @param r 右边距
 * @param b 下边距
 */
fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        (this.layoutParams as ViewGroup.MarginLayoutParams).setMargins(l, t, r, b)
        this.requestLayout()
    }
}

fun View.toGONE() {
    this.visibility = View.GONE
}

fun View.toVISIBLE() {
    this.visibility = View.VISIBLE
}

inline fun Context.View(init: View.() -> Unit) =
    View(this).apply(init)

/**
 * 内联函数，设置View ID
 */
inline var View.layout_id: String
    get() {
        return ""
    }
    set(value) {
        id = value.toLayoutId()
    }

inline var View.layout_width: Int
    get() {
        return 0
    }
    set(value) {}

inline var View.layout_height: Int
    get() {
        return 0
    }
    set(value) {}

inline var View.background_color: Int
    get() {
        return 0
    }
    set(value) {
        setBackgroundColor(value)
    }

inline var View.layout_visibility: Int
    get() {
        return -1
    }
    set(value) {
        visibility = value
    }

inline var View.layout_weight: Float
    get() {
        return 0f
    }
    set(value) {
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, value)
    }

inline var TextView.textStyleBold: Boolean
    get() {
        return typeface.style == Typeface.BOLD
    }
    set(value) {
        typeface =
            if (value) Typeface.defaultFromStyle(Typeface.BOLD) else Typeface.defaultFromStyle(
                Typeface.NORMAL
            )
    }

/**
 * 将点击事件转换为Flow流
 *
 */
fun View.clickFlow() = callbackFlow {
    setOnClickListener { trySend(Unit) }
    awaitClose { setOnClickListener(null) }
}

/**
 * 限流发送
 *
 * @param T
 * @param thresholdMillis
 * @return
 */
fun <T> Flow<T>.throttleFirst(thresholdMillis: Long): Flow<T> = flow {
    var lastTime = 0L // 上次发射数据的时间
    // 收集数据
    collect { upstream ->
        // 当前时间
        val currentTime = System.currentTimeMillis()
        // 时间差超过阈值则发送数据并记录时间
        if (currentTime - lastTime > thresholdMillis) {
            lastTime = currentTime
            emit(upstream)
        }
    }
}

/**
 * 限流点击事件
 *
 * @param operate 点击操作
 * @param interceptTime 限流时间
 * @receiver
 */
fun View.onClick(operate: () -> Unit = {}, interceptTime: Long = 500) {
    this.clickFlow()
        .throttleFirst(interceptTime)
        .onEach {
            operate()
        }.launchIn(MainScope())
}

fun String.toLayoutId(): Int {
    var id = java.lang.String(this).bytes.sum()
    if (id == 48) id = 0
    return id
}

fun <T : View> View.find(id: String): T? = findViewById(id.toLayoutId())

/**
 * 将EditText文本变化转变为Flow流进行输出
 *
 * @return
 */
fun EditText.textChangFlow(): Flow<CharSequence> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { trySend(it) }
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    addTextChangedListener(watcher)
    // 当通道关闭时，进行的操作
    awaitClose { removeTextChangedListener(watcher) }
}

/**
 * 限流搜索
 *
 * @param onSearch 进行搜索
 * @param onChange 改变文本内容
 * @receiver
 */
fun EditText.textChangListener(
    onChange: (String) -> Unit = {},
    interceptTime: Long = 300,
) {
    this.textChangFlow()
        .filter { it.isNotEmpty() } // 中间流，用于判断.判断成立才发射
        .debounce(interceptTime) // 中间流，用于拦截，只有超过指定时间的流才会被发射，其余将拦截
        .flatMapConcat { flow { emit(it.toString()) } } // 中间生产者，接收上流的值并进行处理，任何产生下流
        .flowOn(Dispatchers.IO) // 指定流所处的线程
        .onEach { onChange(it) } // 流接收者
        .launchIn(MainScope())
}