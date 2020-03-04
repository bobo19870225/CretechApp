package cn.com.cretech.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.SurfaceView
import android.view.View
import android.widget.SeekBar
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.databinding.ActivityCretechPlayBinding
import cn.com.cretech.iView.ICretechPlayView
import cn.com.cretech.presenter.CretechPlayPresenter


class CretechPlayActivity : MVPBaseActivity<CretechPlayPresenter,ActivityCretechPlayBinding>(), ICretechPlayView {
    override fun isPlay(isPlay: Boolean) {
        binDing.isPlay =isPlay
    }


    override fun setSeekBar(): SeekBar {
        return binDing.seekBar
    }

    override fun setTime(recordTime : String , countTime :String) {
        binDing.tvRecordTime.text =recordTime
        binDing.tvCountTime.text =countTime
    }

    override fun setSurfaceView(): SurfaceView {
        return binDing.surfaceView
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_cretech_play
    }

    override fun isTitleBarBack(): Boolean {
        return false
    }
    lateinit var play : String
    var isLive : Boolean =false
    lateinit var mHandler: Handler
    var TIME = 0
    var currentPosition = 0
    var is_seek = false
    override fun initView() {
        super.initView()
        play = intent.getStringExtra("play")
        isLive = intent.getBooleanExtra("isLive",false)
        presenter = CretechPlayPresenter(play,isLive,this ,this ,supportFragmentManager)
        binDing.isPlay =true
        binDing.presenter =presenter
        mHandler = Handler(object : Handler.Callback{
            override fun handleMessage(p0: Message): Boolean {
                TIME += 1000
                if (TIME == 5000) {
                    binDing.llTime.visibility = (View.GONE)
                    TIME = 0
                    mHandler.removeMessages(0)
                } else {
                    mHandler.sendEmptyMessageDelayed(0, 1000)
                }
                return false
            }
        })
    }

    override fun setListener() {
        super.setListener()
        mHandler.sendEmptyMessageDelayed(0, 1000)
        binDing.surfaceView.setOnClickListener(View.OnClickListener {
            if (binDing.llTime.visibility == View.VISIBLE) {
                binDing.llTime.visibility = (View.GONE)
                TIME = 0
                mHandler.removeMessages(0)
            } else {
                binDing.llTime.visibility = (View.VISIBLE)
                mHandler.sendEmptyMessageDelayed(0, 1000)
            }
        })
    }

    override fun initData() {
        super.initData()
        presenter.setPlay()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //横竖屏切换前调用，保存用户想要保存的数据，以下是样例
        outState.putInt("currentPosition", currentPosition)
        outState.putBoolean("is_seek", is_seek)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 屏幕切换完毕后调用用户存储的数据，以下为样例：
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition")
            is_seek = savedInstanceState.getBoolean("is_seek")
        }
    }

}