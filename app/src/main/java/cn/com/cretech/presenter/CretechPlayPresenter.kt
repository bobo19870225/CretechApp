package cn.com.cretech.presenter

import android.content.Context
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.SeekBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import cn.com.cretech.activity.CretechPlayActivity
import cn.com.cretech.iView.ICretechPlayView
import cn.com.cretech.mvpbase.BasePresenter
import com.alivc.player.AliVcMediaPlayer
import com.alivc.player.MediaPlayer

class CretechPlayPresenter(fragmentManager: FragmentManager) : BasePresenter(fragmentManager), SurfaceHolder.Callback {
    lateinit var context: Context
    lateinit var iView: ICretechPlayView
    lateinit var url: String
    var isLive: Boolean =false
    lateinit var mPlayer : AliVcMediaPlayer
    var currentPosition : Int = 0
   constructor(url : String ,isLive : Boolean , context: Context , iView : CretechPlayActivity , fm : FragmentManager): this(fm){
       this.url = url
       this.isLive = isLive
       this.context = context
       this.iView = iView
   }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        mPlayer.setSurfaceChanged()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        mPlayer.destroy()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        //准备播放
        preparePlay()
    }

    fun setPlay() {
        var surfaceHolder = iView.setSurfaceView().holder
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceHolder.addCallback(this)

    }

    fun preparePlay() {
        mPlayer = AliVcMediaPlayer(context,iView.setSurfaceView())
        mPlayer.setRefer("http://prog1.cretech.cn")
        mPlayer.volume = 15
        if (isLive){
            mPlayer.setMediaType(MediaPlayer.MediaType.Live)
        }else{
            mPlayer.setMediaType(MediaPlayer.MediaType.Vod)
        }


        mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT)
       //开启循环播放
        mPlayer.setCirclePlay(true)

        mPlayer.setPreparedListener(object :MediaPlayer.MediaPlayerPreparedListener {
            override fun onPrepared() {
                //mPlayer.prepareAndPlay(url)
            }
        })
        mPlayer.setPcmDataListener { bytes, i ->
            //音频数据回调接口，在需要处理音频时使用，如拿到视频音频，然后绘制音柱。
            //获取播放的当前时间，单位为毫秒
            currentPosition = mPlayer.currentPosition
            setChangerTime()
        }
        iView.setSeekBar().setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // mPlayer.seekTo(progress);
                currentPosition = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setChangerTime()
                mPlayer.seekTo(currentPosition)
            }
        })
        mPlayer.prepareAndPlay(url)
    }
    fun isPlay(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (mPlayer.isPlaying) {
                iView.isPlay(false)
                mPlayer.pause()
            } else {
                iView.isPlay(true)
                mPlayer.play()
            }
        }
    }
    private fun setChangerTime() {

        val duration = mPlayer.duration
        val hour = duration / 1000 / 60 / 60//小时
        var recordTime : String
        var countTime : String
        if (hour == 0) {
            val mm = duration / 1000 / 60//分钟
            val ss = duration / 1000 - mm * 60//秒

            countTime =  setMSTime(mm, ss)
        } else {
            val mm = duration / 1000 / 60 - hour * 60//分钟
            val ss = duration / 1000 - mm * 60 - hour * 60 * 60//秒
            countTime = setHMSTime(hour, mm, ss)
        }
        val hour1 = currentPosition / 1000 / 60 / 60//小时
        if (hour1 == 0) {
            val mm1 = currentPosition / 1000 / 60//分钟
            val ss1 = currentPosition / 1000 - mm1 * 60//秒
            recordTime = setMSTime(mm1, ss1)
        } else {
            val mm1 = currentPosition / 1000 / 60 - hour1 * 60//分钟
            val ss1 = currentPosition / 1000 - mm1 * 60 - hour1 * 60 * 60//秒
            recordTime = setHMSTime(hour1, mm1, ss1)
        }
        iView.setTime(recordTime,countTime)
        iView.setSeekBar().max = (duration)
        iView.setSeekBar().progress = (currentPosition)

    }

    private fun setMSTime(mm: Int, ss: Int): String {
        var str_mm = mm.toString()
        var str_ss = ss.toString()
        if (str_mm.length < 2) {
            str_mm = "0$mm"
        } else {
            str_mm = mm.toString() + ""
        }
        if (str_ss.length < 2) {
            str_ss = "0$ss"
        } else {
            str_ss = ss.toString() + ""
        }
        return "$str_mm:$str_ss"
    }

    private fun setHMSTime(hour: Int, mm: Int, ss: Int): String {
        var str_hour = hour.toString()
        var str_mm = mm.toString()
        var str_ss = ss.toString()
        if (str_hour.length < 2) {
            str_hour = "0$hour"
        } else {
            str_hour = hour.toString() + ""
        }
        if (str_mm.length < 2) {
            str_mm = "0$mm"
        } else {
            str_mm = mm.toString() + ""
        }
        if (str_ss.length < 2) {
            str_ss = "0$ss"
        } else {
            str_ss = ss.toString() + ""
        }
        return "$str_hour:$str_mm:$str_ss"
    }

    override fun onDestory(owner: LifecycleOwner) {
        super.onDestory(owner)
        mPlayer.destroy()
    }
}