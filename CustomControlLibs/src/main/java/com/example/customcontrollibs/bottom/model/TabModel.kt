package com.juguo.libbasecoreui.mvvm.model

import android.graphics.drawable.Drawable

/**
 * Author: 付鑫博
 * Date: 2021/9/27
 * Description: 底部导航栏资源Model
 */
data class TabModel(
    val title: String,
    val iconNormal: Drawable? = null,
    val iconChecked: Drawable? = null,
    val isRepeatOnClick: Boolean = false
)
