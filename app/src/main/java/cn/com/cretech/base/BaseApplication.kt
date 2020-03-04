package cn.com.cretech.base

import android.app.Application
import android.os.Environment
import cn.com.cretech.bean.UserDefaultKeepSchoolBean
import cn.com.cretech.bean.UserMessageBean
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.setting.NetWorkService
import cn.com.cretech.util.SpUtils
import cn.com.cretech.util.SystemUtils
import com.alivc.player.AliVcMediaPlayer
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

class BaseApplication : Application() {

    companion object{
        lateinit  var app : BaseApplication
        lateinit  var netService : NetWorkService // 网咯请求的实例
        var company_id : String = "" // 变化的学校id
        lateinit var map : MutableMap<String,String>
        lateinit var cachePath : String
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        AliVcMediaPlayer.init(app)
        var client = OkHttpClient()
        netService = Retrofit.Builder()
            .baseUrl(BaseLink().BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(NetWorkService::class.java)

        addFromUrl()
        /**
         * 获取默认关注学校
         */
        getUserDefaultSchoolId()

        UMConfigure.setLogEnabled(true)
        UMShareAPI.get(this)
        UMConfigure.init(this, "5ca466fa570df31e67000e09", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null)
        //UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"5ca466fa570df31e67000e09");
        PlatformConfig.setWeixin("wxf67118ddb832c458", "1426d7fa14b544575e783412c21ee07f")
        initCachePath()
    }
    fun getUserDefaultSchoolId(){
        if (isUserLogin()){
            netService.getUserDefaultSchoolId(map,getUserMessage().uid).enqueue(object : NetWorkResult<UserDefaultKeepSchoolBean>(){
                override fun onSucceed(response: Response<UserDefaultKeepSchoolBean>) {
                    super.onSucceed(response)
                    company_id = response.body()!!.dataBean.company_id
                }

                override fun onNothing() {
                    super.onNothing()
                }
            })
        }
    }
    /**
     * 前缀
     */
    fun addFromUrl(){
        map = mutableMapOf()
        map[BaseLink().APP_VERSION] = SystemUtils.getAppVersionName(BaseApplication.app)
        map[BaseLink().OFFICIAL] = "0"
        map[BaseLink().TYPE] = "2"
    }
    /**
     * 获取用户信息
     */
    fun getUserMessage() : UserMessageBean.UserMessage{
        return SpUtils.getObject(app , UserMessageBean.UserMessage::class.java)
    }
    /**
     * 是否登陆
     */
    fun isUserLogin() : Boolean{
        return SpUtils.getObject(app , UserMessageBean.UserMessage::class.java) != null
    }

    fun getUid () : Int{
        if (!isUserLogin()){
            return 0
        }
        return getUserMessage().uid
    }
    /**
     * 初始化缓存路径
     */
    private fun initCachePath() {
        val sampleDir = File(
            Environment.getExternalStorageDirectory().toString() + File.separator +
                    BaseLink.BASE_URL
        )
        if (!sampleDir.exists()) {
            sampleDir.mkdirs()
        }

        cachePath = sampleDir.absolutePath
    }

    fun getCachePath(): String {
        return cachePath
    }
}