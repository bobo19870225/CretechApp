package cn.com.cretech.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import cn.com.cretech.R
import cn.com.cretech.activity.*
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.bean.LiveBean
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.bean.SchoolPublishedBean
import cn.com.cretech.databinding.DialogSchoolBinding
import cn.com.cretech.databinding.DialogTodayLiveBinding
import cn.com.cretech.fragment.MyFragment
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.LoadImage

import cn.com.cretech.util.SkipTo
import cn.com.cretech.util.Toast

class ListenerHandlers(var context: Context) {
    lateinit var myFragment: MyFragment

    constructor(context: Context, myFragment: MyFragment) : this(context) {
        this.myFragment = myFragment
    }

    /**
     * 我的界面
     *  is_login == true 跳转到用户信息界面
     *  is_login = false  跳转到登陆界面
     */
    fun setLoginListener(is_login: Boolean) {
        if (is_login)
            SkipTo(context).skipPersonMessage()
        else {
            SkipTo(context, myFragment).skipUserLogin()
        }
    }

    /**
     * 跳转注册界面
     */
    fun setRegisterListener() {
        SkipTo(context).skipUserRegister()
    }

    /**
     * 跳转重置密码界面
     */
    fun setResetPasswordListener() {
        SkipTo(context).skipResetPasswordRegister()
    }

    /**
     * 跳转到关注列表
     */
    fun setKeepList(isLogin: Boolean) {
        if (isLogin)
            SkipTo(context).skipKeepList()
        else {
            SkipTo(context, myFragment).skipUserLogin()
        }
    }
    /**
     * 跳转到微册专栏
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setMicroColumn(company_id: String, column_id: Int) {
        onClickCover(company_id, column_id)

    }
    /**
     * 跳转到关于我们
     */
    fun setAboutWe() {
        SkipTo(context).skipAboutWe()
    }

    /**
     * 跳转到封面图
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun onClickCover(company_id: String, column_id: Int) {

        val intent = Intent().setClass(context, CoverActivity::class.java)
        intent.putExtra("company_id", company_id)
        intent.putExtra("column_id", column_id)
        BaseApplication.company_id = company_id
        context.startActivity(intent)

    }

    /**
     * 跳转到学校导航页
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun onSchoolNavigation(areaChildModel: SchoolPublishedBean.AreaData.ChildBean) {

        val intent = Intent().setClass(context, SchoolNavigationActivity::class.java)
        intent.putExtra("areaChildModel", areaChildModel)
        context.startActivity(intent)

    }


    /**
     * 直播弹窗
     */
    var bindingUtil : DialogTodayLiveBinding? =null
    lateinit var custom : CustomPopupDialog<DialogTodayLiveBinding>
    fun diaLogTodayLive(liveBean : LiveBean.DataBean){
        diaLogTodayLive(true ,liveBean )
    }
    fun diaLogTodayLive(isToday : Boolean , liveBean : LiveBean.DataBean){
        if (bindingUtil == null){
            bindingUtil = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_today_live , null ,false)
            custom = CustomPopupDialog<DialogTodayLiveBinding>(context ,bindingUtil ,  false)
        }
        bindingUtil!!.liveBean = liveBean
        bindingUtil!!.listener = this
        bindingUtil!!.isToday = isToday
        setStatus(isToday , liveBean.status)
        bindingUtil!!.tvNumber.text = LoadImage().setTime(liveBean.livetime)
        LoadImage.loadImageView(bindingUtil!!.ivSchoolImage,setDialogImage(liveBean.grade,liveBean.column_id,liveBean.column_img,liveBean.logo))
        custom.show()
    }



    private fun setStatus(isToday: Boolean , status : String){

        if (isToday){
            when(status){
                "正在直播" ->
                    bindingUtil!!.ivLive.setImageResource(R.drawable.start_live)
                "尚未开始" ->
                    bindingUtil!!.ivLive.setImageResource(R.drawable.no_live)
                "直播已结束" ->
                    bindingUtil!!.ivLive.setImageResource(R.drawable.end_live)
                "因故取消" ->
                    bindingUtil!!.ivLive.setImageResource(R.drawable.call_live)
                "精彩回放" ->
                    bindingUtil!!.ivLive.setImageResource(R.drawable.jingcai_playback)
            }
        }else{
            bindingUtil!!.ivLive.setImageResource(R.drawable.jingcai_playback)
        }


    }

    fun setBackground(status : String , tvIsStart : TextView){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (status == "正在直播") {
                tvIsStart.background = context.getDrawable(R.drawable.being_start_live)
            } else if (status == "尚未开始") {
                tvIsStart.background = context.getDrawable(R.drawable.not_yet_live)
            } else if (status == "直播已结束") {
                tvIsStart.background = context.getDrawable(R.drawable.end_is_live)
            } else {
                tvIsStart.background = context.getDrawable(R.drawable.cancel_live)
            }
        }
    }

    fun setLiveTimeImage(liveBean: LiveBean.DataBean ,ivLiveImage : ImageView , tvTime : TextView?){
        if(liveBean.grade == 1){
            if (liveBean.column_id>1) {
                if (liveBean.column_img != null) {
                    LoadImage().loadImage(context,liveBean.column_img,ivLiveImage)
                } else if (liveBean.logo != null) {
                    LoadImage().loadImage(context,BaseLink().BASE_H5_IMAGE_URL+liveBean.logo,ivLiveImage)
                } else {
                    ivLiveImage.scaleType = ImageView.ScaleType.FIT_XY
                    ivLiveImage.setImageResource(R.drawable.icon_default)
                }

            } else {
                if (liveBean.logo != null) {
                    LoadImage().loadImage(context,BaseLink().BASE_H5_IMAGE_URL+liveBean.logo,ivLiveImage)
                } else {
                    ivLiveImage.scaleType = ImageView.ScaleType.FIT_XY
                    ivLiveImage.setImageResource(R.drawable.icon_default)
                }
            }
        }
        if (tvTime != null){
            tvTime.text = LoadImage().setTime(liveBean.livetime)
        }
    }

    fun setDialogImage(grade: Int, column_id: Int, column_img: String, logo: String): String {

        return if (grade == 1) {

            if (column_id > 0) {
                if (column_img != "") {
                    column_img
                } else {
                    BaseLink().BASE_H5_IMAGE_URL + logo
                }

            } else {
                BaseLink().BASE_H5_IMAGE_URL + logo
            }

        } else {
            ""
        }

    }

    /**
     * 点击直播 或 点播
     */
    fun onClickLive(isToday: Boolean ,liveurl : String,replay_url : String, status : String){
        if (isToday && status == "正在直播"){
            Toast(context,status)
            setPlay(liveurl , true)
        }
        if (!isToday){//点播
            Toast(context,"精彩回放")
            setPlay(replay_url , false)
        }
    }
    fun setPlay( play : String , isLive : Boolean){
        var intent = Intent().setClass(context , CretechPlayActivity::class.java)
        intent.putExtra("play",play)
        intent.putExtra("isLive",isLive)
        context.startActivity(intent)
    }
    /**
     * 点击十万加学校进入封面图
     */
    fun onClickCompanyName(company_id: String , grade: Int){
        if (grade == 1){
            //Toast(context,"十万加")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                onClickCover(company_id , 0)
            }
        }
    }

    /**
     * 学校h5
     */
    fun onClickSchoolDynamic(dynamicBean:  MicroDynamicBean.DynamicResult.ResultBean){

        var intent = Intent().setClass(context, H5Activity::class.java)
        intent.putExtra("dynamicBean",dynamicBean)
        context.startActivity(intent)
    }
    /**
     * 微册跳转学校
     */
    fun onClickMicroSchool(schoolDynamic: ChildMicroPublishedBean.DataBean.SchoolDynamic){
       var intent = Intent().setClass(context , SchoolStyleDynamicActivity::class.java)
        intent.putExtra("schoolDynamic",schoolDynamic)
        context.startActivity(intent)
    }
    /**
     * 学校动态弹窗
     */
    var schoolBindingUtil : DialogSchoolBinding? =null
    lateinit var schoolCustom : CustomPopupDialog<DialogSchoolBinding>
    fun diaLogSchool(dynamicBean : MicroDynamicBean.DynamicResult.ResultBean){
        if (schoolBindingUtil == null){
            schoolBindingUtil = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_school , null ,false)
            schoolCustom = CustomPopupDialog<DialogSchoolBinding>(context ,schoolBindingUtil ,  false)
        }
        LoadImage.loadImageView(schoolBindingUtil!!.ivSchoolImage , BaseLink.BASE_SCHOOL_IMAGE + dynamicBean.h5_image)

        schoolBindingUtil!!.dynamicModel = dynamicBean
        schoolCustom.show()
    }
}