package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.*
import cn.com.cretech.childfragment.ChildMicroFragment
import cn.com.cretech.fragment.MicroFragment
import cn.com.cretech.iView.IMicroView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.util.EncodeImgZxing
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.ShareUtil
import retrofit2.Response

class MicroPresenter(fragmentManager: FragmentManager) : BasePresenter(fragmentManager),
    ChildMicroFragment.OnListenerChildMicroData {

    lateinit var iView : IMicroView
    lateinit var context : Context

    constructor(context: Context , iView: MicroFragment , fm : FragmentManager): this(fm){
        this.context = context
        this.iView = iView
    }

    /**
     * 获取学校状态
     */
    fun getKeepStatus(){

        BaseApplication.netService.getKeepStatus(map,BaseApplication().getUid(),BaseApplication.company_id)
            .enqueue(object : NetWorkResult<KeepStatusBean>(){

                override fun onSucceed(response: Response<KeepStatusBean>) {
                    super.onSucceed(response)
                    isVerifyToken(context,response.body()!!.code)
                    when(response.body()!!.code){
                        1 -> {
                            if (response.body()!!.data.result == 1){//已关注
                                iView.setPopupWindow(true)
                            }else{
                                iView.setPopupWindow(false)
                            }

                        }
                    }
                }

            })
    }

    fun isKeepSchool(isKeep :Boolean){

        if (isKeep){//取消关注
            BaseApplication.netService.getCancelKeep(map,BaseApplication.company_id,BaseApplication().getUid())
                .enqueue(object : NetWorkResult<CodeMessageBean>(){
                    override fun onSucceed(response: Response<CodeMessageBean>) {
                        super.onSucceed(response)
                        setIsKeep(response.body()!!)
                    }
                })
        }else{//关注
            BaseApplication.netService.getKeep(map,BaseApplication.company_id,BaseApplication().getUid())
                .enqueue(object : NetWorkResult<CodeMessageBean>(){
                    override fun onSucceed(response: Response<CodeMessageBean>) {
                        super.onSucceed(response)
                        setIsKeep(response.body()!!)
                    }
                })
        }
    }
    fun setIsKeep(bean : CodeMessageBean){
        Toast(context,bean.msg)
        iView.setCancelWindow()
    }
    fun getShareData(){
        (fragmentList.get(1) as ChildMicroFragment).setOnListenerChildMicroData(this)
    }
    var dataBean: ChildMicroPublishedBean.DataBean.SchoolDynamic? =null
    override fun setOnListenerData(dataBean: ChildMicroPublishedBean.DataBean.SchoolDynamic) {
        this.dataBean = dataBean
    }
    fun setShare(){
        iView.setCancelWindow()
        if (dataBean == null){
            Toast(context,context.resources.getString(R.string.no_school))
            return
        }
        if (dataBean!!.column_img != "") {
            ShareUtil((context as FragmentActivity), dataBean!!.column_img, null, dataBean!!.column_names)
        } else {
            if (dataBean!!.qrc != "") {
                var qrImage = EncodeImgZxing.createQRImage(
                    dataBean!!.qrc,
                    DensityUtils().dip2px(238f),
                    DensityUtils().dip2px(325f),
                    null,
                    "")
                ShareUtil((context as FragmentActivity), "", qrImage, dataBean!!.column_names)
            }
        }
    }
}