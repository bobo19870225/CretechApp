package cn.com.cretech.util

import android.os.Handler
import androidx.fragment.app.FragmentActivity
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.SchoolMessageBean

class FromTime(var context : FragmentActivity) {

    fun skipTime( changerTime : Long) {
        Handler().postDelayed(Runnable {
            if (BaseApplication().isUserLogin()){
                SkipTo(context).skipMain()
            }else{
                SkipTo(context).skipLogin()
            }
            context.finish()
        },changerTime)
    }
    fun FromCoverskipMain(dataBean : SchoolMessageBean.DataBean , changerTime : Long) {
        Handler().postDelayed(Runnable {
            SkipTo(context).skipCoverMain(dataBean)
            context.finish()
        },changerTime)
    }
}