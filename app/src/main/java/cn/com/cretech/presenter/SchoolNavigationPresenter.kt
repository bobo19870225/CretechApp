package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import cn.com.cretech.activity.SchoolNavigationActivity
import cn.com.cretech.adapter.SchoolNavigationAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.SchoolNavigationBean
import cn.com.cretech.iView.ISchoolNavigationView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

class SchoolNavigationPresenter(fm : FragmentManager) : BasePresenter(fm){

    lateinit var context: Context
    lateinit var iView : ISchoolNavigationView

    constructor(context: Context  , iView : SchoolNavigationActivity , fm: FragmentManager) : this(fm){
        this.context = context
        this.iView = iView
    }


    fun setData(nav_id : Int){

        BaseApplication.netService.getSchoolNavigation(map,BaseApplication().getUid(),nav_id)
            .enqueue(object : NetWorkResult<SchoolNavigationBean>(){

                override fun onSucceed(response: Response<SchoolNavigationBean>) {
                    super.onSucceed(response)
                    iView.setCancelPull()
                    var dataList = response.body()!!.data
                    isVerifyToken(context,response.body()!!.code)
                    iView.setRecyclerView().adapter = SchoolNavigationAdapter(context , dataList)
                }
            })
    }
}