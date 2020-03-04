package cn.com.cretech.adapter

import android.content.Context
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import cn.com.cretech.R
import cn.com.cretech.base.BaseAdapter

import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.ItemMicroDynamicBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.ListenerHandlers

class DynamicSchoolAdapter(
    var context: Context,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : BaseAdapter<MicroDynamicBean.DynamicResult.ResultBean , ItemMicroDynamicBinding>(context, R.layout.item_micro_dynamic,resultList) {
    override fun convert(holder: BaseHolder, position: Int) {
        var binding = DataBindingUtil.bind<ItemMicroDynamicBinding>(holder.itemView)!!
        var dynamicBean = resultList.get(position)
        binding .dynamicModel = dynamicBean
        binding.listener = ListenerHandlers(context)
        if(dynamicBean.h5_image == ""){
            binding.ivDynamicImage.scaleType = ImageView.ScaleType.FIT_XY
        }else{
            LoadImage().loadImage(context, BaseLink().BASE_ALL_IMAGE+dynamicBean.h5_image,binding.ivDynamicImage)
        }
        holder.itemView.setOnClickListener { ListenerHandlers(context).diaLogSchool(dynamicBean) }
    }


    fun setNotify(
        addResultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    ) {
        resultList.addAll(addResultList)
        notifyDataSetChanged()
    }
}