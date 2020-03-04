package cn.com.cretech.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerTitleStrip
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.childfragment.ChildLiveFragment
import cn.com.cretech.adapter.LivePagerStripAdapter
import cn.com.cretech.iModel.ILiveModel
import cn.com.cretech.iView.ILiveView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.model.LiveModel
import cn.com.cretech.widget.PagerSlidingTabStrip
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LivePresenter(fragmentManager: FragmentManager) : BasePresenter(fragmentManager)