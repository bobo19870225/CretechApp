package cn.com.cretech.iView

import androidx.recyclerview.widget.RecyclerView

interface ISchoolNavigationView {

    fun setAreaValues(area : String)
    fun setRecyclerView() : RecyclerView
    fun setCancelPull()

}