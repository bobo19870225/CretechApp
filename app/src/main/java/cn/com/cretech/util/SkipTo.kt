package cn.com.cretech.util

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.com.cretech.activity.*
import cn.com.cretech.bean.SchoolMessageBean
import cn.com.cretech.fragment.MyFragment
import cn.com.cretech.setting.BaseLink


class SkipTo(var context: Context) {

    lateinit var  myFragment : MyFragment
    constructor(context: Context ,myFragment: MyFragment) : this(context){
        this.myFragment = myFragment
    }

    /**
     * 跳转主界面
     */
    fun skipMain(){

        val intent = Intent().setClass(context , MainActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 从封面图跳转主界面
     */
    fun skipCoverMain(dataBean : SchoolMessageBean.DataBean ){

        val intent = Intent().setClass(context , MainActivity::class.java)
        intent.putExtra("dataBean",dataBean)
        context.startActivity(intent)
    }
    /**
     * 跳转登陆界面
     */
    fun skipUserLogin(){
        val intent = Intent().setClass(context , LoginActivity::class.java)
        myFragment.startActivityForResult(intent , BaseLink().MYUSERMESSAGE)
    }

    fun skipLogin(){
        val intent = Intent().setClass(context , LoginActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 跳转注册界面
     */
    fun skipUserRegister(){
        val intent = Intent().setClass(context , RegisterActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 跳转重置密码界面
     */
    fun skipResetPasswordRegister(){
        val intent = Intent().setClass(context , ResetPasswordActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 跳转到个人资料界面
     */
    fun skipPersonMessage(){
        val intent = Intent().setClass(context , PersonMessageActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 跳转到关注列表
     */
    fun skipKeepList(){
        val intent = Intent().setClass(context , KeepListActivity::class.java)
        context.startActivity(intent)
    }
    /**
     * 跳转到关于我们
     */
    fun skipAboutWe(){
        val intent = Intent().setClass(context , AboutWeActivity::class.java)
        context.startActivity(intent)
    }

}