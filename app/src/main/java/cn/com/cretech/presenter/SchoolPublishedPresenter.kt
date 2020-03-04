package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import cn.com.cretech.adapter.AreaAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.SchoolPublishedBean
import cn.com.cretech.fragment.SchoolPublishedFragment
import cn.com.cretech.iView.ISchoolPublishedView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import kotlinx.android.synthetic.main.net_error_default.*
import retrofit2.Response

class SchoolPublishedPresenter(var context: Context , fm: FragmentManager) : BasePresenter(fm) {

    lateinit var iView : ISchoolPublishedView

    constructor(iView : SchoolPublishedFragment,context: Context , fm : FragmentManager ): this(context,fm){
        this.iView = iView
        iView.setLinearLayout().setOnClickListener { setData() }
    }

    fun setData(){

        BaseApplication.netService.getSchoolPublishedData(map)
            .enqueue(object : NetWorkResult<SchoolPublishedBean>(){
                override fun onSucceed(response: Response<SchoolPublishedBean>) {
                    super.onSucceed(response)
                    var areaData = response.body()!!.data
                    iView.setPullRefreshLayout().setRefreshing(false)
                    isVerifyToken(context,response.body()!!.code)
                    setVisibility(iView.setPullRefreshLayout(),iView.setLinearLayout(),iView.setNetWorkError(),BaseBean(areaData).setListValues())
                    iView.setRecyclerView().adapter = AreaAdapter(context , areaData  )
                }
            })

    }
}