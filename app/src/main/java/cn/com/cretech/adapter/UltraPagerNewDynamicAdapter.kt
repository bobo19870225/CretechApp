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



class UltraPagerNewDynamicAdapter(
    var context: Context,
    var liveList: MutableList<LiveBean.DataBean>,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return liveList.size + resultList.size
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var item_micro : View?
        if (liveList.size>position){
            item_micro = LayoutInflater.from(context).inflate(R.layout.item_new_dynamic,null)
            var newDynamicBinding= DataBindingUtil.bind<ItemNewDynamicBinding>(item_micro)
            newDynamicBinding!!.dynamicModel = liveList.get(position)
            if (liveList.get(position).status == "正在直播"){
                newDynamicBinding!!.ivIsLive.setImageResource(R.drawable.yes_play)
            }else if (liveList.get(position).status == "尚未开始"){
                newDynamicBinding!!.ivIsLive.setImageResource(R.drawable.no_live_play)
            }else if (liveList.get(position).status == "直播已结束"){
                newDynamicBinding!!.ivIsLive.setImageResource(R.drawable.yes_finish)
            }else{
                newDynamicBinding!!.ivIsLive.setImageResource(R.drawable.yes_cancel)
            }
            ListenerHandlers(context).setLiveTimeImage(liveList.get(position) , newDynamicBinding.ivSchoolImage ,newDynamicBinding.tvSchoolTime)
            newDynamicBinding.llTitle.setOnClickListener {
                ListenerHandlers(context).onClickLive(true,liveList.get(position).liveurl,liveList.get(position).replay_url,liveList.get(position).status)
            }
        }else{
             item_micro = LayoutInflater.from(context).inflate(R.layout.item_micro_dynamic_data,null)
            var microDynamicDataBinding= DataBindingUtil.bind<ItemMicroDynamicDataBinding>(item_micro)
            var dynamicBean = resultList.get(position - liveList.size)

            LoadImage().loadImage(context,BaseLink().BASE_ALL_IMAGE+dynamicBean.h5_image,microDynamicDataBinding!!.ivUltra)
            microDynamicDataBinding.ivUltra.setOnClickListener {
                ListenerHandlers(context).onClickSchoolDynamic( resultList.get(position - liveList.size))
            }
        }

        container.addView(item_micro)
        return item_micro!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setNotify(
        addLiveList: MutableList<LiveBean.DataBean>,
        addResultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    ) {
        liveList.addAll(addLiveList)
        resultList.addAll(addResultList)
    }

}