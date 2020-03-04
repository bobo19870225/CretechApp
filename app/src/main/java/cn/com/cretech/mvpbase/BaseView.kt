package cn.com.cretech.mvpbase

import android.widget.LinearLayout
import android.widget.TextView

interface BaseView {

    fun setLinearLayout() : LinearLayout
    fun setNetWorkError() : TextView
    fun setNetWorkClick() : TextView
}