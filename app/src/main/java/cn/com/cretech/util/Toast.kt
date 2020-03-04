package cn.com.cretech.util

import android.content.Context
import android.widget.Toast

class Toast{

    constructor(context : Context,any: Any){
        if(any is Int){
            Toast.makeText(context, any.toInt(), Toast.LENGTH_SHORT).show()
        }else if (any is String){
            Toast.makeText(context, any.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}