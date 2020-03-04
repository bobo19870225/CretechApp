package cn.com.cretech.base

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.com.cretech.R
import cn.com.cretech.mvpbase.BasePresenter

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {


    lateinit var binDing : T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (setStatusBar()){
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        binDing = DataBindingUtil.setContentView(this@BaseActivity,setLayoutRes())
        initToolBar()
        initView()
        initLifecycle()
        setListener()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    abstract fun setLayoutRes() : Int
    open fun initToolBar(){}
    open fun initView(){}
    open fun initLifecycle(){}
    open fun setListener(){}

    /**
     * 判斷是否全屏顯示
     * 默認 false
     */
    open fun setStatusBar() : Boolean{
        return false
    }
    open fun initData(){}
}