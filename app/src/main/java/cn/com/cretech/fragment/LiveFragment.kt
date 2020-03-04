package cn.com.cretech.fragment

import androidx.viewpager.widget.ViewPager
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentLiveBinding
import cn.com.cretech.iView.ILiveView
import cn.com.cretech.presenter.LivePresenter

/**
 * 直播
 */

class LiveFragment : MVPBaseFragment<LivePresenter , FragmentLiveBinding>() {

    override fun setLayoutRes(): Int {

        return R.layout.fragment_live
    }

    override fun initView() {
        presenter = LivePresenter( this@LiveFragment.childFragmentManager)
        presenter.setLiveViewData(binDing.pagerSliding,binDing.viewPager)
    }

}