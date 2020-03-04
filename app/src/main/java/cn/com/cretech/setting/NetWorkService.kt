package cn.com.cretech.setting

import cn.com.cretech.bean.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface NetWorkService {


    /**
     * 直播数据接口
     */
    @POST("live/index?")
    @FormUrlEncoded
    fun addLiveData(@FieldMap map : Map<String,String>, @Field("flag") flag : String) : Call<LiveBean>

    /**
     * 微册馆标题
     */
    @POST("EducationCloud/EducationColumn?")
    @FormUrlEncoded
    fun addMicroPublishedTitleData(@FieldMap map : Map<String,String>) : Call<MicroPublishedBean>

    /**
     * 微册馆标题下的动态
     */
    @POST("EducationCloud/Educationdynamic?")
    @FormUrlEncoded
    fun addMicroPublishedTitleDynamicData(
        @FieldMap map : Map<String,String> ,
        @Field("p") p : Int,
        @Field("count") count : Int,
        @Field("nav_id") nav_id : Int,
        @Field("company_id") company_id : String) : Call<ChildMicroPublishedBean>

    /**
     * 获取用户默认的特别关注学校
     */
    @POST("User/user_default?")
    @FormUrlEncoded
    fun getUserDefaultSchoolId(
        @FieldMap map : Map<String,String> ,
        @Field("uid") uid : Int) : Call<UserDefaultKeepSchoolBean>

    /**
     * 获取微册下的最新动态
     */
    @POST("Webook/getNewH5lists?")
    @FormUrlEncoded
    fun getMicroDynamic(
        @FieldMap map : Map<String,String> ,
        @Field("p") p : Int,
        @Field("company_id") company_id : String) : Call<MicroDynamicBean>

    /**
     * 获取微册下的数据
     */
    @POST("Webook/getColumn?")
    @FormUrlEncoded
    fun getMicroData(
        @FieldMap map : Map<String,String> ,
        @Field("company_id") company_id : String) : Call<MicroDataBean>

    /**
     * 学校榜模块数据
     */
    @POST("Areadynamic/getNavigation?")
    @FormUrlEncoded
    fun getSchoolPublishedData(
        @FieldMap map : Map<String,String>) : Call<SchoolPublishedBean>

    /**
     * 获取用户数据
     */
    @POST("User/show?")
    @FormUrlEncoded
    fun getUserData(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int) : Call<UserDataBean>

    /**
     * 登陆
     */
    @POST("Oauth/login?")
    @FormUrlEncoded
    fun userLogin(
        @FieldMap map : Map<String,String>,
        @Field("username") username : String,
        @Field("password") password : String) : Call<UserMessageBean>
    /**
     * 修改头像
     */
    @Multipart
    @POST("User/edit?")
    fun updateHeadPhoto(
        @PartMap map : Map<String,String>,
        @Part("uid") uid : Int,
        @Part file : MultipartBody.Part) : Call<UpdateUserMessageBean>
    /**
     * 修改昵称
     */
    @FormUrlEncoded
    @POST("User/edit?")
    fun updateNickname(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("nickname") nickname : String) : Call<UpdateUserMessageBean>
    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("User/edit?")
    fun updatePassword(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("oldpassword") old_password : String,
        @Field("password") new_password : String) : Call<UpdateUserMessageBean>

    /**
     * 验证用户名是否存在
     */
    @FormUrlEncoded
    @POST("Oauth/CheckUsername?")
    fun isCheckUsername(
        @FieldMap map : Map<String,String>,
        @Field("username") username : String) : Call<UpdateUserMessageBean>
    /**
     * 发送注册验证码
     */
    @FormUrlEncoded
    @POST("Oauth/send_sign_code?")
    fun getYZM(
        @FieldMap map : Map<String,String>,
        @Field("phone") phone : String) : Call<UpdateUserMessageBean>
    /**
     * 发送注册验证码
     */
    @FormUrlEncoded
    @POST("Oauth/send_codes?")
    fun getLoginYZM(
        @FieldMap map : Map<String,String>,
        @Field("phone") phone : String) : Call<UpdateUserMessageBean>
    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("Oauth/signIn?")
    fun getRegister(
        @FieldMap map : Map<String,String>,
        @Field("username") username : String,
        @Field("phone") phone : String,
        @Field("code") code : String,
        @Field("password") password : String) : Call<UpdateUserMessageBean>
    /**
     * 发送重置密码验证码
     */
    @FormUrlEncoded
    @POST("Oauth/send_code?")
    fun getResetYZM(
        @FieldMap map : Map<String,String>,
        @Field("phone") phone : String) : Call<UpdateUserMessageBean>

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("User/reset_password?")
    fun getResetPassword(
        @FieldMap map : Map<String,String>,
        @Field("phone") phone : String,
        @Field("code") code : String,
        @Field("password") password : String) : Call<UpdateUserMessageBean>
    /**
     * 获取关注列表
     */
    @FormUrlEncoded
    @POST("User/user_following?")
    fun getKeepList(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("p") pageNumber : Int) : Call<KeepListBean>
    /**
     * 获取用户关注状态
     */
    @FormUrlEncoded
    @POST("User/follow_status?")
    fun getKeepStatus(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("company_id") company_id : String) : Call<KeepStatusBean>

    /**
     * 取消关注
     */
    @FormUrlEncoded
    @POST("User/unfollow?")
    fun getCancelKeep(
        @FieldMap map : Map<String,String>,
        @Field("company_id") company_id : String,
        @Field("uid") uid : Int) : Call<CodeMessageBean>
    /**
     * 关注
     */
    @FormUrlEncoded
    @POST("User/follow?")
    fun getKeep(
        @FieldMap map : Map<String,String>,
        @Field("company_id") company_id : String,
        @Field("uid") uid : Int) : Call<CodeMessageBean>
    /**
     * 获取用户封面图
     */
    @FormUrlEncoded
    @POST("Company/getCompany?")
    fun getCover(
        @FieldMap map : Map<String,String>,
        @Field("company_id") company_id : String) : Call<SchoolMessageBean>
    /**
     * 获取学校导航页
     */
    @FormUrlEncoded
    @POST("Company/getCompang?")
    fun getSchoolNavigation(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("nav_id") nav_id : Int) : Call<SchoolNavigationBean>
    /**
     * 设置默认关注
     */
    @FormUrlEncoded
    @POST("User/edit_default?")
    fun getDefaultKeepSchool(
        @FieldMap map : Map<String,String>,
        @Field("uid") uid : Int,
        @Field("company_id") company_id : String) : Call<CodeMessageBean>

    /**
     * 学校动态数据
     */
    @FormUrlEncoded
    @POST("Webook/getColumnLists?")
    fun getDynamicSchool(
        @FieldMap map : Map<String,String>,
        @Field("company_id") company_id : String,
        @Field("column_id") column_id : Int,
        @Field("p") pageNumber: Int) : Call<MicroDynamicBean>
    /**
     * 学校动态数据
     */
    @FormUrlEncoded
    @POST("Oauth/CheckAppVersion?")
    fun getUpdateVersion( @FieldMap map : Map<String,String>) : Call<UpdateVersionBean>

    /**
     * 我们的学校
     */
    @FormUrlEncoded
    @POST("Dynamic/getCompanyCampus?")
    fun getWeSchoolData( @FieldMap map : Map<String,String>,
                      @Field("company_id") company_id : String,
                      @Field("p") pageNumber: Int,
                      @Field("count") count : Int) : Call<WeSchoolBean>
    /**
     * 我们的学校
     */
    @FormUrlEncoded
    @POST("Dynamic/getCompanyFame?")
    fun getWallFameData( @FieldMap map : Map<String,String>,
                      @Field("company_id") company_id : String,
                      @Field("p") pageNumber: Int,
                      @Field("count") count : Int) : Call<WeSchoolBean>
}