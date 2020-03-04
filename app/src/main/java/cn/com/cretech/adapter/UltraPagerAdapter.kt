package cn.com.cretech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import cn.com.cretech.R
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.databinding.ItemMicroDataBinding
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.util.EncodeImgZxing
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.ListenerHandlers
import com.bumptech.glide.Glide


class UltraPagerAdapter(var context: Context ,var dataList: MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var item_micro = LayoutInflater.from(context).inflate(R.layout.item_micro_data,null)
        var binding= DataBindingUtil.bind<ItemMicroDataBinding>(item_micro)
        binding!!.microModel = dataList.get(position)
        binding.listener = ListenerHandlers(context)
        var shake = AnimationUtils.loadAnimation(context,R.anim.anim)
        shake.reset()
        shake.fillAfter = true
        binding.ivMicroUpdate.startAnimation(shake)
        if (dataList.get(position).column_img != ""){
            LoadImage().loadImage(context,dataList.get(position).column_img,binding.ivUltra)
        }else{
            var qrImage = EncodeImgZxing.createQRImage(
                dataList.get(position).qrc,
                DensityUtils().dip2px(238f),
                DensityUtils().dip2px(325f),
                null,
                "")
            binding.ivUltra.setImageBitmap(qrImage)
        }
        container.addView(item_micro)
        return item_micro
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}