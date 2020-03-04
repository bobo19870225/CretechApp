package cn.com.cretech.iView

import android.graphics.Bitmap
import cn.com.cretech.bean.UserDataBean

interface IPersonMessageView {

    fun setBindingData(userDataBean : UserDataBean.UserMessage)
    fun setBitmap(btm : Bitmap)
    fun setNickname(nickname : String)
}