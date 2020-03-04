package cn.com.cretech.presenter

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.activity.PersonMessageActivity
import cn.com.cretech.activity.UpdateUserMessageActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.UpdateUserMessageBean
import cn.com.cretech.bean.UserDataBean
import cn.com.cretech.iView.IPersonMessageView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.SelectImageListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class PersonMessagePresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView :  IPersonMessageView
    constructor(iView : PersonMessageActivity , fm: FragmentManager) : this(fm){
        this.iView = iView
        photoImage = SelectImageListener(iView as FragmentActivity)
        isCheckSelfPermission(iView , BaseLink.permission)
    }

    /**
     * 点击头像
     */
    fun onClickPhoto(){
        var listData = arrayListOf(
            (iView as FragmentActivity).resources.getString(R.string.shooting),
            (iView as FragmentActivity).resources.getString(R.string.switch_photo),
            (iView as FragmentActivity).resources.getString(R.string.cancel))
        onItemClickListener(object : OnItemClickListener{
            override fun setOnClick(position: Int) {
                when(position){
                    0 -> {
                        if (isCheckSelfPermission((iView as FragmentActivity) , arrayOf(Manifest.permission.CAMERA))) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                                var builder =  StrictMode.VmPolicy.Builder()
                                StrictMode.setVmPolicy(builder.build())
                                builder.detectFileUriExposure()
                            }
                            getPhone((iView as FragmentActivity))//拍照
                        }
                    }
                    1 -> {
                        var arr = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        if (isCheckSelfPermission((iView as FragmentActivity) , arr)) {
                            photoImage.locationImage()
                        }
                    }
                    2 -> {
                        cancel()
                    }
                }
            }

        })
        getPhoneDialog((iView as FragmentActivity),listData)

    }


    /**
     * 点击昵称
     */
    fun onClickNickName(){

        updateNick(BaseLink.POSITION_UPDATE_NICKNAME,BaseLink.UPDATE_NICKNAME)
    }

    /**
     * 修改密码
     */
    fun onUpdatePassword(){

        updateNick(BaseLink.POSITION_UPDATE_PASSWORD,BaseLink.UPDATE_PASSWORD)

    }

    private fun updateNick(position : Int ,requestCode : Int ){
        var intent = Intent((iView as FragmentActivity) , UpdateUserMessageActivity::class.java)
        intent.putExtra("position",position)
        intent.putExtra("nickname",userData.nickname)
        (iView as FragmentActivity).startActivityForResult( intent,requestCode)
    }

    /**
     * 获取用户数据
     */
    lateinit var userData : UserDataBean.UserMessage
    fun setData(){

        BaseApplication.netService.getUserData(map,BaseApplication().getUserMessage().uid)
            .enqueue(object : NetWorkResult<UserDataBean>(){
                override fun onSucceed(response: Response<UserDataBean>) {
                    super.onSucceed(response)
                    userData = response.body()!!.data
                    iView.setBindingData(userData)
                }
            })
    }
    /**
     * 修改头像
     */
    fun updateHeadPhoto(btm : Bitmap){
        BaseApplication.netService.updateHeadPhoto(map ,BaseApplication().getUserMessage().uid
            , MultipartBody.Part.createFormData("file",file.path,
            RequestBody.create(MediaType.parse("image/jpeg"),file)))
            .enqueue(object : NetWorkResult<UpdateUserMessageBean>(){

                override fun onSucceed(response: Response<UpdateUserMessageBean>) {
                    super.onSucceed(response)
                    var code = response.body()!!.code
                    isVerifyToken(iView as FragmentActivity,response.body()!!.code)
                    when (code){
                        1 -> {
                            Toast(iView as FragmentActivity , (iView as FragmentActivity).resources.getString(R.string.update_succeed))
                            iView.setBitmap(btm)
                        }
                        else ->{
                            Toast(iView as FragmentActivity , (iView as FragmentActivity).resources.getString(R.string.update_nothing))
                        }
                    }
                }
            })
    }
}