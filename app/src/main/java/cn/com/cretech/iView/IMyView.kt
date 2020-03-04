package cn.com.cretech.iView

import cn.com.cretech.mvpbase.BaseView

interface IMyView  {

    fun setLoginVisible()

    fun setLoginImage(imageUrl : String, userName : String)

    fun setIsLoginStatus()

}