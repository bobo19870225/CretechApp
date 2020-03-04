package cn.com.cretech.adapter

import android.content.Context
import cn.com.cretech.R
import cn.com.cretech.base.BaseListAdapter
import cn.com.cretech.bean.SchoolPublishedBean
import cn.com.cretech.databinding.ItemChildAreaBinding
import cn.com.cretech.widget.ListenerHandlers

class ChildAreaAdapter(context: Context, var  areaChildList : MutableList<SchoolPublishedBean.AreaData.ChildBean>) : BaseListAdapter<SchoolPublishedBean.AreaData.ChildBean,ItemChildAreaBinding>(context,areaChildList) {
    override fun setRes(): Int {
        return R.layout.item_child_area
    }

    override fun initData(position: Int) {

        bindDing!!.areaChildModel = areaChildList.get(position)
        bindDing!!.listener = ListenerHandlers(context)

    }
}