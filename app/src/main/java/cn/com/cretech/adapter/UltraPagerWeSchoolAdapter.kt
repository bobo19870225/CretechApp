package cn.com.cretech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import cn.com.cretech.R
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.ItemMicroDynamicDataBinding
import cn.com.cretech.databinding.ItemNewDynamicBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.ListenerHandlers


class UltraPagerWeSchoolAdapter(
    var context: Context,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return resultList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var item_micro: View?

        item_micro = LayoutInflater.from(context).inflate(R.layout.item_micro_dynamic_data, null)
        var microDynamicDataBinding = DataBindingUtil.bind<ItemMicroDynamicDataBinding>(item_micro)
        var dynamicBean = resultList.get(position)

        LoadImage().loadImage(
            context,
            BaseLink().BASE_ALL_IMAGE + dynamicBean.h5_image,
            microDynamicDataBinding!!.ivUltra
        )
        microDynamicDataBinding.ivUltra.setOnClickListener {
            ListenerHandlers(context).onClickSchoolDynamic(resultList.get(position))
        }


        container.addView(item_micro)
        return item_micro!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setNotify(
        addResultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    ) {
        resultList.addAll(addResultList)
    }

}