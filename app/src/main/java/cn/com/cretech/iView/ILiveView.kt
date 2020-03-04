package cn.com.cretech.iView

import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.mvpbase.BaseView
import com.baoyz.widget.PullRefreshLayout

interface ILiveView : BaseView{

      fun setSwipeRefreshLayout() : PullRefreshLayout
      fun setRecyclerView() : RecyclerView

}