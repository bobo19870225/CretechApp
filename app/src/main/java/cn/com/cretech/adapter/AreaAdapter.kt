package cn.com.cretech.adapter

import android.content.Context
import cn.com.cretech.R
import cn.com.cretech.base.BaseAdapter
import cn.com.cretech.bean.SchoolPublishedBean
import cn.com.cretech.databinding.ItemAreaBinding
import cn.com.cretech.widget.BaseHolder

class AreaAdapter(context: Context ,var  areaList : MutableList<SchoolPublishedBean.AreaData>) : BaseAdapter<SchoolPublishedBean.AreaData,ItemAreaBinding>(context , R.layout.item_area,areaList) {
    override fun convert(holder: BaseHolder, position: Int) {

        binDing.areaModel = areaList.get(position)
        binDing.gridNavList.adapter = ChildAreaAdapter(ctx , areaList.get(position).child)

    }
}