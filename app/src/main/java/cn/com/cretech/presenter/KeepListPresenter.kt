package cn.com.cretech.presenter

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.activity.KeepListActivity
import cn.com.cretech.adapter.KeepListAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseModel
import cn.com.cretech.bean.KeepListBean
import cn.com.cretech.iView.IKeepListView
import cn.com.cretech.model.KeepListModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

class KeepListPresenter(fm : FragmentManager) : BasePresenter(fm) , KeepListAdapter.OnDefaultListener {
    override fun setClick(dataList: MutableList<KeepListBean.DataBean>) {
        adapter = KeepListAdapter((iView as FragmentActivity),map,dataList)
        iView.setListView().adapter = adapter
        adapter.setListener(this@KeepListPresenter)
    }


    lateinit var iView : IKeepListView
    lateinit var iModel: BaseModel

    constructor(iView : KeepListActivity , fm: FragmentManager) : this(fm){
        this.iView =iView
        this.iModel = KeepListModel()
    }

    fun setCancelData(){

        iModel.setPageNumber(1)
        setData()
    }
    lateinit var adapter: KeepListAdapter
    fun setData(){

        BaseApplication.netService.getKeepList(map,BaseApplication().getUid(),iModel.getPageNumber())
            .enqueue(object : NetWorkResult<KeepListBean>(){

                override fun onSucceed(response: Response<KeepListBean>) {
                    super.onSucceed(response)
                    iView.setCancelPull()
                    var result = response.body()!!.data
                    isVerifyToken(iView as FragmentActivity,response.body()!!.code)
                    if (iModel.getPageNumber() == 1){
                        if (result.size > 0){
                            iView.setNetVisible(true)
                            setClick(result)
                        }else{
                            iView.setNetVisible(false)
                        }
                    }else{
                        adapter.setNotify(result)
                    }

                    iModel.setPageNumber(iModel.getPageNumber() + 1)
                }
            })

    }

}