package cn.com.cretech.presenter

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.adapter.LiveListAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.childfragment.ChildLiveFragment
import cn.com.cretech.iModel.ILiveModel
import cn.com.cretech.iView.ILiveView
import cn.com.cretech.model.LiveModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import retrofit2.Response

/**
 * 直播列表数据
 */
class ChildLivePresenter(var context : Context, fragmentManager: FragmentManager) : BasePresenter(fragmentManager) {

    lateinit var liveView : ILiveView
    lateinit var liveModel : ILiveModel

   constructor(iLiveView : ChildLiveFragment, context: Context, fragmentManager: FragmentManager) : this(context,fragmentManager) {
       liveView = iLiveView
       liveModel = LiveModel()
       getWorkClick()
   }
    fun setIsToday(isToday: Boolean ){
        liveModel.setIsToday(isToday)
    }
    fun setFlag(flag : String ){
        liveModel.setFlag(flag)
    }

    /**
     * 直播界面数据
     */
    fun setData(){

        BaseApplication.netService.addLiveData(map,liveModel.getFlag()).enqueue(object : NetWorkResult<LiveBean>() {
            override fun onSucceed(response: Response<LiveBean>) {
                var list = response.body()!!.live_list
                isVerifyToken(context,response.body()!!.code)
                liveView.setSwipeRefreshLayout().setRefreshing(false)
                setVisibility(null,liveView.setSwipeRefreshLayout(),liveView.setLinearLayout(),liveView.setNetWorkError(),BaseBean(list = list).setListValues())
                list.sortBy {  it.livetime }
                list.sortWith(
                    compareBy(
                        {it.status == "直播已结束"},
                        {it.status == "尚未开始"},
                        {it.status == "正在直播"}
                    )
                )
                for (i in 0..(list.size -1)){
                    if (list.get(i).liveurl == null){
                        list.get(i).liveurl = ""
                    }
                    if (list.get(i).replay_url == null){
                        list.get(i).replay_url = ""
                    }
                }
                liveView.setRecyclerView().adapter = LiveListAdapter( context, liveModel.getIsToday() , list)
            }

        })
    }

    private fun getWorkClick(){
        liveView.setNetWorkClick().setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                setData()
            }
        })
    }

}