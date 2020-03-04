package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName


data class DynamicSchoolBean(
     var code: Int,
     var msg: String,
     @SerializedName("data")
     var data : MutableList<MicroDynamicBean.DynamicResult.ResultBean>
)


