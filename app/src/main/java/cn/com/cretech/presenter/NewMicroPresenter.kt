package cn.com.cretech.presenter

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.adapter.MicroDynamicAdapter
import cn.com.cretech.adapter.NewMicroDynamicAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.childfragment.NewMicroFragment
import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iModel.INewMicroModel
import cn.com.cretech.iView.INewMicroView
import cn.com.cretech.model.NewMicroModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.NetWorkSign
import cn.com.cretech.util.Toast
import retrofit2.Response

class NewMicroPresenter(var context: Context , fragmentManager: FragmentManager) : BasePresenter(fragmentManager) {

    lateinit var iView : INewMicroView
    lateinit var iModel : INewDynamicMicroModel

    constructor(iView : NewMicroFragment , context: Context , fragmentManager: FragmentManager) : this(context,fragmentManager){
        this.iView = iView
        this.iModel = NewMicroModel()
        setCompanyId(BaseApplication.company_id)
        iView.setNetWorkClick().setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                setClearData()
            }
        })
    }
    fun setCompanyId(company_id : String){
        iModel.setCompany(company_id)
    }
    fun setClearData(){
        iModel.setPageNumber(1)
        setData()
    }
    fun onNewIntent(){
        if (iModel.getCompany() != BaseApplication.company_id){
            setCompanyId(BaseApplication.company_id)
            setClearData()
        }
    }
    lateinit var adapter : MicroDynamicAdapter
    fun setData(){
        if (BaseApplication.company_id ==""){
            setVisibility(iView.setPullToRefreshScrollView(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
            return
        }
        BaseApplication.netService.getMicroDynamic(map,iModel.getPageNumber(),iModel.getCompany())
            .enqueue(object : NetWorkResult<MicroDynamicBean>(){
                override fun onSucceed(response: Response<MicroDynamicBean>) {
                    super.onSucceed(response)
                    var liveList = response.body()!!.data.live
                    isVerifyToken(context,response.body()!!.code)
                    for (i in 0..(liveList.size -1)){
                        if (liveList.get(i).liveurl == null){
                            liveList.get(i).liveurl = ""
                        }
                        if (liveList.get(i).replay_url == null){
                            liveList.get(i).replay_url = ""
                        }
                    }
                    var resultList = response.body()!!.data.result
                    setClearPullRefresh(iView.setPullToRefreshScrollView())
                    if (iModel.getPageNumber() == 1){
                        if(BaseBean(liveList).setListValues() == NetWorkSign.NET_CONNECT_SUCCEED || BaseBean(resultList).setListValues() == NetWorkSign.NET_CONNECT_SUCCEED){
                            setVisibility(iView.setPullToRefreshScrollView(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_SUCCEED)
                        }else{
                            setVisibility(iView.setPullToRefreshScrollView(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
                        }
                        liveList.sortBy {  it.livetime }
                        liveList.sortWith(
                            compareBy(
                                {it.status == "直播已结束"},
                                {it.status == "尚未开始"},
                                {it.status == "正在直播"}
                            )
                        )
                        adapter = MicroDynamicAdapter(context,liveList,resultList)
                        iView.setRecyclerView().adapter = adapter
                    }else{
                        if(liveList.size == 0 && resultList.size == 0){
                            Toast(context,"没有更多数据了！")
                            return
                        }
                        adapter.setNotify(liveList,resultList)
                    }
                    iModel.setPageNumber(iModel.getPageNumber()+1)
                }

                override fun onNothing() {
                    super.onNothing()
                    setClearPullRefresh(iView.setPullToRefreshScrollView())
                    setVisibility(iView.setPullToRefreshScrollView(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
                }
            })


    }

}