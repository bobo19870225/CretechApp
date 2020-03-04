package cn.com.cretech.activity

import android.os.Build
import androidx.annotation.RequiresApi
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityAboutWeBinding
import cn.com.cretech.iView.IAboutWeView
import cn.com.cretech.presenter.AboutWePresenter

class AboutWeActivity : MVPBaseActivity<AboutWePresenter,ActivityAboutWeBinding>() , IAboutWeView {
    override fun setAboutAgreement(isAbout: Boolean) {
        var ft = supportFragmentManager.beginTransaction()
        if(isAbout){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                binDing.tvAbout.setBackground(resources.getDrawable(R.drawable.left_blue))
                binDing.tvUserAgreement.setBackground(resources.getDrawable(R.drawable.right_white))
            }
            binDing.tvAbout.setTextColor(resources.getColor(R.color.white))
            binDing.tvUserAgreement.setTextColor(resources.getColor(R.color.black))
            if(presenter.fragmentList.get(0).isAdded){
                ft.hide(presenter.fragmentList.get(1))
                ft.show(presenter.fragmentList.get(0)).commit()
            }else{
                ft.add(R.id.recent_fragment , presenter.fragmentList.get(0)).commit()
            }
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                binDing.tvAbout.setBackground(resources.getDrawable(R.drawable.left_white))
                binDing.tvUserAgreement.setBackground(resources.getDrawable(R.drawable.right_blue))
            }
            binDing.tvAbout.setTextColor(resources.getColor(R.color.black))
            binDing.tvUserAgreement.setTextColor(resources.getColor(R.color.white))
            if(presenter.fragmentList.get(1).isAdded){
                ft.hide(presenter.fragmentList.get(0))
                ft.show(presenter.fragmentList.get(1)).commit()
            }else{
                ft.add(R.id.recent_fragment ,presenter.fragmentList.get(1)).commit()
            }
        }

    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_about_we
    }

    override fun setValues(): String {
        return resources.getString(R.string.about_we)
    }

    override fun initView() {
        super.initView()
        presenter = AboutWePresenter(this  , supportFragmentManager)
        binDing.presenter = presenter
        presenter.initFragment(R.id.recent_fragment)
    }
}