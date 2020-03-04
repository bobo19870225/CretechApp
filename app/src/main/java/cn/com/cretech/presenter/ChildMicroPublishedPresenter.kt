package cn.com.cretech.presenter

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import cn.com.cretech.adapter.ChildMicroPublishedAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.childfragment.ChildMicroPublishedFragment
import cn.com.cretech.iView.IChildMicroPublishedView
import cn.com.cretech.model.MicroPublishedModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

class ChildMicroPublishedPresenter(var context: Context , fm : FragmentManager): BasePresenter(fm) {

    lateinit var iView : IChildMicroPublishedView
    lateinit var iModel : MicroPublishedModel

    constructor(iView : ChildMicroPublishedFragment  , context: Context , fm : FragmentManager) : this(context , fm){
        this.iView = iView
        iModel = MicroPublishedModel()
        iView.setNetWorkClick().setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                setClear()
            }
        })
    }

    fun setDefaultId(default_id : Int){
        iModel.setId(default_id)
    }

    private fun getDefaultId() : Int{
        return iModel.getId()
    }
    fun setPagerNumber(default_id : Int){
        iModel.setPageNumber(default_id)
    }

    private fun getPagerNumber() : Int{
        return iModel.getPageNumber()
    }

    fun setClear(){

        iModel.setClear()
        iModel.setPageNumber(1)
        setChildData()
    }
    lateinit var adapter : ChildMicroPublishedAdapter
    fun setChildData(){

        BaseApplication.netService.addMicroPublishedTitleDynamicData(map,getPagerNumber(),iModel.total,getDefaultId(),BaseApplication.company_id)
            .enqueue(object : NetWorkResult<ChildMicroPublishedBean>(){

                override fun onSucceed(response: Response<ChildMicroPublishedBean>) {
                    super.onSucceed(response)
                    var list = response.body()!!.live_list.microLibrary
                    setClearPullRefresh(iView.setPullRefreshLayout())
                    isVerifyToken(context,response.body()!!.code)
                    if(iModel.getPageNumber() ==1){
                        setVisibility(null,iView.setPullRefreshLayout(),iView.setTvCount(),iView.setLinearLayout(),iView.setNetWorkError(),BaseBean(list).setListValues())
                        adapter = ChildMicroPublishedAdapter(context,list)
                        iView.setGridView().adapter = adapter
                    }else{
                        adapter.setNotify(list)
                    }
                    iModel.setSchoolDynamic(list)
                    setPagerNumber(iModel.page_number+1)
                    iView.setTvCount().text = iModel.getSchoolDynamic().size.toString()+"/"+response.body()!!.live_list.totalNum.toString()
                }
            })

    }
}