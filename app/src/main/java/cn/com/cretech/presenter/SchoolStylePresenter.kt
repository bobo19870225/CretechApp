package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.activity.SchoolStyleDynamicActivity
import cn.com.cretech.adapter.MicroDynamicAdapter
import cn.com.cretech.adapter.UltraPagerNewDynamicAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.childfragment.NewDynamicMicroFragment
import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iView.IChildDynamicMicroView
import cn.com.cretech.model.NewMicroModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.NetWorkSign
import cn.com.cretech.util.Toast
import retrofit2.Response

class SchoolStylePresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView : IChildDynamicMicroView
    lateinit var iModel : INewDynamicMicroModel
    lateinit var context: Context

    constructor(iView : SchoolStyleDynamicActivity, context: Context, fragmentManager: FragmentManager) : this(fragmentManager){
        this.iView = iView
        this.context = context
        this.iModel = NewMicroModel()
        initUltraViewPager(context,iView.setUltraViewPager())
    }

    lateinit var adapter : UltraPagerNewDynamicAdapter
    lateinit var schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic
    fun setData(schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic){
        this.schoolDynamic = schoolDynamic
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
                    if (iModel.getPageNumber() == 1){
                        if(BaseBean(liveList).setListValues() == NetWorkSign.NET_CONNECT_SUCCEED || BaseBean(resultList).setListValues() == NetWorkSign.NET_CONNECT_SUCCEED){
                            setVisibility(iView.setUltraViewPager(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_SUCCEED)
                        }else{
                            setVisibility(iView.setUltraViewPager(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
                        }
                        liveList.sortBy {  it.livetime }
                        liveList.sortWith(
                            compareBy(
                                {it.status == "直播已结束"},
                                {it.status == "尚未开始"},
                                {it.status == "正在直播"}
                            )
                        )
                        iModel.setListCount(liveList.size+resultList.size)
                        adapter = UltraPagerNewDynamicAdapter(context,liveList,resultList)
                        iView.setUltraViewPager().adapter = adapter
                    }else{
                        if(liveList.size == 0 && resultList.size == 0){
                            Toast(context,"没有更多数据了！")
                            return
                        }
                        iModel.setAddListCount(liveList.size+resultList.size)
                        //adapter.setNotify(liveList,resultList)
                        //liveList.addAll(adapter.liveList)
                        //resultList.addAll(adapter.resultList)
                        adapter.setNotify(liveList,resultList)
                        iView.setUltraViewPager().refresh()
                    }
                    iModel.setPageNumber(iModel.getPageNumber()+1)
                    iView.setUltraViewPager().setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            if (iModel.getListCount() - 3==position){
                                setData(schoolDynamic)
                            }
                        }
                    })
                }

                override fun onNothing() {
                    super.onNothing()
                    setVisibility(iView.setUltraViewPager(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
                }
            })

    }

}