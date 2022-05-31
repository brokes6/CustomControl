package com.juguo.libbasecoreui.mvvm.model

import androidx.annotation.IntDef

/**
 * Author: 付鑫博
 * Date: 2021/10/11
 * Description: 底部导航栏展示模式
 */
class TextDisPlayModel {

    @IntDef(
        TEXT_DISPLAY.RED_DOT,
        TEXT_DISPLAY.HINT_TEXT
    )
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TEXT_DISPLAY {
        companion object {
            const val NOR = 0

            const val RED_DOT = 1

            const val HINT_TEXT = 2
        }
    }

}