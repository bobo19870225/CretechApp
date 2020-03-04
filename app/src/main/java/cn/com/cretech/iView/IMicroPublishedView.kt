package cn.com.cretech.iView

import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.mvpbase.BaseView
import cn.com.cretech.widget.PagerSlidingTabStrip
import com.baoyz.widget.PullRefreshLayout

interface IMicroPublishedView : BaseView{

      fun setPagerSlidingTabStrip() : PagerSlidingTabStrip
      fun setViewPager() : ViewPager
      
}