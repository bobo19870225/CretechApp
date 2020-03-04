package cn.com.cretech.activity

import cn.com.cretech.R
import cn.com.cretech.base.BaseActivity
import cn.com.cretech.databinding.ActivityWelcomeBinding
import cn.com.cretech.util.FromTime

/**
 * 歡迎頁
 */

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    var changerTime = 2000L

    override fun setLayoutRes(): Int {
        return R.layout.activity_welcome
    }

    override fun setStatusBar(): Boolean {
        return true
    }

    override fun initData() {

        FromTime(this@WelcomeActivity).skipTime(changerTime)

    }

}