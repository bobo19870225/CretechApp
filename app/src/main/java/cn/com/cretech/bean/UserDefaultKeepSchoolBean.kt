package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class UserDefaultKeepSchoolBean(
    var code : Int ,
    var msg : String ,
    @SerializedName("data")
    var dataBean : DataBean
) {

    data class DataBean(
        var company_id : String ,
        var company_name : String
    )


}