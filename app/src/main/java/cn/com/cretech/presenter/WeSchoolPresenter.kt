package cn.com.cretech.presenter

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.adapter.UltraPagerNewDynamicAdapter
import cn.com.cretech.adapter.UltraPagerWeSchoolAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.bean.WeSchoolBean
import cn.com.cretech.childfragment.NewDynamicMicroFragment
import cn.com.cretech.childfragment.WeSchoolFragment
import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iView.IChildDynamicMicroView
import cn.com.cretech.model.NewMicroModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.NetWorkSign
import cn.com.cretech.util.Toast
import retrofit2.Response

 class WeSchoolPresenter(var context: Context, fragmentManager: FragmentManager) : BasePresenter(fragmentManager) {

    lateinit var iView : IChildDynamicMicroView
    lateinit var iModel : INewDynamicMicroModel

    constructor(iView : WeSchoolFragment , context: Context , fragmentManager: FragmentManager) : this(context,fragmentManager){
        this.iView = iView
        this.iModel = NewMicroModel()
        setCompanyId(BaseApplication.company_id)
        initUltraViewPager(context,iView.setUltraViewPager())
        iView.setLinearLayout().setOnClickListener { setClearData() }
    }
    fun setCompanyId(company_id : String){
        iModel.setCompany(company_id)
    }
    fun setClearData(){
        iModel.setPageNumber(1)
        iModel.setListCount(0)
        setData()
    }
    fun onNewIntent(){
        if (iModel.getCompany() != BaseApplication.company_id){
            setCompanyId(BaseApplication.company_id)
            setClearData()
        }
    }
    lateinit var adapter : UltraPagerWeSchoolAdapter
    fun setData(){
        if (BaseApplication.company_id ==""){
            setVisibility(iView.setUltraViewPager(),iView.setLinearLayout(),iView.setNetWorkError(),NetWorkSign.NET_CONNECT_DATA_EMPTY)
            return
        }
        BaseApplication.netService.getWeSchoolData(map,iModel.getCompany(),iModel.getPageNumber(),iModel.getListCount())
            .enqueue(object : NetWorkResult<WeSchoolBean>(){
                override fun onSucceed(response: Response<WeSchoolBean>) {
                    super.onSucceed(response)
                    var resultList = response.body()!!.data
                    isVerifyToken(context,response.body()!!.code)

                    if (iModel.getPageNumber() == 1){

                        setVisibility(iView.setUltraViewPager(),iView.setLinearLayout(),iView.setNetWorkError(),BaseBean(resultList).setListValues())
                        adapter = UltraPagerWeSchoolAdapter(context,resultList)
                        iView.setUltraViewPager().adapter = adapter
                    }else{
                        if( resultList.size == 0){
                            Toast(context,"没有更多数据了！")
                            return
                        }
                        iModel.setAddListCount(resultList.size)
                        //adapter.setNotify(liveList,resultList)
                        //liveList.addAll(adapter.liveList)
                        //resultList.addAll(adapter.resultList)
                        adapter.setNotify(resultList)
                        iView.setUltraViewPager().refresh()
                    }
                    iModel.setPageNumber(iModel.getPageNumber()+1)
                    iView.setUltraViewPager().setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            if (iModel.getListCount() - 3==position){
                                setData()
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