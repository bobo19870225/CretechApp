package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class UpdateVersionBean(
    var code : Int,
    var msg : String,
    @SerializedName("data")
    var dataBean : DataBean

){

    data class DataBean(

        var is_update : Int,
        var download_address : String
    )
}