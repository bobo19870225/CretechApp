package cn.com.cretech.iView

import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.mvpbase.BaseView
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView

interface IDynamicSchoolView   : BaseView {

    fun setRecyclerView() : RecyclerView
    fun setPullToRefreshScrollView() : PullToRefreshScrollView

}