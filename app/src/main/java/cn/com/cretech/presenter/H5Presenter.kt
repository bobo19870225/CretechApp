package cn.com.cretech.presenter

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.com.cretech.R
import cn.com.cretech.activity.H5Activity
import cn.com.cretech.iView.IH5View
import cn.com.cretech.mvpbase.BasePresenter

class H5Presenter( fm :FragmentManager) : BasePresenter(fm) {

    lateinit var iView :IH5View

    constructor(iView: H5Activity , fm: FragmentManager) : this(fm){
        this.iView = iView
    }

    fun setQrc(){
        var listData = mutableListOf(
            (iView as FragmentActivity).resources.getString(R.string.set_qrc),
            (iView as FragmentActivity).resources.getString(R.string.cancel)
        )
        onItemClickListener(object : OnItemClickListener{
            override fun setOnClick(position: Int) {
                when(position){
                    0 -> {
                        iView.setQrc()
                        cancel()
                    }
                    1 -> {
                        cancel()
                    }

                }
            }

        })
        getPhoneDialog((iView as FragmentActivity),listData)

    }

}