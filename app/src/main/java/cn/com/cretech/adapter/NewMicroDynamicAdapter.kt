package cn.com.cretech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.*
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.DynamicHolder
import cn.com.cretech.widget.ListenerHandlers
import cn.com.cretech.widget.ViewHolder

class NewMicroDynamicAdapter(
    var context: Context,
    var liveList: MutableList<LiveBean.DataBean>,
    var resultList: MutableList<MicroDynamicBean.DynamicResult.ResultBean>
) : BaseAdapter(){

    /**
     * 1：直播
     * 2：动态
     */
    var LIVE_SIGN = 1
    var RESULT_SIGN = 2

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var item_micro : View?
        var viewHolder : ViewHolder?
        if (convertView == null){
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
                var shake = AnimationUtils.loadAnimation(context,R.anim.anim)
                shake.reset()
                shake.fillAfter = true
                newDynamicBinding!!.ivIsLive.startAnimation(shake)
                ListenerHandlers(context).setLiveTimeImage(liveList.get(position) , newDynamicBinding.ivSchoolImage ,newDynamicBinding.tvSchoolTime)
                newDynamicBinding.llTitle.setOnClickListener {
                    ListenerHandlers(context).onClickLive(true,liveList.get(position).liveurl,liveList.get(position).replay_url,liveList.get(position).status)
                }
            }else{

                item_micro = LayoutInflater.from(context).inflate(R.layout.item_child_new_dynamic,null)
                val microDynamicDataBinding= DataBindingUtil.bind<ItemChildNewDynamicBinding>(item_micro)
                val dynamicBean = resultList.get(position - liveList.size)
                val shake = AnimationUtils.loadAnimation(context,R.anim.anim)
                shake.reset()
                shake.fillAfter = true
                microDynamicDataBinding!!.ivUpdate.startAnimation(shake)
                LoadImage.loadImageView(microDynamicDataBinding.ivLiveImage , BaseLink().BASE_ALL_IMAGE+dynamicBean.h5_image)
                microDynamicDataBinding.ivLiveImage.setOnClickListener {
                    ListenerHandlers(context).onClickSchoolDynamic( resultList.get(position - liveList.size))
                }

            }
            viewHolder = ViewHolder()
            item_micro.tag = viewHolder
        }else{
            item_micro = convertView
            item_micro.tag as ViewHolder
        }

        return item_micro!!
    }

    override fun getItem(p0: Int): Any {
        if (liveList.size>p0){
           return liveList[p0]
        }else{
            return resultList[p0 - liveList.size]
        }
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return liveList.size + resultList.size
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