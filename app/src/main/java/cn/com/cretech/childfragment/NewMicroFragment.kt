package cn.com.cretech.childfragment

import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.bean.UserDefaultKeepSchoolBean
import cn.com.cretech.databinding.FragmentNewMicroBinding
import cn.com.cretech.iView.INewMicroView
import cn.com.cretech.presenter.NewMicroPresenter
import cn.com.cretech.pullToRefresh.PullToRefreshBase
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.widget.NestedGridView
import retrofit2.Response

class NewMicroFragment : MVPBaseFragment<NewMicroPresenter, FragmentNewMicroBinding>() , INewMicroView {
    override fun setRecyclerView(): RecyclerView {
         return lvData
    }

    override fun setPullToRefreshScrollView(): PullToRefreshScrollView {
        return binDing.pullRefreshLayout
    }

    override fun setLayoutRes(): Int {

        return R.layout.fragment_new_micro
    }
    lateinit var layout_recycler_view : View
    lateinit var lvData : RecyclerView
    override fun initView() {
        super.initView()
        layout_recycler_view =  LayoutInflater.from(context).inflate(R.layout.recycler_view,null)
        lvData = layout_recycler_view.findViewById(R.id.lv_data)
        initRecycler(lvData)
        lvData.isNestedScrollingEnabled = false
        binDing.pullRefreshLayout.refreshableView .addView(layout_recycler_view)
        presenter = NewMicroPresenter(this,context!!,childFragmentManager)
    }

    override fun setListener() {
        binDing.pullRefreshLayout.setOnRefreshListener(object : PullToRefreshBase.OnRefreshListener<ScrollView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setClearData()
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ScrollView>?) {
                presenter.setData()
            }
        })
    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }

    override fun onResume() {
        super.onResume()
        if (BaseApplication.company_id == "" && BaseApplication().isUserLogin()){
            BaseApplication.netService.getUserDefaultSchoolId(BaseApplication.map,BaseApplication().getUid()).enqueue(object : NetWorkResult<UserDefaultKeepSchoolBean>(){
                override fun onSucceed(response: Response<UserDefaultKeepSchoolBean>) {
                    super.onSucceed(response)
                    BaseApplication.company_id = response.body()!!.dataBean.company_id
                    presenter.onNewIntent()
                }

                override fun onNothing() {
                    super.onNothing()
                }
            })
        }else{
            presenter.onNewIntent()
        }
    }
}