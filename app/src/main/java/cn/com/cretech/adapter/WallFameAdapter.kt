package cn.com.cretech.adapter

import android.content.Context
import cn.com.cretech.R
import cn.com.cretech.base.BaseAdapter
import cn.com.cretech.base.BaseListAdapter
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.ItemWallFameBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.ListenerHandlers


class WallFameAdapter(
    var context: Context,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : BaseAdapter<MicroDynamicBean.DynamicResult.ResultBean,ItemWallFameBinding>(context, R.layout.item_wall_fame,resultList) {
    override fun convert(holder: BaseHolder, position: Int) {
        var dynamicBean = resultList.get(position % resultList.size)
        LoadImage().loadImage(
            context,
            BaseLink().BASE_ALL_IMAGE + dynamicBean.h5_image,
            binDing.ivWallFame
        )
        binDing.ivWallFame.setOnClickListener {
            ListenerHandlers(context).onClickSchoolDynamic(resultList.get(position% resultList.size))
        }
    }

    override fun getItemCount(): Int {
        if (resultList.size>1){
            return Int.MAX_VALUE
        }else{
            return resultList.size
        }

    }

    fun setNotify(
        addResultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    ) {
        resultList.addAll(addResultList)
    }

}