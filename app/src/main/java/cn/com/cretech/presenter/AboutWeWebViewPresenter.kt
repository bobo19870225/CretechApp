package cn.com.cretech.presenter

import android.util.Log
import androidx.annotation.MainThread
import androidx.fragment.app.FragmentManager
import cn.com.cretech.childfragment.AboutWeWebViewFragment
import cn.com.cretech.iView.IAboutWeWebView
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.setting.BaseLink
import okhttp3.*
import java.io.IOException

class AboutWeWebViewPresenter(fm : FragmentManager) : BasePresenter(fm) {

    lateinit var iView : IAboutWeWebView
    lateinit var client : OkHttpClient

    constructor(iView : AboutWeWebViewFragment , fm : FragmentManager): this(fm){
        this.iView = iView
        client = OkHttpClient()
    }

    fun setData(location : Int){

        when(location){

            0 -> {
                var formBady = FormBody.Builder()
                formBady.add("mod",BaseLink.PUBLIC)
                formBady.add("act",BaseLink.SHOW_ABOUT_US)
                getUrl(formBady)
            }
            1 -> {
                var formBady = FormBody.Builder()
                formBady.add("mod",BaseLink.PUBLIC)
                formBady.add("act",BaseLink.SHOW_USER_AGREEMENT)
                getUrl(formBady)
            }
        }
    }
    fun getUrl(formBady : FormBody.Builder){
        var call = client.newCall(Request.Builder().post(formBady.build()).url(BaseLink.ABOUT_BASE_URL).build())
        call.enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.i("错误信息",e.message)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                iView.setAboutWebView(response.body()!!.string())
            }
        })
    }
}