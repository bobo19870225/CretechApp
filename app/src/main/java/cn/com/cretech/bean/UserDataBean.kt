package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class UserDataBean(

     var code: Int ,
     var msg: String,
     @SerializedName("data")
     var data: UserMessage

) {
    data class UserMessage(
         var id: Int ,
         var username: String,
         var user_img: String,
         var phone: String,
         var location: String,
         var nickname: String
    )
}