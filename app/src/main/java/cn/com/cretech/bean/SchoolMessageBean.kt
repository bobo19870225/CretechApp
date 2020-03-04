package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SchoolMessageBean (
     var code: Int ,
     var msg: String,
     var token: String,
     @SerializedName("data")
     var data: DataBean
){
    data class DataBean(
         var company_id: String,
         var company_image: String,
         var company_name: String,
         var sum_count: String,
         var column_id: Int
    ) : Serializable
}