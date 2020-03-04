package cn.com.cretech.setting

import android.content.Context
import android.util.Log
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.util.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface INetWorkResult<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.i("requestData", response.message())

        if (response.isSuccessful) {
            onSucceed(response)
        } else {
            Toast(
                BaseApplication.app,
                BaseApplication.app.resources.getString(R.string.net_server_error)
            )
            onNothing()
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onError()
    }

    fun onSucceed(response: Response<T>)
    fun onNothing()
    fun onError()

}