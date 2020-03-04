package cn.com.cretech.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UserDataBean
import cn.com.cretech.bean.UserMessageBean
import cn.com.cretech.databinding.DialogOutLoginBinding
import cn.com.cretech.fragment.MyFragment
import cn.com.cretech.iView.IMyView
import cn.com.cretech.listener.MyListener
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.SkipTo
import cn.com.cretech.util.SpUtils
import cn.com.cretech.widget.CustomPopupDialog
import retrofit2.Call
import retrofit2.Response

class MyPresenter(var context: Context , fm : FragmentManager) : BasePresenter(fm )  {

    lateinit var iView : IMyView

    constructor(iView : MyFragment ,context: Context , fm : FragmentManager) : this(context , fm ){
        this.iView = iView
        iView.setIsLoginStatus()
    }
    var bindingUtil : DialogOutLoginBinding? = null
    var v = R.layout.dialog_out_login
    lateinit var custom : CustomPopupDialog<DialogOutLoginBinding>
    fun outLogin(){
        if (bindingUtil == null){
            bindingUtil = DataBindingUtil.inflate(LayoutInflater.from(context),v , null ,false)
            custom = CustomPopupDialog<DialogOutLoginBinding>(context ,bindingUtil ,  false)
            bindingUtil!!.outLoginListener = this@MyPresenter
        }
        custom.show()
    }

    /**
     * 取消退出登录
     */
    fun canOutLogin(){
        custom.dismiss()
    }
    /**
     * 确定退出登录
     */
    fun conOutLogin(){
        custom.dismiss()
        SpUtils.removeObject(context , UserMessageBean.UserMessage::class.java)
        iView.setIsLoginStatus()
        iView.setLoginVisible()
        iView.setLoginImage("",context.resources.getString(R.string.no_login))
    }
    /**
     * 获取用户信息
     */
    fun setLoginData(uid : Int){

        BaseApplication.netService.getUserData(map,uid)
            .enqueue(object : NetWorkResult<UserDataBean>(){
                override fun onSucceed(response: Response<UserDataBean>) {
                    super.onSucceed(response)
                    var userData = response.body()!!.data
                    isVerifyToken(context,response.body()!!.code)
                    iView.setLoginImage(BaseLink.BASE_USER_IMAGE+userData.user_img , userData.username)
                }
            })
    }
}