package cn.com.cretech.presenter

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.activity.RegisterActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UpdateUserMessageBean
import cn.com.cretech.databinding.DialogExistUserBinding
import cn.com.cretech.iView.IRegisterView
import cn.com.cretech.model.RegisterModel
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.StringUtils
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.CustomPopupDialog
import retrofit2.Response

class RegisterPresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView : IRegisterView

    constructor(context : RegisterActivity ,fm: FragmentManager): this(fm){
        this.iView = context
    }

    /**
     * 检查用户名
     */

    fun isCheckUsername(userName :String){

        BaseApplication.netService.isCheckUsername(map,userName)
            .enqueue(object :NetWorkResult<UpdateUserMessageBean>(){
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    when(response.body()!!.code){
                        1 -> {
                            setBooleanExist(true)
                        }
                        0 -> {
                            setBooleanExist(false)
                        }
                    }
                }

            })

    }
    lateinit var v :View
    private fun setBooleanExist(bn: Boolean) {
        iView.isCheckImage(bn)
        if (!bn)  {
            if (binDing == null) {
                binDing = DataBindingUtil.inflate(LayoutInflater.from((iView as FragmentActivity)),R.layout.dialog_exist_user , null ,false)
            }

            if (userDialog == null) {
                userDialog = CustomPopupDialog<DialogExistUserBinding>((iView as FragmentActivity), binDing, false)
                userDialog!!.setCanceledOnTouchOutside(false)
            }
            userDialog!!.show()
        }
        binDing!!.tvDismiss.setOnClickListener {  userDialog!!.dismiss() }
    }


    /**
     * 该用户名已存在，清空用户名
     */
    fun clearUsername(){
        iView.clearUsername()
    }
    /**
     * 注册
     */
    fun onClickRegister(model : RegisterModel){
        if (model.userName == "" || model.phone == "" || model.yzm == "" || model.password == "" || model.affirmPassword == "" ){
            Toast(iView as FragmentActivity ,(iView as FragmentActivity).resources.getString(R.string.input_no_null))
            return
        }
        if ( model.password != model.affirmPassword ){
            Toast(iView as FragmentActivity ,(iView as FragmentActivity).resources.getString(R.string.two_password_fit))
            return
        }
        BaseApplication.netService.getRegister(map,model.userName,model.phone,model.yzm,model.password)
            .enqueue(object :NetWorkResult<UpdateUserMessageBean>(){
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    Toast(iView as FragmentActivity ,response.body()!!.msg)
                    when(response.body()!!.code){
                        1 -> {
                            (iView as FragmentActivity).finish()
                        }
                    }
                }
            })
    }
    /**
     * 获取验证码
     */
    fun getYZM(phone :String){
        if (!StringUtils.isMobile(phone)) {
            Toast(iView as FragmentActivity,(iView as FragmentActivity).resources.getString(R.string.no_register_phone))
            return
        }
        BaseApplication.netService.getYZM(map,phone)
            .enqueue(object :NetWorkResult<UpdateUserMessageBean>(){
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
}