package cn.com.cretech.presenter

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.activity.LoginActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UpdateUserMessageBean
import cn.com.cretech.bean.UserMessageBean
import cn.com.cretech.iView.ILoginView
import cn.com.cretech.model.UserMessageModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.SkipTo
import cn.com.cretech.util.SpUtils
import cn.com.cretech.util.StringUtils
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.ListenerHandlers
import retrofit2.Response

class LoginPresenter(fm: FragmentManager) : BasePresenter(fm) {

    lateinit var context: Context
    lateinit var iView: ILoginView

    constructor(context: Context, iView: LoginActivity, fm: FragmentManager) : this(fm) {
        this.context = context
        this.iView = iView
    }

    /**
     * 点击登陆按钮
     */
    fun setLoginButton(userDataBean: UserMessageModel) {
        if (userDataBean.userName == "") {
            Toast(context, context.resources.getString(R.string.no_phone))
            return
        }
        if (userDataBean.userPassword == "") {
            Toast(context, context.resources.getString(R.string.no_yzm))
            return
        }
        setData(userDataBean)
    }

    /**
     * 获取验证码
     */
    fun getYZM(phone: String) {
        if (!StringUtils.isMobile(phone)) {
            Toast(iView as FragmentActivity,(iView as FragmentActivity).resources.getString(R.string.no_register_phone))
            return
        }
        BaseApplication.netService.getLoginYZM(map, phone)
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>() {
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    when (response.body()!!.code) {
                         1 -> {
                             iView.getYZM()
                         }
                         0 -> {
                             Toast(iView as FragmentActivity ,response.body()!!.msg)
                         }
                    }
                }
            })
    }

    fun setData(userDataBean: UserMessageModel) {
        BaseApplication.netService.userLogin(map, userDataBean.userName, userDataBean.userPassword)
            .enqueue(object : NetWorkResult<UserMessageBean>() {
                override fun onSucceed(response: Response<UserMessageBean>) {
                    super.onSucceed(response)
                    var userList = response.body()!!.userList
                    SpUtils.putObject(context, userList)
                    var intent = Intent()
                    intent.setAction("com.user.update")
                    intent.putExtra("is_update",true)
                    context.sendBroadcast(intent)
                    SkipTo(context).skipMain()
                    //(context as FragmentActivity).setResult(RESULT_OK)
                    (context as FragmentActivity).finish()
                }

                override fun onNothing() {
                    super.onNothing()
                    Toast(
                        context,
                        context.resources.getString(R.string.all_username_password_error)
                    )
                }

                override fun onError() {
                    Toast(context, context.resources.getString(R.string.username_password_error))
                }
            })

    }

}