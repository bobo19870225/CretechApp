package cn.com.cretech.adapter

import android.content.Context
import android.view.animation.AnimationUtils
import cn.com.cretech.R
import cn.com.cretech.base.BaseListAdapter
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.databinding.ItemChildMicroPublishedBinding
import cn.com.cretech.util.LoadImage
import cn.com.cretech.widget.ListenerHandlers

class ChildMicroPublishedAdapter(
    context : Context, var list: MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic> ) : BaseListAdapter<ChildMicroPublishedBean.DataBean.SchoolDynamic, ItemChildMicroPublishedBinding>(context, list) {
    override fun setRes(): Int {
        return R.layout.item_child_micro_published
    }

    override fun initData(position : Int) {

        bindDing!!.model = list.get(position)
        bindDing!!.listener = ListenerHandlers(context)
        LoadImage.loadImageView(bindDing!!.ivLiveImage , "https://weice.cretech.cn/"+list.get(position).column_img)
        var shake = AnimationUtils.loadAnimation(context,R.anim.anim)
        shake.reset()
        shake.fillAfter = true
        bindDing!!.ivUpdate.startAnimation(shake)
        bindDing!!.executePendingBindings()
    }

    fun setNotify(addList: MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>){
        list.addAll(addList)
        notifyDataSetChanged()
    }

}