package com.juguo.libbasecoreui.mvvm.model

import androidx.annotation.IntDef

/**
 * Author: 付鑫博
 * Date: 2021/9/28
 * Description: 底部导航栏当前处于的动画模式
 */
class AnimateState {

    /**
     * 0/1/2/3 --> 准备刷新/开始刷新/刷新成功/刷新结束
     *
     */
    @IntDef(
        ANIMATE_STATE.ON_REFRESH,
        ANIMATE_STATE.ON_REFRESH_START,
        ANIMATE_STATE.ON_REFRESH_SUCCESS
    )
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ANIMATE_STATE {
        companion object {
            /**
             * 准备刷新
             */
            const val ON_REFRESH = 0

            /**
             * 开始刷新
             */
            const val ON_REFRESH_START = 1

            /**
             * 刷新成功
             */
            const val ON_REFRESH_SUCCESS = 2

            /**
             * 刷新结束
             */
            const val ON_REFRESH_END = 3
        }
    }

}