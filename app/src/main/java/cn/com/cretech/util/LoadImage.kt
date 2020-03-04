package cn.com.cretech.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cn.com.cretech.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class LoadImage {

    companion object {
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImageView(image: ImageView, url: String) {
            Glide.with(image.context).load(url)
                .error(Glide.with(image.context).load(R.drawable.icon_default))
                .into(image)
        }

        @BindingAdapter("imageUrlIcon")
        @JvmStatic
        fun loadImageViewIcon(image: ImageView, url: String) {
            Glide.with(image.context).load(url)
                .error(Glide.with(image.context).load(R.drawable.icon))
                .into(image)
        }
    }
    /**
     * 加载图片
     */
    fun loadImage(context: Context , url : String ,image : ImageView){
        Glide
            .with(context)
            .load(url)
            .error(Glide.with(context).load(R.drawable.icon_default))
            .into(image)
    }


    fun setTime(currentTime: Int): String {

        val date = Date()
        date.hours = 0
        date.minutes = 0
        date.seconds = 0
        val time = date.time + currentTime * 1000
        val sf = SimpleDateFormat("hh:mm")
        val format = currentTime / 3600

        return if (format >= 12) {
            "下午：" + sf.format(time)
        } else {
            "上午：" + sf.format(time)
        }
    }

}