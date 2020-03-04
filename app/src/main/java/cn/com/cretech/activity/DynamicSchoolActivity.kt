package cn.com.cretech.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.databinding.ActivityDynamicSchoolBinding
import cn.com.cretech.iView.IDynamicSchoolView
import cn.com.cretech.presenter.DynamicSchoolPresenter
import cn.com.cretech.pullToRefresh.PullToRefreshBase
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView

class DynamicSchoolActivity : MVPBaseActivity<DynamicSchoolPresenter,ActivityDynamicSchoolBinding>() , IDynamicSchoolView {
    override fun setLinearLayout(): LinearLayout {
        return this.findViewById(R.id.ll_net_work)
    }

    override fun setNetWorkError(): TextView {
        return this.findViewById(R.id.tv_net_work)
    }

    override fun setNetWorkClick(): TextView {
        return this.findViewById(R.id.tv_click)
    }

    override fun setRecyclerView(): RecyclerView {
        return lvData
    }

    override fun setPullToRefreshScrollView(): PullToRefreshScrollView {
        return binDing.pullRefreshLayout
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_dynamic_school
    }
    lateinit var layout_recycler_view : View
    lateinit var lvData : RecyclerView
    lateinit var schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic
    override fun initView() {
        super.initView()
        layout_recycler_view =  LayoutInflater.from(this).inflate(R.layout.recycler_view,null)
        lvData = layout_recycler_view.findViewById(R.id.lv_data)
        initRecycler(lvData)
        lvData.isNestedScrollingEnabled = false
        binDing.pullRefreshLayout.refreshableView .addView(layout_recycler_view)
        presenter = DynamicSchoolPresenter(this,this , supportFragmentManager)
    }

    override fun setValues(): String {
        schoolDynamic = intent.getSerializableExtra("schoolDynamic") as ChildMicroPublishedBean.DataBean.SchoolDynamic
        return schoolDynamic.column_names
    }

    override fun setListener() {
        binDing.pullRefreshLayout.setOnRefreshListener(object : PullToRefreshBase.OnRefreshListener<ScrollView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setClearData(schoolDynamic)
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setData(schoolDynamic)
            }
        })
    }

    override fun initData() {
        super.initData()
        presenter.setData(schoolDynamic)
    }
}