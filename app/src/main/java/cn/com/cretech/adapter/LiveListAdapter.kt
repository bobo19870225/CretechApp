package cn.com.cretech.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import cn.com.cretech.R
import cn.com.cretech.base.BaseAdapter
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.databinding.DialogTodayLiveBinding
import cn.com.cretech.databinding.ItemLiveBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.BaseHolder
import cn.com.cretech.widget.CustomPopupDialog
import cn.com.cretech.widget.ListenerHandlers

class LiveListAdapter(var context: Context,var is_today : Boolean , var  bean: MutableList<LiveBean.DataBean>) : BaseAdapter<LiveBean.DataBean,ItemLiveBinding>(context,R.layout.item_live, bean) {
    override fun convert(holder: BaseHolder, position: Int) {
        binDing.liveBean = bean.get(position)
        binDing.isToday = is_today
        var listener = ListenerHandlers(context)
        binDing.listener = listener
        listener.setLiveTimeImage(bean.get(position) ,binDing.ivLiveImage,binDing.tvTime )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if(is_today){
            listener.setBackground(bean.get(position).status ,binDing.tvIsStart )
            }else{
                binDing.tvIsStart.background = context.getDrawable(R.drawable.playback)
             }
        }

        holder.itemView.setOnClickListener {ListenerHandlers(context).diaLogTodayLive(is_today , bean.get(position)) }
    }


}