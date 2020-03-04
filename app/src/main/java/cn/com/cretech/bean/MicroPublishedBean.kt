package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class MicroPublishedBean(
    var code: Int,
    var msg: String,
    var token: String,
    @SerializedName("data")
    var data: MutableList<DataBean>
) {
   data class DataBean(
        var id: Int,
        var navname: String
   )

}