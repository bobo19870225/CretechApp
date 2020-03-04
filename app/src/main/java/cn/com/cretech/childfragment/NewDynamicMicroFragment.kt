package cn.com.cretech.childfragment

import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.bean.UserDefaultKeepSchoolBean
import cn.com.cretech.databinding.FragmentChildMicroBinding
import cn.com.cretech.iView.IChildDynamicMicroView
import cn.com.cretech.presenter.NewDynamicMicroPresenter
import cn.com.cretech.setting.NetWorkResult
import com.tmall.ultraviewpager.UltraViewPager
import retrofit2.Response

class NewDynamicMicroFragment : MVPBaseFragment<NewDynamicMicroPresenter, FragmentChildMicroBinding>() ,
    IChildDynamicMicroView {
    override fun setUltraViewPager(): UltraViewPager {
        return binDing.ultraViewpager
    }
    override fun setLayoutRes(): Int {

        return R.layout.fragment_child_micro
    }
    override fun initView() {
        super.initView()
        presenter = NewDynamicMicroPresenter(this,context!!,childFragmentManager)
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