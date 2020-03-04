package cn.com.cretech.iView

import android.widget.ListView

interface IKeepListView {

    fun setNetVisible(is_visible : Boolean)
    fun setListView() : ListView
    fun setCancelPull()

}