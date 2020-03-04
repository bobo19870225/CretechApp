package cn.com.cretech.activity

import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityLoginBinding
import cn.com.cretech.iView.ILoginView
import cn.com.cretech.model.UserMessageModel
import cn.com.cretech.presenter.LoginPresenter
import cn.com.cretech.util.TimerHelper
import cn.com.cretech.widget.ListenerHandlers

/**
 * 登陆
 */

class LoginActivity : MVPBaseActivity<LoginPresenter, ActivityLoginBinding>(),ILoginView {
    override fun getYZM() {
        TimerHelper(binDing.tvGetYzm,this).start()
    }


    override fun setLayoutRes(): Int {
        return R.layout.activity_login
    }

    override fun setValues(): String {
        return resources.getString(R.string.user_login)
    }

    override fun initView() {
        presenter = LoginPresenter(this@LoginActivity,this,supportFragmentManager)
    }
    override fun setListener() {
        binDing.click = ListenerHandlers(this@LoginActivity)
        binDing.presenter =presenter

    }

    override fun initData() {
        super.initData()
        binDing.userMessageModel = UserMessageModel(binDing.etUserName.text.toString(),binDing.etPassword.text.toString())

    }
}