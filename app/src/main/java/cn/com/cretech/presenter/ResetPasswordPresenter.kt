package cn.com.cretech.presenter

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.activity.ResetPasswordActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UpdateUserMessageBean
import cn.com.cretech.iView.IResetPasswordView
import cn.com.cretech.model.ResetPasswordModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.StringUtils
import cn.com.cretech.util.Toast
import retrofit2.Response

class ResetPasswordPresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView : IResetPasswordView

    constructor(iView: ResetPasswordActivity, fm: FragmentManager): this(fm){
        this.iView = iView
    }

    /**
     * 获取重置验证码
     */
    fun getYZM(phone : String){
        if (!StringUtils.isMobile(phone)) {
            Toast(iView as FragmentActivity,(iView as FragmentActivity).resources.getString(R.string.no_register_phone))
            return
        }
        BaseApplication.netService.getResetYZM(map,phone)
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>(){
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    when(response.body()!!.code){
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

    /**
     * 重置密码
     */
    fun onResetPassword(resetPasswordModel: ResetPasswordModel){
        if ( resetPasswordModel.phone == "" || resetPasswordModel.yzm == "" || resetPasswordModel.password == "" || resetPasswordModel.affirmPassword == "" ){
            Toast(iView as FragmentActivity ,(iView as FragmentActivity).resources.getString(R.string.input_no_null))
            return
        }
        if ( resetPasswordModel.password != resetPasswordModel.affirmPassword ){
            Toast(iView as FragmentActivity ,(iView as FragmentActivity).resources.getString(R.string.two_password_fit))
            return
        }
        BaseApplication.netService.getResetPassword(map,resetPasswordModel.phone,resetPasswordModel.yzm,resetPasswordModel.password)
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>(){

                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    Toast(iView as FragmentActivity , response.body()!!.msg)
                    isVerifyToken(iView as FragmentActivity,response.body()!!.code)
                    when(response.body()!!.code){
                        1 -> {
                            (iView as FragmentActivity).finish()
                        }
                    }
                }
            })
    }
}