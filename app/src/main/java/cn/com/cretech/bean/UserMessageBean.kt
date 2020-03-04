package cn.com.cretech.bean

import com.google.gson.annotations.SerializedName

data class UserMessageBean(

    var code : Int,
    @SerializedName("data")
    var userList : UserMessage

) {

   data class UserMessage(
       var uid : Int,
       var oauth_token : String,
       var oauth_token_secret : String
   )
}