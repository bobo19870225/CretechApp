package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.activity.CoverActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.SchoolMessageBean
import cn.com.cretech.iView.ICoverView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

class CoverPresenter(fm: FragmentManager) : BasePresenter(fm) {

    lateinit var iView: ICoverView
    var column_id = 0

    constructor(iView: CoverActivity,column_id : Int, fm: FragmentManager) : this(fm) {
        this.iView = iView
        this.column_id = column_id
    }

    fun SkipCover(company_id : String){

        BaseApplication.netService.getCover(map,company_id)
            .enqueue(object : NetWorkResult<SchoolMessageBean>(){

                override fun onSucceed(response: Response<SchoolMessageBean>) {
                    super.onSucceed(response)
                    var dataBean = response.body()!!.data
                    isVerifyToken(iView as FragmentActivity,response.body()!!.code)
                    dataBean.column_id = column_id
                    iView.setH5Image(dataBean , dataBean.company_image,dataBean.company_name,dataBean.sum_count)
                    BaseApplication.company_id = dataBean.company_id
                }

            } )

    }

}