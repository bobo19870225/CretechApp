package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChildMicroPublishedBean(
    var code : Int ,
    var msg : String ,
    @SerializedName("data")
    var live_list : DataBean
) {


    data class DataBean(
         var totalNum: Int,
         @SerializedName("microLibrary")
         var microLibrary: MutableList<SchoolDynamic>
    ){
        data class SchoolDynamic(
            var column_img: String,
            var column_names: String,
            var company_id: String,
            var sticky: Int,
            var u_status: Int,
            var column_id: Int,
            var qrc: String
        ) : Serializable
    }
}