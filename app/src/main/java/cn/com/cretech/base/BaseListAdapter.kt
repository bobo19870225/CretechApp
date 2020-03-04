package cn.com.cretech.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.com.cretech.widget.ViewHolder

abstract class BaseListAdapter<B,T : ViewDataBinding>(var context : Context,var bean : List<B>) : BaseAdapter() {

    var bindDing : T? = null

    override fun getView( position : Int , convertView : View? ,parent : ViewGroup ): View {
        var viewHolder : ViewHolder?
        var v : View
        if (convertView == null ){
            v  = LayoutInflater.from(context).inflate(setRes(),parent,false)
            bindDing = DataBindingUtil.bind<T>(v)
            viewHolder = ViewHolder()
            v.tag = viewHolder
        }else{
            v = convertView
            v.tag as ViewHolder
        }
        initData(position)

        return v
    }

    abstract fun setRes() : Int
    fun initView(){}
    abstract fun initData(position : Int)

    override fun getItem(p0: Int): B {
        return bean.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return bean.size
    }
}