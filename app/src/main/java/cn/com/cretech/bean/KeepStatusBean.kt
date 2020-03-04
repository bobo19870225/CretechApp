package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class KeepStatusBean(
    var code : Int,
    var msg : String,
    @SerializedName("data")
    var data : DataBean
) {
    data class DataBean(
        var result : Int
    )
}