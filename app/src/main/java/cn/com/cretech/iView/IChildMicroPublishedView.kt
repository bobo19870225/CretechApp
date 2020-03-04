package cn.com.cretech.iView

import android.widget.TextView
import cn.com.cretech.mvpbase.BaseView
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView
import cn.com.cretech.widget.NestedGridView

interface IChildMicroPublishedView : BaseView {

    fun setPullRefreshLayout() : PullToRefreshScrollView
    fun setGridView() : NestedGridView
    fun setTvCount() : TextView

}