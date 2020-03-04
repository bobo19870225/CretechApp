package cn.com.cretech.fragment


import androidx.viewpager.widget.ViewPager
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentStyleBinding
import cn.com.cretech.iView.IMicroPublishedView
import cn.com.cretech.presenter.MicroPublishedPresenter
import cn.com.cretech.widget.PagerSlidingTabStrip

/**
 * 微册馆
 */

class MicroPublishedFragment : MVPBaseFragment<MicroPublishedPresenter, FragmentStyleBinding>() ,IMicroPublishedView {
    override fun setPagerSlidingTabStrip(): PagerSlidingTabStrip {
        return binDing.pagerSliding
    }

    override fun setViewPager(): ViewPager {
        return binDing.viewPager
    }

    override fun setLayoutRes(): Int {

        return R.layout.fragment_style
    }

    override fun initView() {
        super.initView()
        presenter = MicroPublishedPresenter(this,context!! , childFragmentManager)
    }

    override fun initData() {
        super.initData()
        presenter.setTitleData()
    }

}
