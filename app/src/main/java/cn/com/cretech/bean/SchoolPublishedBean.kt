package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SchoolPublishedBean(

     var code: Int,
     var msg: String,
     var token: String,
     @SerializedName("data")
     var data: MutableList<AreaData>
){

    data class AreaData(

         var id: Int ,
         var p_name: String,
         @SerializedName("child")
         var child: MutableList<ChildBean>
    ){
        data class ChildBean(
             var id: Int ,
             var class_name: String,
             var status: Int
        ) : Serializable
    }
}