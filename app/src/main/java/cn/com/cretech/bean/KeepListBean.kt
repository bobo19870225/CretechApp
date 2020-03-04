package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class KeepListBean(
     var code: Int,
     var msg: String,
     @SerializedName("data")
     var data: MutableList<DataBean>
) {

    data class DataBean(
        var id: Int,
        var uid: Int ,
        var company_id: String,
        var defaults: Int,
        var ctime: Int,
        var logo: String,
        var company_image: String,
        var province: Int ,
        var city: Int,
        var area: Int,
        var location: String,
        var abbreviation: String,
        var company_name: String,
        var grade: Int


    )

}