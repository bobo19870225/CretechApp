package cn.com.cretech.activity

import android.view.View
import android.widget.ListView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityKeepListBinding
import cn.com.cretech.iView.IKeepListView
import cn.com.cretech.presenter.KeepListPresenter
import cn.com.cretech.pullToRefresh.PullToRefreshBase
import cn.com.cretech.pullToRefresh.PullToRefreshListView
import cn.com.cretech.util.DensityUtils

class KeepListActivity : MVPBaseActivity<KeepListPresenter,ActivityKeepListBinding>() , IKeepListView {
    override fun setCancelPull() {
        binDing.pullRefreshList.onPullUpRefreshComplete()
        binDing.pullRefreshList.onPullDownRefreshComplete()
    }

    override fun setListView(): ListView {
        return binDing.pullRefreshList.refreshableView
    }

    override fun setNetVisible(is_visible: Boolean) {
        super.setNetVisible(is_visible)
        if (is_visible){
            binDing.pullRefreshList.visibility = View.VISIBLE
        }else{
            binDing.pullRefreshList.visibility = View.GONE
        }
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_keep_list
    }

    override fun setValues(): String {
        return resources.getString(R.string.keep_list)
    }

    override fun initView() {
        super.initView()
        presenter = KeepListPresenter(this ,supportFragmentManager)
        binDing.pullRefreshList.refreshableView.divider = (resources.getDrawable(R.color.distance_gray))
        binDing.pullRefreshList.refreshableView.dividerHeight = (DensityUtils().dip2px(8f))
        binDing.pullRefreshList.isPullLoadEnabled = true
    }

    override fun setListener() {
        super.setListener()
        binDing.pullRefreshList.setOnRefreshListener(object : PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                presenter.setCancelData()
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                presenter.setData()
            }

        })
    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }
}