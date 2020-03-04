package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class LiveBean(
  var code : Int ,
  var msg : String ,
  @SerializedName("data")
  var live_list : MutableList<DataBean>
) {

     data class  DataBean(
         var id_live: Int,
         var flag: Int,
         var date: String,
         var status: String,
          var theme: String,
          var content: String,
          var livetime: Int,
          var stop_time: Int ,
          var liveurl: String ,
          var livepower: Int,
          var livelocation: String,
          var livesubject: String,
          var liveorganizer: String,
          var livecloud_id: Int,
          var live_level: Int ,
          var live_type: Int,
          var liveshotmsg: String,
          var lastedittime: Int,
          var img_big_url: String,
          var img_small_url: String,
          var location: String,
          var livesubject_extra: String,
          var replay_url: String,
          var serverarea: Int,
          var servername: String,
          var videoname: String,
          var m3u8name: String,
          var is_replay: Int ,
          var is_deleted: Int ,
          var photographer: Int ,
          var company_id: String,
          var column_id: Int ,
          var column_img: String,
          var h5_url: String,
          var logo: String,
          var company_name: String,
          var company_location: String,
          var grade: Int
     )
}