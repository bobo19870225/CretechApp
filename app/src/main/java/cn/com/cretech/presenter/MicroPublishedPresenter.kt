package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.MicroPublishedBean
import cn.com.cretech.fragment.MicroPublishedFragment
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

class MicroPublishedPresenter(var context: Context, fragmentManager : FragmentManager) : BasePresenter(fragmentManager){

    lateinit var iView : MicroPublishedFragment

    constructor(iMicroPublishedFragment: MicroPublishedFragment,context: Context,fragmentManager: FragmentManager) : this(context,fragmentManager){
        this.iView = iMicroPublishedFragment
    }

    fun setTitleData(){
        BaseApplication.netService.addMicroPublishedTitleData(map).enqueue(object : NetWorkResult<MicroPublishedBean>(){

            override fun onSucceed(response: Response<MicroPublishedBean>) {
                var list = response.body()!!.data

                setVisibility(iView.setPagerSlidingTabStrip(),iView.setViewPager(),iView.setLinearLayout(),iView.setNetWorkError(), BaseBean(list).setListValues())
                isVerifyToken(context,response.body()!!.code)
                if(list != null){
                    setMicroPublishedViewPager(iView.setPagerSlidingTabStrip(),iView.setViewPager(),list)
                }
            }
        })
    }
}