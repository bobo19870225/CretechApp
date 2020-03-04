package cn.com.cretech.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import cn.com.cretech.R
import kotlinx.android.synthetic.main.title_bar.view.*

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var binDing : T
    lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSkip()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binDing = DataBindingUtil.inflate(inflater,setLayoutRes(),container,false)
        v = binDing.root
        initView()
        initLifecycle()
        setListener()
        return v
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    fun getFragmentView() : View{
        return v
    }

    abstract fun setLayoutRes() : Int
    open fun setSkip(){

    }
    open fun initView(){

    }
    open fun initLifecycle(){

    }
    open fun setListener(){

    }
    open fun initData(){

    }

}