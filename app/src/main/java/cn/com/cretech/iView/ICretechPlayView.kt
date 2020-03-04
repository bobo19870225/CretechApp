package cn.com.cretech.iView

import android.view.SurfaceView
import android.widget.SeekBar

interface ICretechPlayView {

    fun setSurfaceView() : SurfaceView
    fun setSeekBar() : SeekBar
    fun isPlay(isPlay :Boolean)
    fun setTime(recordTime : String , countTime :String)

}