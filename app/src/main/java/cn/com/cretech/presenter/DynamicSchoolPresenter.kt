package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import cn.com.cretech.activity.DynamicSchoolActivity
import cn.com.cretech.adapter.DynamicSchoolAdapter
import cn.com.cretech.adapter.MicroDynamicAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.bean.DynamicSchoolBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iModel.INewMicroModel
import cn.com.cretech.iView.IDynamicSchoolView
import cn.com.cretech.model.NewMicroModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.NetWorkSign
import cn.com.cretech.util.Toast
import retrofit2.Response

class DynamicSchoolPresenter(fm :FragmentManager) : BasePresenter(fm) {

    lateinit var iView : IDynamicSchoolView
    lateinit var iModel : INewDynamicMicroModel
    lateinit var context : Context
    lateinit var adapter : MicroDynamicAdapter

    constructor(context: Context , iView : DynamicSchoolActivity , fm: FragmentManager) : this(fm){
        this.context = context
        this.iView = iView
        this.iModel = NewMicroModel()
    }
    fun setClearData(schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic){
        iModel.setPageNumber(1)
        setData(schoolDynamic)
    }
    fun setData(schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic){

        BaseApplication.netService.getDynamicSchool(map,schoolDynamic.company_id,schoolDynamic.column_id ,iModel.getPageNumber())
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