package cn.com.cretech.iView
import cn.com.cretech.mvpbase.BaseView
import com.tmall.ultraviewpager.UltraViewPager

interface IChildDynamicMicroView : BaseView {

    fun setUltraViewPager() : UltraViewPager
}