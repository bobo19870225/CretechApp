package cn.com.cretech.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.broad.UserUpdateBroad
import cn.com.cretech.databinding.FragmentMyBinding
import cn.com.cretech.iView.IMyView
import cn.com.cretech.presenter.MyPresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.ListenerHandlers


class  MyFragment : MVPBaseFragment<MyPresenter , FragmentMyBinding>() , IMyView {
    override fun setLoginImage(imageUrl : String, userName : String) {
        if(imageUrl ==""){
            binDing.circleImage.setImageResource(R.drawable.icon)
        }else{
            LoadImage().loadImage(context!! , imageUrl , binDing.circleImage)
        }
        binDing.tvUsername.text = userName
    }

    override fun setLoginVisible() {
        if(binDing.isLogin!!){
            //是登陆状态
            binDing.tvOutLogin.visibility = View.VISIBLE
            presenter.setLoginData(BaseApplication().getUserMessage().uid)
        } else{
            //退出状态
            binDing.circleImage.setImageDrawable(resources.getDrawable(R.drawable.icon))
            binDing.tvOutLogin.visibility = View.GONE
        }
    }

    override fun setIsLoginStatus() {
       binDing.isLogin = BaseApplication().isUserLogin()
    }

    lateinit var broadReceiver : UserUpdateBroad
    override fun setLayoutRes(): Int {

        return  R.layout.fragment_my
    }

    override fun initView() {
        super.initView()
        presenter = MyPresenter(this , context!! ,childFragmentManager)
        broadReceiver = UserUpdateBroad()
        context!!.registerReceiver(broadReceiver,IntentFilter("com.user.update"))
    }

    override fun setListener() {
        binDing.listener = ListenerHandlers( context!!,this@MyFragment)
        binDing.presenter = presenter
    }

    override fun initData() {
        super.initData()
        setLoginVisible()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if(requestCode == BaseLink().MYUSERMESSAGE){
                setIsLoginStatus()
                setLoginVisible()
                BaseApplication().getUserDefaultSchoolId()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (broadReceiver.is_update){
            presenter.setLoginData()
            setIsLoginStatus()
            setLoginVisible()
            broadReceiver.is_update =false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(broadReceiver)
    }
}
