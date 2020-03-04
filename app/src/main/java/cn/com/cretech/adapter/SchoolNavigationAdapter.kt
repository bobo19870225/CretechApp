package cn.com.cretech.adapter

import android.content.Context
import cn.com.cretech.R
import cn.com.cretech.base.BaseAdapter
import cn.com.cretech.bean.SchoolNavigationBean
import cn.com.cretech.databinding.ItemSchoolNavigationBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.ListenerHandlers

class SchoolNavigationAdapter(var context: Context , var dataList : MutableList<SchoolNavigationBean.DataBean>) :
    BaseAdapter<SchoolNavigationBean.DataBean,ItemSchoolNavigationBinding>(context, R.layout.item_school_navigation,dataList) {
    override fun convert(holder: BaseHolder, position: Int) {
        binDing.dataBean = dataList.get(position)
        binDing.listener = ListenerHandlers(context)
        if (dataList.get(position).logo != null){
            LoadImage.loadImageViewIcon(binDing.ivSchoolHead , BaseLink().BASE_H5_IMAGE_URL + dataList.get(position).logo)
        }
    }
}