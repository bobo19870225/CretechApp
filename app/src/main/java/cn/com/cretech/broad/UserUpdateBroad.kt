package cn.com.cretech.broad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UserUpdateBroad : BroadcastReceiver() {

    var is_update = false

    override fun onReceive(context : Context?, intent: Intent?) {

        is_update = intent!!.getBooleanExtra("is_update",false)
    }
}