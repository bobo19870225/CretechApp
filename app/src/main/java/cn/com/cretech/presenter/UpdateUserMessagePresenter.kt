package cn.com.cretech.presenter

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UpdateUserMessageBean
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.Toast
import retrofit2.Response

class UpdateUserMessagePresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var context :Context
    var position :Int = BaseLink.POSITION_UPDATE_NICKNAME

    constructor(context: Context ,position : Int , fm: FragmentManager) : this(fm){
        this.context = context
        this.position = position
    }

    /**
     * 确认修改
     */

    fun updateNickName(nickname :String ,oldPassword :String ,newPassword :String ,affirmPassword :String ){
        when(position){
            BaseLink.POSITION_UPDATE_NICKNAME -> {
                if (nickname == ""){
                    Toast(context , context.resources.getString(R.string.nickname_no_null))
                    return
                }
                var intent = Intent()
                intent.putExtra("nickname",nickname)
                setUpdateNickname(nickname,intent)
            }
            BaseLink.POSITION_UPDATE_PASSWORD -> {
                if (oldPassword == "" || newPassword == ""  || affirmPassword == "" ){
                    Toast(context , context.resources.getString(R.string.password_no_null))
                    return
                }
                if ( newPassword != affirmPassword  ){
                    Toast(context , context.resources.getString(R.string.two_password_fit))
                    return
                }
                setUpdatePassword(oldPassword,newPassword)
            }
        }
    }

    /**
     * 修改昵称
     */
    private fun setUpdateNickname(nickname: String ,intent : Intent){

        BaseApplication.netService.updateNickname(map,BaseApplication().getUid() ,nickname)
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>(){
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    getResultData(response , intent)
                }
            })

    }
    /**
     * 修改密码
     */
    private fun setUpdatePassword(oldPassword: String,newPassword: String){

        BaseApplication.netService.updatePassword(map,BaseApplication().getUid() ,oldPassword , newPassword)
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>(){
                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    isVerifyToken(context,response.body()!!.code)
                    getResultData(response , Intent())
                }
            })


    }
    fun getResultData(response: Response<UpdateUserMessageBean> , intent: Intent){
        var code = response.body()!!.code
        when(code){
            1 -> {
                (context as FragmentActivity).setResult(RESULT_OK,intent)
                (context as FragmentActivity).finish()
            }
            0 -> {
                Toast(context,response.body()!!.msg)
            }
        }
    }
}