package cn.com.cretech.activity

import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityResetPasswordBinding
import cn.com.cretech.iView.IResetPasswordView
import cn.com.cretech.model.ResetPasswordModel
import cn.com.cretech.presenter.ResetPasswordPresenter
import cn.com.cretech.util.TimerHelper

class ResetPasswordActivity : MVPBaseActivity<ResetPasswordPresenter,ActivityResetPasswordBinding>(),
    IResetPasswordView {

    override fun getYZM() {
        TimerHelper(binDing.tvGetYzm,this).start()
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_reset_password
    }
    override fun setValues(): String {
        return resources.getString(R.string.user_reset_password)
    }

    override fun initView() {
        super.initView()
        presenter = ResetPasswordPresenter(this, supportFragmentManager)
    }

    override fun setListener() {
        super.setListener()
        binDing.presneter =presenter
        binDing.resetPasswordModel = ResetPasswordModel()
    }
}