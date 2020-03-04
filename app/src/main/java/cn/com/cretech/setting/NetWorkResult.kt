package cn.com.cretech.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.com.cretech.R
import cn.com.cretech.bean.UserMessageBean
import cn.com.cretech.databinding.DialogExistUserBinding
import cn.com.cretech.util.SpUtils
import cn.com.cretech.widget.CustomPopupDialog
import retrofit2.Response

open class NetWorkResult<T> : INetWorkResult<T>{
    override fun onSucceed(response: Response<T>) {
    }

    /**
     * 网络请求失败
     */
    override fun onNothing() {

    }

    /**
     * 网络请求错误
     */
    override fun onError() {

    }
}