package cn.com.cretech.base

import android.annotation.SuppressLint
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.mvpbase.BasePresenter
import cn.com.cretech.mvpbase.BaseView
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.widget.FullyLinearLayoutManager
import cn.com.cretech.widget.MyGridLayoutManager
import cn.com.cretech.widget.NestedGridView
import cn.com.cretech.widget.ScrollLinearLayoutManager

open class MVPBaseFragment<P : BasePresenter,T : ViewDataBinding> : BaseFragment<T>(),BaseView{

    lateinit var tool_bar : Toolbar
    lateinit var presenter : P
    override fun setLinearLayout(): LinearLayout {
        return getFragmentView().findViewById(R.id.ll_net_work)
    }

    override fun setNetWorkError(): TextView {
        return getFragmentView().findViewById(R.id.tv_net_work)
    }

    override fun setNetWorkClick(): TextView {
        return getFragmentView().findViewById(R.id.tv_click)
    }

    override fun setLayoutRes(): Int {
        return R.layout.net_error_default
    }

    override fun initLifecycle() {
        lifecycle.addObserver(presenter)
    }

    override fun initView() {
        if (isTitleBarBack()){
            tool_bar = binDing.root.findViewById(R.id.tool_bar)
            tool_bar.navigationIcon = resources.getDrawable(R.drawable.back)
        }
    }

    fun isTitleBarBack(): Boolean{
        return false
    }

    /**
     * Recycler初始化
     */

    @SuppressLint("WrongConstant")
    fun initRecycler(recycler : RecyclerView){
        var layoutManager = FullyLinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    @SuppressLint("WrongConstant")
    fun initRecyclerHORIZONTAL(recycler : RecyclerView){
        var layoutManager = ScrollLinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
    }
    @SuppressLint("WrongConstant")
    fun initRecyclerGridView(recycler : RecyclerView){
        var layoutManager = MyGridLayoutManager(context,3)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager

    }
    /**
     * GridView初始化
     */
    fun initGridView(gridView : NestedGridView){

        gridView.numColumns = 3
        gridView.verticalSpacing = DensityUtils().dip2px(15f)

    }
    /**
     * 刷新控件设置
     */
    fun initPullRefresh(){

    }

}