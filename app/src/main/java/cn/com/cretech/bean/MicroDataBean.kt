package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class MicroDataBean(
     var code: Int,
     var msg: String,
     @SerializedName("data")
     var data: MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>
)