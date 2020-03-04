package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MicroDynamicBean(
     var code: Int,
     var msg: String,
     var data : DynamicResult
)
     {
     data class DynamicResult(
          @SerializedName("dynamic")
          var result: MutableList<ResultBean>,
          @SerializedName("live")
          var live: MutableList<LiveBean.DataBean>
     ){
          data class ResultBean(
               var id: String,
               var titles: String,
               var content: String,
               var h5_image: String,
               var sticky: String,
               var h5_url: String,
               var company_id: String,
               var start_time: String,
               var create_time: String,
               var company_location: String,
               var company_name: String,
               var grade: String,
               var hitcount: String

          ) : Serializable
     }
     }

