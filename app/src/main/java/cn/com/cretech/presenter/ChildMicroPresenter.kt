package cn.com.cretech.presenter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.R
import cn.com.cretech.adapter.ChildDynamicMicroAdapter
import cn.com.cretech.adapter.ChildMicroPublishedAdapter
import cn.com.cretech.adapter.UltraPagerAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseBean
import cn.com.cretech.bean.MicroDataBean
import cn.com.cretech.childfragment.ChildMicroFragment
import cn.com.cretech.iModel.IMicroDataModel
import cn.com.cretech.iView.IChildMicroView
import cn.com.cretech.model.MicroDataModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer
import kotlinx.android.synthetic.main.net_error_default.*
import retrofit2.Response

class ChildMicroPresenter(var context: Context , fragmentManager: FragmentManager) : BasePresenter(fragmentManager) {

    lateinit var iView : IChildMicroView
    lateinit var iModel : IMicroDataModel
    var column_id = 0
    constructor(iView : ChildMicroFragment , context: Context , fm : FragmentManager): this(context,fm){
        this.iView = iView
        this.iModel = MicroDataModel()
        iView.setLinearLayout().setOnClickListener { setData() }
    }
    fun setColumnId(column_id : Int){
        this.column_id = column_id
    }
    fun setData(){
        BaseApplication.netService.getMicroData(map,BaseApplication.company_id )
            .enqueue(object : NetWorkResult<MicroDataBean>(){
                override fun onSucceed(response: Response<MicroDataBean>) {
                    super.onSucceed(response)
                    var dataList = response.body()!!.data
                    setVisibility(iView.setNestedGridView(),iView.setLinearLayout(),iView.setNetWorkError(),BaseBean(dataList).setListValues())
                    isVerifyToken(context,response.body()!!.code)
                    iView.setSwipeRefreshLayout().setRefreshing(false)
                    iView.setNestedGridView().adapter = ChildDynamicMicroAdapter(context,  dataList)
                    if (dataList.size > 0){
                        iView.setData(dataList.get(0))
                    }
                    /* for (i in 0..(dataList.size -1)){
                        if (dataList[i].column_id == column_id){
                            iView.setNestedGridView().currentItem = i
                        }
                    }*/
                }
            })
    }

}