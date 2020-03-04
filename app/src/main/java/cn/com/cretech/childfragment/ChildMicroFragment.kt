package cn.com.cretech.childfragment

import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseFragment
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.bean.UserDefaultKeepSchoolBean
import cn.com.cretech.databinding.FragmentChildMicroBinding
import cn.com.cretech.databinding.FragmentPullToListBinding
import cn.com.cretech.iView.IChildMicroView
import cn.com.cretech.presenter.ChildMicroPresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.widget.NestedGridView
import com.baoyz.widget.PullRefreshLayout
import com.tmall.ultraviewpager.UltraViewPager
import retrofit2.Response

/**
 * 微册
 */
class ChildMicroFragment : MVPBaseFragment<ChildMicroPresenter, FragmentPullToListBinding>() , IChildMicroView  {
    override fun setSwipeRefreshLayout(): PullRefreshLayout {
        return binDing.swipeRefreshLayout
    }

    interface OnListenerChildMicroData{

        fun setOnListenerData(dataBean: ChildMicroPublishedBean.DataBean.SchoolDynamic)

    }

    lateinit var listenerChild : OnListenerChildMicroData

    fun setOnListenerChildMicroData(listenerChild : OnListenerChildMicroData){
        this.listenerChild = listenerChild
    }

    override fun setData(dataBean: ChildMicroPublishedBean.DataBean.SchoolDynamic) {
        listenerChild.setOnListenerData(dataBean)
    }

    override fun setNestedGridView(): NestedGridView {
        return binDing.lvData
    }

    override fun setLayoutRes(): Int {

        return R.layout.fragment_pull_to_list
    }

    override fun initView() {
        super.initView()

        presenter = ChildMicroPresenter(this , context!! , childFragmentManager)
        initGridView(binDing.lvData)
    }

    override fun setListener() {
        binDing.swipeRefreshLayout.setOnRefreshListener(object : PullRefreshLayout(context),
            PullRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                presenter.setData()
            }
        })
    }

    fun onNewIntent(column_id : Int){
        presenter.setColumnId(column_id)
    }

    override fun initData() {
        presenter.setData()
    }
    var company_id :String = ""
    override fun onResume() {
        super.onResume()

        if (BaseApplication.company_id == "" && BaseApplication().isUserLogin()){
            BaseApplication.netService.getUserDefaultSchoolId(BaseApplication.map,BaseApplication().getUid()).enqueue(object : NetWorkResult<UserDefaultKeepSchoolBean>(){
                override fun onSucceed(response: Response<UserDefaultKeepSchoolBean>) {
                    super.onSucceed(response)
                    BaseApplication.company_id = response.body()!!.dataBean.company_id
                    company_id = response.body()!!.dataBean.company_id
                    presenter.setData()
                }

                override fun onNothing() {
                    super.onNothing()
                }
            })
        }else{
            if (company_id != BaseApplication.company_id){
                company_id =BaseApplication.company_id
                presenter.setData()
            }
        }
    }
}