package cn.com.cretech.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class DensityUtils {

    fun PxFromdip(context: Context, pxValues: Float): Int {
        var res = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValues, res.displayMetrics)
            .toInt()
    }

    /**
     * dip转化像素
     * @param dipValue
     * @return
     */
    fun dip2px(dipValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()

    }

}