package cn.com.cretech.widget

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import cn.com.cretech.R
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.Toast
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.umeng.socialize.shareboard.ShareBoardConfig

class ShareUtil {

    lateinit var activity: Context

    private val umShareListener = object : UMShareListener {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        override fun onStart(platform: SHARE_MEDIA) {}

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        override fun onResult(platform: SHARE_MEDIA) {
            Toast(activity,"分享成功了")
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        override fun onError(platform: SHARE_MEDIA, t: Throwable) {
            Toast(activity,"失败了")
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        override fun onCancel(platform: SHARE_MEDIA) {
            Toast(activity,"取消了")
        }
    }


    constructor(
        activity: FragmentActivity,
        title: String,
        h5_url: String,
        show_h5_image: String,
        company_name: String
    ) {
        var title = title
        this.activity = activity

        val config =
            ShareBoardConfig()//新建ShareBoardConfig config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//设置位置
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM)
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR)
        config.setCancelButtonVisibility(true)


        if (title == "") {
            title = "创先泰克教育云"
        }
        val web = UMWeb(h5_url)
        web.title = title//标题
        if (show_h5_image != "") {
            if (show_h5_image.contains("http")) {
                web.setThumb(UMImage(activity, show_h5_image))  //缩略图
            } else {
                web.setThumb(UMImage(activity, BaseLink().BASE_ALL_IMAGE + show_h5_image))  //缩略图
            }
        } else {
            web.setThumb(UMImage(activity, R.drawable.icon_default))
        }

        web.description = company_name//学校
        ShareAction(activity).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
            .withMedia(web)
            .setCallback(umShareListener).open(config)

    }

    constructor(activity: FragmentActivity, h5_url: String, bitmap: Bitmap?, company_name: String) {
        var company_name = company_name
        this.activity = activity

        val config =
            ShareBoardConfig()//新建ShareBoardConfig config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//设置位置
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM)
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR)
        config.setCancelButtonVisibility(true)


        if (company_name == "") {
            company_name = "创先泰克教育云"
        }
        val umImage: UMImage
        if (bitmap != null) {
            umImage = UMImage(activity, bitmap)
        } else {
            umImage = UMImage(activity,BaseLink().BASE_ALL_IMAGE+ h5_url)
        }

        ShareAction(activity).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
            .withMedia(umImage)
            .setCallback(umShareListener).open(config)

    }
}
