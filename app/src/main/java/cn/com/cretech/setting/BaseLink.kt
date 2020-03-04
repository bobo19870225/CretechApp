package cn.com.cretech.setting

import android.Manifest

class BaseLink {

     val BASE_URl = "http://mng1.cretech.cn/cretech/api.php/app/"

     val APP_VERSION = "app_version"
     val OFFICIAL = "official"
     val TYPE = "apptype"
     val TOKEN = "oauth_token"
     val OAUTH_TOKEN_SECRET = "oauth_token_secret"
     val BASE_H5_IMAGE_URL = "http://appdev1.cretech.cn/tadmin/static/"
     //val BASE_ALL_IMAGE = "http://mng1.cretech.cn/tadmin/"
     val BASE_ALL_IMAGE = "https://weice.cretech.cn/"

     var MYUSERMESSAGE = 1


     companion object{


         var BASE_SCHOOL_IMAGE = "http://mng1.cretech.cn/tadmin/"
         var ABOUT_BASE_URL = "http://sns1.cretech.cn/thinksns/api.php?"
         var BASE_USER_IMAGE = "http://mng1.cretech.cn/cretech/static/upload/"
         var BACK_TIME = 2000L
         var PUBLIC = "Public"
         var SHOW_ABOUT_US = "showAbout"//关于我们
         var SHOW_USER_AGREEMENT = "showAgreement"//用户协议
         val PERMISSIONINSTAL = 6

         @JvmField
         var BASE_URL = "ThinkSNS"
         @JvmField
         val CACHE_PATH = BASE_URL + "/image_cache" // 照片存放地址
         @JvmField
         var PHONE_PICTURE = 1//拍照
         @JvmField
         var SWITCH_PHONE_PICTURE = 2   //选择图片
         @JvmField
         var ZOOM_IMAGE : Int = 3       //拍照或选择图片玩成后进行的回掉

         var CONFIRM_PERMISSION = 4//权限回掉

         var UPDATE_NICKNAME = 5//修改昵称
         var UPDATE_PASSWORD = 6//修改密码

         val POSITION_UPDATE_NICKNAME = 7
         val POSITION_UPDATE_PASSWORD = 8

         var permission = arrayOf(
             Manifest.permission.CAMERA,
             Manifest.permission.WRITE_EXTERNAL_STORAGE,
             Manifest.permission.READ_EXTERNAL_STORAGE,
             Manifest.permission.RECORD_AUDIO,
             Manifest.permission.REQUEST_INSTALL_PACKAGES,
             Manifest.permission.WRITE_APN_SETTINGS
         )

     }

 }