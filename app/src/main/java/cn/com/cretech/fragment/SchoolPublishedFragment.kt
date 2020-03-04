package cn.com.cretech.fragment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentDynamicBinding
import cn.com.cretech.iView.ISchoolPublishedView
import cn.com.cretech.presenter.SchoolPublishedPresenter
import com.baoyz.widget.PullRefreshLayout

class SchoolPublishedFragment : MVPBaseFragment<SchoolPublishedPresenter, FragmentDynamicBinding>(),ISchoolPublishedView {
    override fun setRecyclerView(): RecyclerView {
        return binDing.lvData
    }

    override fun setPullRefreshLayout(): PullRefreshLayout {
        return binDing.pullToRefresh
    }

    override fun setLayoutRes(): Int {

        return R.layout.fragment_dynamic
    }

    override fun initView() {
        super.initView()
        presenter = SchoolPublishedPresenter(this ,context!!, childFragmentManager)
        initRecycler(binDing.lvData)
        var divider = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context!! , R.drawable.divider)!!)
        binDing.lvData.addItemDecoration(divider)
    }

    override fun setListener() {
        binDing.pullToRefresh.setOnRefreshListener(object : PullRefreshLayout(context),
            PullRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                presenter.setData()
            }
        })
    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }
}
