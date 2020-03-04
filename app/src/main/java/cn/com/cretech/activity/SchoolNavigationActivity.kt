package cn.com.cretech.activity

import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.SchoolPublishedBean
import cn.com.cretech.databinding.ActivitySchoolNavigationBinding
import cn.com.cretech.iView.ISchoolNavigationView
import cn.com.cretech.presenter.SchoolNavigationPresenter
import com.baoyz.widget.PullRefreshLayout

class SchoolNavigationActivity : MVPBaseActivity<SchoolNavigationPresenter,ActivitySchoolNavigationBinding>() ,
    ISchoolNavigationView {
    override fun setCancelPull() {
        binDing.swipeRefreshLayout.setRefreshing( false)
    }

    override fun setRecyclerView(): RecyclerView {
        return binDing.lvData
    }

    override fun setAreaValues(area: String) {
        binDing.tvSettingValue.text = (area + resources.getString(R.string.no_after_another))
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_school_navigation
    }

    lateinit var areaChildModel : SchoolPublishedBean.AreaData.ChildBean
    override fun initView() {
        super.initView()
        areaChildModel = intent.getSerializableExtra("areaChildModel") as  SchoolPublishedBean.AreaData.ChildBean
        setAreaValues(areaChildModel.class_name)
        initRecyclerGridView(binDing.lvData)
        presenter = SchoolNavigationPresenter(this ,this , supportFragmentManager)

    }

    override fun setListener() {
        super.setListener()
        binDing.swipeRefreshLayout.setOnRefreshListener(object : PullRefreshLayout(this),
            PullRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                presenter.setData(areaChildModel.id)
            }
        })
    }

    override fun initData() {
        super.initData()
        presenter.setData(areaChildModel.id)
    }
}