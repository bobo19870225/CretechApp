package cn.com.cretech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.ItemLiveBinding
import cn.com.cretech.databinding.ItemMicroDynamicBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.DynamicHolder
import cn.com.cretech.widget.ListenerHandlers

class MicroDynamicAdapter(
    var context: Context,
    var liveList: MutableList<LiveBean.DataBean>,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 1：直播
     * 2：动态
     */
    var LIVE_SIGN = 1
    var RESULT_SIGN = 2

    override fun getItemViewType(position: Int): Int {
        if (liveList.size > 0 && liveList.size > position){
            return LIVE_SIGN
        }else{
            return RESULT_SIGN
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == LIVE_SIGN){
                return BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_live, parent, false))
            }else{
                return DynamicHolder(LayoutInflater.from(context).inflate(R.layout.item_micro_dynamic, parent, false))
            }
    }

    override fun getItemCount(): Int {
        return liveList.size + resultList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseHolder){
           var liveBindingUtil =  DataBindingUtil.bind<ItemLiveBinding>(holder.itemView)
            liveBindingUtil!!.liveBean = liveList.get(position)
            var listener = ListenerHandlers(context)
            liveBindingUtil!!.listener = listener
            liveBindingUtil!!.isToday = true
            listener.setLiveTimeImage(liveList.get(position) , liveBindingUtil.ivLiveImage ,liveBindingUtil.tvTime)
            listener.setBackground(liveList.get(position).status ,liveBindingUtil.tvIsStart )
            holder.itemView.setOnClickListener { ListenerHandlers(context).diaLogTodayLive(liveList.get(position)) }
        }else{
            var binding = DataBindingUtil.bind<ItemMicroDynamicBinding>(holder.itemView)!!
            var dynamicBean = resultList.get(position - liveList.size)
            binding .dynamicModel = dynamicBean
            binding.listener = ListenerHandlers(context)
            if(dynamicBean.h5_image == ""){
                binding.ivDynamicImage.scaleType = ImageView.ScaleType.FIT_XY
            }else{
                LoadImage().loadImage(context,BaseLink().BASE_ALL_IMAGE+dynamicBean.h5_image,binding.ivDynamicImage)
            }
            holder.itemView.setOnClickListener { ListenerHandlers(context).diaLogSchool(dynamicBean) }
        }
    }
    fun setNotify(
        addLiveList: MutableList<LiveBean.DataBean>,
        addResultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
    ) {
        liveList.addAll(addLiveList)
        resultList.addAll(addResultList)
        notifyDataSetChanged()
    }
}