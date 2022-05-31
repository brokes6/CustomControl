package com.juguo.libbasecoreui.mvvm.listener

/**
 * Author: 付鑫博
 * Version: 1.18.0
 * Date: 2021/9/28
 * Mender:
 * Modify:
 * Description:
 */
interface OnTabClickListener {
    fun onClick(position: Int)

    fun onRefresh(position: Int)
}