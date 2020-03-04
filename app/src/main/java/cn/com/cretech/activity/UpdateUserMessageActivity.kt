package cn.com.cretech.activity

import android.view.View
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityUpdateUserBinding
import cn.com.cretech.presenter.UpdateUserMessagePresenter
import cn.com.cretech.setting.BaseLink


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpdateUserMessageActivity : MVPBaseActivity<UpdateUserMessagePresenter,ActivityUpdateUserBinding>() {

    override fun setLayoutRes(): Int {

        return R.layout.activity_update_user
    }
    var position : Int = BaseLink.POSITION_UPDATE_NICKNAME
    override fun setValues(): String {
        position = intent.getIntExtra("position",BaseLink.POSITION_UPDATE_NICKNAME)
        nickname =intent.getStringExtra("nickname")
        when(position){
            //修改昵称
            BaseLink.POSITION_UPDATE_NICKNAME -> {
                binDing.llUpdatePassword.visibility = View.GONE
                binDing.etUserName.visibility = View.VISIBLE
                binDing.etUserName.setText(nickname)
                binDing.tvUpdateData.text = resources.getString(R.string.update_nickname)
                return resources.getString(R.string.update_nickname)
            }
            //修改密码
            BaseLink.POSITION_UPDATE_PASSWORD -> {
                binDing.etUserName.visibility = View.GONE
                binDing.llUpdatePassword.visibility = View.VISIBLE
                binDing.tvUpdateData.text = resources.getString(R.string.update_password)
                return resources.getString(R.string.update_password)
            }
        }
        return resources.getString(R.string.update_nickname)
    }
     var nickname : String = ""
    override fun initView() {
        super.initView()
        presenter = UpdateUserMessagePresenter(  this , position , supportFragmentManager)
    }

    override fun setListener() {
        super.setListener()
        binDing.presenter = presenter
        binDing.nickname = binDing.etUserName.text.toString()
        binDing.oldPassword = binDing.etOldPassword.text.toString()
        binDing.newPassword = binDing.etNewPassword.text.toString()
        binDing.affirmPassword = binDing.etAffirmNewPassword.text.toString()
    }

}