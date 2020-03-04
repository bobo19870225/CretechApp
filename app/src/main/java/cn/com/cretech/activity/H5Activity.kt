package cn.com.cretech.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.MicroDynamicBean
import cn.com.cretech.databinding.ActivityH5Binding
import cn.com.cretech.iView.IH5View
import cn.com.cretech.presenter.H5Presenter
import cn.com.cretech.util.DecodeImage
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.AttributesUtil
import cn.com.cretech.widget.ShareUtil
import com.google.zxing.Result
import com.umeng.socialize.UMShareAPI
import java.io.IOException
import java.net.URL
import java.net.URLConnection

class H5Activity : MVPBaseActivity<H5Presenter,ActivityH5Binding>() , IH5View {
    override fun setQrc() {
        if (bitmapResult.toString().contains("jump")) {
            binDing.webView.loadUrl(bitmapResult.toString())
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(bitmapResult.toString())
            startActivity(intent)
        }
        stopAudio()
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_h5
    }
    lateinit var audioManager: AudioManager
    lateinit var dynamicBean: MicroDynamicBean.DynamicResult.ResultBean
    var bitmapResult : Result?= null
    companion object {
        var isQR : Boolean =false
    }
    lateinit var handler : Handler
    override fun initView() {
        super.initView()
        presenter = H5Presenter( this , supportFragmentManager)
        tool_bar.inflateMenu(R.menu.h5_menu)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        AttributesUtil.initWebView(this,binDing.webView,dynamicBean.h5_url ,binDing.progressBar)
        handler = Handler(object :Handler.Callback{
            override fun handleMessage(p0: Message): Boolean {
                if (isQR) {
                    presenter.setQrc()
                } else {
                    Toast(this@H5Activity, "当前图片不是二维码")
                }
                return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
    override fun setValues(): String {
        dynamicBean = intent.getSerializableExtra("dynamicBean") as MicroDynamicBean.DynamicResult.ResultBean
        return dynamicBean.company_name
    }

    override fun setListener() {
        super.setListener()
        tool_bar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                ShareUtil(this@H5Activity,dynamicBean.titles,dynamicBean.h5_url,dynamicBean.h5_image,dynamicBean.company_name)
                return true
            }
        })
        binDing.webView.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                val htr = binDing.webView.hitTestResult
                if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE
                    || htr.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                    || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
                ) {
                    var longClickUrl = htr.getExtra()
                    getBitMBitmap(longClickUrl!!)
                }
                return false
            }

        })
    }

    fun getBitMBitmap(urlpath: String) {

        Thread(Runnable {
            try {
                val url = URL(urlpath)
                var conn: URLConnection? = null
                conn = url.openConnection()
                conn!!.connect()
                val `in` = conn.getInputStream()
                val map = BitmapFactory.decodeStream(`in`)
                bitmapResult = DecodeImage.handleQRCodeFormBitmap(map)
                if (bitmapResult == null) {
                    isQR = false
                } else {
                    isQR = true
                }
                handler.sendEmptyMessage(1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
        binDing.webView.reload()
        binDing.webView.clearCache(true)
        binDing.webView.clearHistory()
        binDing.webView.removeAllViews()
        binDing.webView.destroy()

    }

    override fun onPause() {

        super.onPause()
        stopAudio()

    }

    override fun onResume() {
        super.onResume()
        audioManager.abandonAudioFocus(adfocusChangeListener)
    }

    private fun stopAudio() {
        if (audioManager.isMusicActive()) {
            audioManager.requestAudioFocus(
                adfocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    /**
     * 获取音频焦点
     */
    private val adfocusChangeListener = AudioManager.OnAudioFocusChangeListener { }
}