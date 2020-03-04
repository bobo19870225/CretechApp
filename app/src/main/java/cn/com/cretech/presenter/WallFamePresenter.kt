package cn.com.cretech.presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentManager
import cn.com.cretech.adapter.WallFameAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.bean.WeSchoolBean
import cn.com.cretech.childfragment.WallFameFragment
import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iView.IWallFameView
import cn.com.cretech.model.NewMicroModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.widget.ScrollLinearLayoutManager
import retrofit2.Response

class WallFamePresenter(fm : FragmentManager) : BasePresenter(fm){

    lateinit var iView : IWallFameView
    lateinit var iModel : INewDynamicMicroModel
    lateinit var context : Context
    lateinit var layoutManager : ScrollLinearLayoutManager
    constructor(iView : WallFameFragment, context: Context, layoutManager : ScrollLinearLayoutManager,
                fm : FragmentManager):this(fm){
        this.iView = iView
        this.context = context
        this.layoutManager = layoutManager
        this.iModel = NewMicroModel()
        mHandler = Handler()
        setCompanyId(BaseApplication.company_id)
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
    lateinit var  mHandler : Handler
    var oldItem = 0
    var scrollRunnable : Runnable? = null
    lateinit var dataList :  MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    fun setData(){

        BaseApplication.netService.getWallFameData(map,iModel.getCompany(),iModel.getPageNumber(),iModel.getListCount())
            .enqueue(object : NetWorkResult<WeSchoolBean>(){

                override fun onSucceed(response: Response<WeSchoolBean>) {
                    super.onSucceed(response)
                     dataList = response.body()!!.data
                    iView.setGallery().adapter = WallFameAdapter(context,dataList)
                     scrollRunnable = object : Runnable {
                        override fun run() {
                            iView.setGallery().scrollBy(3, 0)

                            val firstItem =  layoutManager.findFirstVisibleItemPosition()
                            if (firstItem != oldItem && firstItem > 0) {
                                oldItem = firstItem
                            }

                            Log.e("WallFragmentPresenter", "run: firstItem:$firstItem")
                            mHandler.postDelayed(this, 10)
                        }
                    }
                    mHandler.postDelayed(scrollRunnable,10)
                }

            })
    }
    fun onResume(){
        if(scrollRunnable != null){
            mHandler.postDelayed(scrollRunnable,10)
        }
    }
    fun onStop(){
        mHandler.removeCallbacks(scrollRunnable)
    }
}