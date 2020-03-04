package cn.com.cretech.activity

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.UserDataBean
import cn.com.cretech.broad.UserUpdateBroad
import cn.com.cretech.databinding.ActivityPersonMessageBinding
import cn.com.cretech.iView.IPersonMessageView
import cn.com.cretech.presenter.PersonMessagePresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.BitmapUtil
import cn.com.cretech.util.LoadImage
import cn.com.cretech.util.UriUtil
import java.io.File

class PersonMessageActivity : MVPBaseActivity<PersonMessagePresenter , ActivityPersonMessageBinding>() ,
    IPersonMessageView {


    var is_update = true // 是否更新用户信息
    /**
     * 修改图片
     */

    override fun setBitmap(btm: Bitmap) {
        binDing.circleImage.setImageBitmap(btm)
        sendBroadReceiver()
    }

    /**
     * 修改昵称
     */
    override fun setNickname(nickname: String) {
        binDing.tvNick.text = nickname
        sendBroadReceiver()
    }


    override fun setBindingData(userDataBean: UserDataBean.UserMessage) {
        binDing.userDataBean = userDataBean
        LoadImage.loadImageView(binDing.circleImage, BaseLink.BASE_USER_IMAGE+userDataBean.user_img)
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_person_message
    }

    override fun setValues(): String {
        return resources.getString(R.string.person_message)
    }

    override fun initView() {
        super.initView()
        presenter = PersonMessagePresenter(this , supportFragmentManager)
    }

    override fun setListener() {
        super.setListener()
        binDing.presenter = presenter

    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }

    private fun sendBroadReceiver(){
        if (is_update){
            var intent = Intent()
            intent.setAction("com.user.update")
            intent.putExtra("is_update",is_update)
            sendBroadcast(intent)
            is_update =false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            BaseLink.PHONE_PICTURE -> {
                presenter.uri = Uri.fromFile(presenter.cameraFile)
                presenter.file = File(presenter.uri.getPath()!!)
                presenter.photoImage.startPhotoZoom(presenter.uri,0,0)
            }
            BaseLink.SWITCH_PHONE_PICTURE -> {
                if (data != null) {
                    // 得到图片的全路径
                    presenter.uri = data.data!!
                    presenter.file = File(presenter.getRealPathFromURI(this , presenter.uri))
                    presenter.photoImage.startPhotoZoom(presenter.uri,0,0)
                }
            }
            BaseLink.ZOOM_IMAGE -> {
                var imagePath = presenter.photoImage.imagePath
                if (imagePath != null) {
                    val btm = presenter.photoImage.decodeUriAsBitmap(Uri.parse(imagePath))
                    val filePath = BitmapUtil.compressImage(presenter.file.getPath())
                    presenter.file = File(filePath)
                    presenter.updateHeadPhoto(btm)
                }
            }
            BaseLink.UPDATE_NICKNAME -> {

                if (intent != null){
                    binDing.tvNick.text = intent.getStringExtra("nickname")
                    sendBroadReceiver()
                }

            }
            BaseLink.UPDATE_PASSWORD -> {
                sendBroadReceiver()
            }
        }
    }

}