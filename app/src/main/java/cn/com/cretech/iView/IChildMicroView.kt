package cn.com.cretech.iView

import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.mvpbase.BaseView
import cn.com.cretech.widget.NestedGridView
import com.baoyz.widget.PullRefreshLayout
import com.tmall.ultraviewpager.UltraViewPager

interface IChildMicroView : BaseView {

    fun setNestedGridView() : NestedGridView
    fun setSwipeRefreshLayout() : PullRefreshLayout
    fun setData(dataBean : ChildMicroPublishedBean.DataBean.SchoolDynamic)
}