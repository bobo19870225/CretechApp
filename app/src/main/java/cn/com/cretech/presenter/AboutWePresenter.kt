package cn.com.cretech.presenter

import android.content.Context
import androidx.fragment.app.FragmentManager
import cn.com.cretech.activity.AboutWeActivity
import cn.com.cretech.iView.IAboutWeView
import cn.com.cretech.mvpbase.BasePresenter

class AboutWePresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView: IAboutWeView

    constructor(iView : AboutWeActivity , fm: FragmentManager): this(fm){
        this.iView = iView
    }
    //关于我们
    fun clickAboutWe(){
        iView.setAboutAgreement(true)
    }
    //用户协议
    fun clickAgreement(){
        iView.setAboutAgreement(false)
    }
}