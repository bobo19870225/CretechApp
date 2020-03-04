package cn.com.cretech.base

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.mvpbase.BasePresenter
import android.R.attr.spacing
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.widget.FullyLinearLayoutManager
import cn.com.cretech.widget.GridSpacingItemDecoration



open class MVPBaseActivity<P : BasePresenter,T : ViewDataBinding> : BaseActivity<T>(){
    override fun setLayoutRes(): Int {
        return R.layout.net_error_default
    }

    lateinit var tool_bar : Toolbar

    lateinit var presenter : P
    lateinit var tv_value : TextView

    open fun setNetVisible(is_visible : Boolean){
        var ll_net_work = binDing.root.findViewById<LinearLayout>(R.id.ll_net_work)
        if (is_visible){
            ll_net_work.visibility =View.GONE
        }else{
            ll_net_work.visibility =View.VISIBLE
        }
    }

    override fun initToolBar() {
        if (isTitleBarBack()){
            tool_bar = binDing.root.findViewById(R.id.tool_bar)
            tv_value = this.findViewById(R.id.tv_value)
            tv_value.text = setValues()
            tool_bar.navigationIcon = resources.getDrawable(R.drawable.back)
            tool_bar.setNavigationOnClickListener { finish() }
        }
    }

    override fun initLifecycle() {
        lifecycle.addObserver(presenter)
    }

    override fun initData() {
        super.initData()
    }
    open fun setValues():String{
        return "创先泰克教育云"
    }

    open fun isTitleBarBack(): Boolean{
        return true
    }
    /**
     * Recycler初始化
     */

    @SuppressLint("WrongConstant")
    fun initRecycler(recycler : RecyclerView){
        var layoutManager = FullyLinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
    @SuppressLint("WrongConstant")
    fun initRecyclerGridView(recycler : RecyclerView){
        var layoutManager = GridLayoutManager(this,3)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(GridSpacingItemDecoration(3, DensityUtils().dip2px(13f), false))
    }
}