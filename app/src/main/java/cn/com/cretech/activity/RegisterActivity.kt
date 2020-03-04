package cn.com.cretech.activity

import android.view.View
import android.view.View.OnFocusChangeListener
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityRegisterBinding
import cn.com.cretech.iView.IRegisterView
import cn.com.cretech.model.RegisterModel
import cn.com.cretech.presenter.RegisterPresenter
import cn.com.cretech.util.TimerHelper

class RegisterActivity : MVPBaseActivity<RegisterPresenter, ActivityRegisterBinding>() , IRegisterView {
    override fun getYZM() {
        TimerHelper(binDing.tvGetYzm,this).start()
    }

    override fun clearUsername() {
        if (!checkUsername){
            setCheckUsername()
        }
    }
    var checkUsername = true

    override fun isCheckImage(checkUsername: Boolean) {
        this.checkUsername = checkUsername
        if (checkUsername) {
            binDing.ivExistUser.setImageResource(R.drawable.can)
        } else {
            binDing.ivExistUser.setImageResource(R.drawable.cannot)
        }
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_register
    }

    override fun setValues(): String {
        return resources.getString(R.string.user_register)
    }
    lateinit var registerModel : RegisterModel
    override fun initView() {
        super.initView()
        presenter = RegisterPresenter(this , supportFragmentManager)
        registerModel = RegisterModel()
    }

    override fun setListener() {
        super.setListener()
        binDing.registerModel= registerModel
        binDing.checkUsername = checkUsername
        binDing.presenter = presenter
        binDing.etUsername.setOnFocusChangeListener(object : OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (!p1){
                    if (registerModel.userName != ""){
                        presenter.isCheckUsername(registerModel.userName)
                    }
                }
            }
        })
    }

   private fun setCheckUsername(){
        binDing.etUsername.setText("")
        binDing.etUsername.setFocusable(true)
        binDing.etUsername.setFocusableInTouchMode(true)
        binDing.etUsername.requestFocus()
        binDing.etUsername.findFocus()
        binDing.ivExistUser.setImageResource(0)
    }
}