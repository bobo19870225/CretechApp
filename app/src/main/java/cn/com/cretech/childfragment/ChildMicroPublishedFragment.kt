package cn.com.cretech.childfragment

import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentChildMicroPublishedBinding
import cn.com.cretech.iView.IChildMicroPublishedView
import cn.com.cretech.presenter.ChildMicroPublishedPresenter
import cn.com.cretech.pullToRefresh.PullToRefreshBase
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView
import cn.com.cretech.widget.NestedGridView

class ChildMicroPublishedFragment : MVPBaseFragment<ChildMicroPublishedPresenter,FragmentChildMicroPublishedBinding>(),IChildMicroPublishedView {
    override fun setPullRefreshLayout(): PullToRefreshScrollView {
        return binDing.pullRefreshLayout
    }

    override fun setGridView(): NestedGridView {
        return grid_view
    }
    override fun setTvCount(): TextView {
        return binDing.tvCount
    }


    override fun setLayoutRes(): Int {
        return R.layout.fragment_child_micro_published
    }
    lateinit var grid_view_layout : View
    lateinit var grid_view : NestedGridView
    override fun initView() {
        super.initView()
        grid_view_layout = LayoutInflater.from(context).inflate(R.layout.grid_view,null)
        grid_view = grid_view_layout.findViewById(R.id.grid_view)
        binDing.pullRefreshLayout.refreshableView.addView(grid_view_layout)
        initGridView(grid_view)
        presenter = ChildMicroPublishedPresenter(this,context!! ,childFragmentManager)
    }

    override fun setListener() {
        binDing.pullRefreshLayout.setOnRefreshListener(object : PullToRefreshBase.OnRefreshListener<ScrollView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setClear()
            }
            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setChildData()
            }
        })
    }

    override fun initData() {
        super.initData()
        var id = arguments!!.getInt("id",0)
        presenter.setDefaultId(id)
        presenter.setChildData()
    }
}